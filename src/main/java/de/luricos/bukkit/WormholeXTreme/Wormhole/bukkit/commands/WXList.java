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
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * The Class WXList.
 * 
 * @author alron
 */
public class WXList implements CommandExecutor {

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!CommandUtilities.playerCheck(sender) || WXPermissions.checkWXPermissions((Player) sender, PermissionType.LIST)) {
            final ArrayList<Stargate> gates = StargateManager.getAllGates();
            sender.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Available gates \u00A73::");
            StringBuilder sb = new StringBuilder();
            // TODO: Add checks for complex permissions enabled users running this command and only display what they have access to use.
            for (int i = 0; i < gates.size(); i++) {
                sb.append("\u00A77").append(gates.get(i).getGateName());
                if (i != gates.size() - 1) {
                    sb.append("\u00A78, ");
                }
                if (sb.toString().length() >= 75) {
                    sender.sendMessage(sb.toString());
                    sb = new StringBuilder();
                }
            }
            if (!sb.toString().equals("")) {
                sender.sendMessage(sb.toString());
            }

        } else {
            sender.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
        }
        return true;
    }
}
