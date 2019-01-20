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
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.CommandUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Class WXRemove.
 * 
 * @author alron
 */
public class WXRemove implements CommandExecutor {

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String[] a = CommandUtilities.commandEscaper(args);
        if ((a.length >= 1) && (a.length <= 2)) {
            if (a[0].equalsIgnoreCase("-all")) {
                return false;
            }
            
            final Stargate s = StargateManager.getStargate(a[0]);

            if (s != null) {
                //if (!CommandUtilities.playerCheck(sender) || WXPermissions.checkPermission((Player) sender, s, PermissionType.REMOVE)) {
                    boolean destroy = false;
                    if ((a.length == 2) && a[1].equalsIgnoreCase("-all")) {
                        destroy = true;
                    }
                    CommandUtilities.gateRemove(s, destroy);

                    sender.sendMessage(Messages.createNormalMessage("Wormhole Removed: " + s.getGateName()));
                /*} else {
                    sender.sendMessage(Messages.Error.BAD_PERMISSIONS.toString());
                }*/

            } else {
                sender.sendMessage(Messages.Error.GATE_DOESNT_EXIST+ a[0] + ". Remember proper capitalization.");
            }
        } else {
            return false;
        }
        return true;
    }
}
