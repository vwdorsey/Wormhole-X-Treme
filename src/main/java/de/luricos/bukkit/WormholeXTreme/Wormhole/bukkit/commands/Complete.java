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

import de.luricos.bukkit.WormholeXTreme.Wormhole.config.Messages;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.StargateRestrictions;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.CommandUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Class Complete.
 * 
 * @author alron
 */
public class Complete implements CommandExecutor {

    /**
     * Do complete.
     * 
     * @param player the player
     * @param args the args
     * @return true, if successful
     */
    private static boolean doComplete(final Player player, final String[] args) {
        final String name = args[0].trim().replace("\n", "").replace("\r", "");

        if (name.length() < 12) {
            String idc = "";
            String network = "Public";

            for (int i = 1; i < args.length; i++) {
                final String[] key_value_string = args[i].split("=");
                if (key_value_string[0].equals("idc")) {
                    idc = key_value_string[1];
                } else if (key_value_string[0].equals("net")) {
                    network = key_value_string[1];
                }
            }
            //if (WXPermissions.checkPermission(player, network, PermissionType.BUILD)) {
                //if (!StargateRestrictions.isPlayerBuildRestricted(player)) {
                    if (StargateManager.getStargate(name) == null) {
                        if (StargateManager.completeStargate(player, name, idc, network)) {
                            player.sendMessage(Messages.Info.BUILD_SUCCESS.toString());
                        } else {
                            player.sendMessage(Messages.createErrorMessage("Construction Failed!"));
                        }
                    } else {
                        player.sendMessage(Messages.Error.GATE_ALREADY_EXISTS.toString() + "\"" + name + "\"");
                    }
                /*} else {
                    player.sendMessage(Messages.Error.BUILD_RESTRICTIONS.toString());
                }*/
            /*} else {
                player.sendMessage(Messages.Error.BAD_PERMISSIONS.toString());
            }*/
        } else {
            player.sendMessage(Messages.Error.GATE_NAME_INVALID + "\"" + name + "\"");
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String[] arguments = CommandUtilities.commandEscaper(args);
        if ((arguments.length <= 3) && (arguments.length > 0)) {
            return !CommandUtilities.playerCheck(sender) || doComplete((Player) sender, arguments);
        }
        return false;
    }
}
