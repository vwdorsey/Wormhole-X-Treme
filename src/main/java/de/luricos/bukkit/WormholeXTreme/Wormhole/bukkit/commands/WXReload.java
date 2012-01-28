/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 * Copyright (C) 2011 Ben Echols
 *                    Dean Bailey
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
package de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands;

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author lycano
 */
public class WXReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandUtilities.playerCheck(sender) || WXPermissions.checkWXPermissions((Player) sender, PermissionType.CONFIG)) {
            final String[] a = CommandUtilities.commandEscaper(args);
            if ((a.length > 4) || (a.length == 0))
                return false;
            
            if ((a[0].equalsIgnoreCase("n")) || (a[0].equalsIgnoreCase("now"))) {
                if (WormholeXTreme.getThisPlugin().reloadPlugin()) {
                    sender.sendMessage(ConfigManager.MessageStrings.normalHeader + "Reloading complete");
                } else {
                    sender.sendMessage(ConfigManager.MessageStrings.normalHeader + "Error during reload see console log");
                }
            }
        } else {
            sender.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
        }
        
        return true;
    }
    
}
