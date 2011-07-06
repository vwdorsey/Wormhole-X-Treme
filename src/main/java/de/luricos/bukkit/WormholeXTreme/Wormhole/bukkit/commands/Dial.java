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
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * The Class Dial.
 * 
 * @author alron
 */
public class Dial implements CommandExecutor {

    /**
     * Do dial.
     * 
     * @param player the player
     * @param args the args
     * @return true, if successful
     */
    private static boolean doDial(final Player player, final String[] args) {
        final Stargate start = StargateManager.removeActivatedStargate(player);
        if (start != null) {
            if (WXPermissions.checkWXPermissions(player, start, PermissionType.DIALER)) {
                final String startnetwork = CommandUtilities.getGateNetwork(start);
                if (!start.getGateName().equals(args[0])) {
                    final Stargate target = StargateManager.getStargate(args[0]);
                    // No target
                    if (target == null) {
                        CommandUtilities.closeGate(start, false);
                        player.sendMessage(ConfigManager.MessageStrings.targetInvalid.toString());
                        return true;
                    }
                    final String targetnetwork = CommandUtilities.getGateNetwork(target);
                    WXTLogger.prettyLog(Level.FINE, false, "Dial Target - Gate: \"" + target.getGateName() + "\" Network: \"" + targetnetwork + "\"");
                    // Not on same network
                    if (!startnetwork.equals(targetnetwork)) {
                        CommandUtilities.closeGate(start, false);
                        player.sendMessage(ConfigManager.MessageStrings.targetInvalid.toString() + " Not on same network.");
                        return true;
                    }
                    if (start.isGateIrisActive()) {
                        start.toggleIrisActive(false);
                    }
                    if (!target.getGateIrisDeactivationCode().equals("") && target.isGateIrisActive()) {
                        if ((args.length >= 2) && target.getGateIrisDeactivationCode().equals(args[1])) {
                            if (target.isGateIrisActive()) {
                                target.toggleIrisActive(false);
                                player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "IDC accepted. Iris has been deactivated.");
                            }
                        }
                    }

                    if (start.dialStargate(target, false)) {
                        player.sendMessage(ConfigManager.MessageStrings.gateConnected.toString());
                    } else {
                        CommandUtilities.closeGate(start, false);
                        player.sendMessage(ConfigManager.MessageStrings.targetIsActive.toString());
                    }
                } else {
                    CommandUtilities.closeGate(start, false);
                    player.sendMessage(ConfigManager.MessageStrings.targetIsSelf.toString());
                }
            } else {
                player.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
            }
        } else {
            player.sendMessage(ConfigManager.MessageStrings.gateNotActive.toString());
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String[] arguments = CommandUtilities.commandEscaper(args);
        if ((arguments.length < 3) && (arguments.length > 0)) {
            return CommandUtilities.playerCheck(sender)
                    ? doDial((Player) sender, arguments)
                    : true;
        }
        return false;
    }
}
