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

import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.logic.StargateHelper;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Class Build.
 * 
 * @author alron
 */
public class BuildList implements CommandExecutor {

    /**
     * List available build shapes.
     * 
     * @param player the player
     * @param args the args
     * @return true, if successful
     */
    private static boolean listBuilds(final Player player, final String[] args) {
        if (!WXPermissions.checkPermission(player, PermissionType.CONFIG)) {
            return false;
        }

        int gateID = 1;
        StringBuilder shapeNames = new StringBuilder();
        for (String shapeName : StargateHelper.getShapeNames()) {
            shapeNames.append(ChatColor.GREEN + "(" + gateID + ")").append(ChatColor.GRAY + shapeName).append(", ");
            gateID++;
        }
        shapeNames.delete(shapeNames.length()-2, shapeNames.length());

        player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Available Shapes: " + shapeNames);
        return true;
    }

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (CommandUtilities.playerCheck(sender)) {
            final String[] arguments = CommandUtilities.commandEscaper(args);
            final Player player = (Player) sender;
            return listBuilds(player, arguments);
        }

        return true;
    }
}
