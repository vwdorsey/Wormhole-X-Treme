/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 * Copyright (C) 2011 Ben Echols
 *                    Dean Bailey
 *
 * This file is a modified version from the Bukkit Plugin PermissionsEx v1.20
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.commands;

import de.luricos.bukkit.WormholeXTreme.Wormhole.commands.exceptions.AutoCompleteChoicesException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class CommandManager {

	protected static final Logger logger = Bukkit.getLogger();
	protected Map<String, Map<CommandSyntax, CommandBinding>> listeners = new LinkedHashMap<String, Map<CommandSyntax, CommandBinding>>();
	protected Plugin plugin;

	public CommandManager(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register(CommandListener listener) {
		for (Method method : listener.getClass().getMethods()) {
			if (!method.isAnnotationPresent(Command.class)) {
				continue;
			}

			Command cmdAnnotation = method.getAnnotation(Command.class);

			Map<CommandSyntax, CommandBinding> commandListeners = listeners.get(cmdAnnotation.name());
			if (commandListeners == null) {
				commandListeners = new LinkedHashMap<CommandSyntax, CommandBinding>();
				listeners.put(cmdAnnotation.name(), commandListeners);
			}

			commandListeners.put(new CommandSyntax(cmdAnnotation.syntax()), new CommandBinding(listener, method));
		}

		listener.onRegistered(this);
	}

	public boolean execute(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		Map<CommandSyntax, CommandBinding> callMap = this.listeners.get(command.getName());

		if (callMap == null) { // No commands registered
			return false;
		}

		CommandBinding selectedBinding = null;
		int argumentsLength = 0;
		String arguments = StringUtils.implode(args, " ");

		for (Entry<CommandSyntax, CommandBinding> entry : callMap.entrySet()) {
			CommandSyntax syntax = entry.getKey();
			if (!syntax.isMatch(arguments)) {
				continue;
			}
			if (selectedBinding != null && syntax.getRegexp().length() < argumentsLength) { // match, but there already more fitted variant
				continue;
			}

			CommandBinding binding = entry.getValue();
			binding.setParams(syntax.getMatchedArguments(arguments));
			selectedBinding = binding;
		}

		if (selectedBinding == null) { // there is fitting handler
			sender.sendMessage(ChatColor.RED + "Error in command syntax. Check command help.");
			return true;
		}

		// Check permission
		if (sender instanceof Player) { // this method are not public and required permission
			if (!selectedBinding.checkPermissions((Player) sender)) {
				logger.warning("User " + ((Player) sender).getName() + " tried to access chat command \""
						+ command.getName() + " " + arguments
						+ "\", but doesn't have permission to do this.");
				sender.sendMessage(ChatColor.RED + "Sorry, you don't have enough permissions.");
				return true;
			}
		}

		try {
			selectedBinding.call(this.plugin, sender, selectedBinding.getParams());
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof AutoCompleteChoicesException) {
				AutoCompleteChoicesException autocomplete = (AutoCompleteChoicesException) e.getTargetException();
				sender.sendMessage("Autocomplete for <" + autocomplete.getArgName() + ">:");
				sender.sendMessage("    " + StringUtils.implode(autocomplete.getChoices(), "   "));
			} else {
				throw new RuntimeException(e.getTargetException());
			}
		} catch (Exception e) {
			logger.severe("Found bogus command handler for " + command.getName() + " command. (Is plugin is update?)");
			if (e.getCause() != null) {
				e.getCause().printStackTrace();
			} else {
				e.printStackTrace();
			}
		}

		return true;
	}

	public List<CommandBinding> getCommands() {
		List<CommandBinding> commands = new LinkedList<CommandBinding>();

		for (Map<CommandSyntax, CommandBinding> map : this.listeners.values()) {
			commands.addAll(map.values());
		}

		return commands;
	}

}



