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
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.CommandUtilities;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * The Class Force.
 * 
 * @author alron
 */
public class Force implements CommandExecutor {

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String[] a = CommandUtilities.commandEscaper(args);
        if (a.length == 1) {
            //if (!CommandUtilities.playerCheck(sender) || WXPermissions.checkPermission((Player) sender, PermissionType.CONFIG)) {
                if (a[0].equalsIgnoreCase("-all")) {
                    for (final Stargate gate : StargateManager.getAllGates()) {
                        CommandUtilities.closeGate(gate, true);
                    }
                    sender.sendMessage(Messages.createNormalMessage("All gates have been deactivated, darkened, and have had their iris (if any) opened."));
                } else if (StargateManager.isStargate(a[0])) {
                    CommandUtilities.closeGate(StargateManager.getStargate(a[0]), true);
                    sender.sendMessage(Messages.createNormalMessage(a[0] + " has been closed, darkened, and has had its iris (if any) opened."));
                } else {
                    sender.sendMessage(Messages.Error.TARGET_INVALID.toString());
                    return false;
                }

                if (CommandUtilities.playerCheck(sender)) {
                    WXTLogger.prettyLog(Level.INFO, false, "Player: \"" + sender.getName() + "\" ran wxforce: " + Arrays.toString(a));
                }
            /*} else {
                sender.sendMessage(Messages.Error.BAD_PERMISSIONS.toString());
            }*/
            return true;
        } else {
            return false;
        }
    }
}
