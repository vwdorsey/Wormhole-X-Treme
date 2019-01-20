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
import de.luricos.bukkit.WormholeXTreme.Wormhole.player.WormholePlayer;
import de.luricos.bukkit.WormholeXTreme.Wormhole.player.WormholePlayerManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.CommandUtilities;
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
        WormholePlayer wormholePlayer = WormholePlayerManager.getRegisteredWormholePlayer(player);
        Stargate sourceGate = wormholePlayer.getStargate();

        if ((sourceGate != null) && (sourceGate.isGateLightsActive())) {
            //if (WXPermissions.checkPermission(player, sourceGate, PermissionType.DIALER)) {
                final String startnetwork = CommandUtilities.getGateNetwork(sourceGate);
                if (!sourceGate.getGateName().equals(args[0])) {
                    final Stargate target = StargateManager.getStargate(args[0]);
                    // No target
                    if (target == null) {
                        CommandUtilities.closeGate(sourceGate, false);
                        wormholePlayer.removeStargate(sourceGate);
                        
                        player.sendMessage(Messages.Error.TARGET_INVALID.toString());
                        return true;
                    }
                    
                    final String targetnetwork = CommandUtilities.getGateNetwork(target);
                    WXTLogger.prettyLog(Level.FINE, false, "Dial Target - Gate: \"" + target.getGateName() + "\" Network: \"" + targetnetwork + "\"");
                    // Not on same network
                    if (!startnetwork.equals(targetnetwork)) {
                        CommandUtilities.closeGate(sourceGate, false);
                        wormholePlayer.removeStargate(sourceGate);
                        
                        player.sendMessage(Messages.Error.TARGET_INVALID.toString() + " Not on same network.");
                        return true;
                    }
                    
                    if (sourceGate.isGateIrisActive()) {
                        sourceGate.toggleIrisActive(false);
                    }
                    
                    if (!target.getGateIrisDeactivationCode().equals("") && target.isGateIrisActive()) {
                        if ((args.length >= 2) && target.getGateIrisDeactivationCode().equals(args[1])) {
                            if (target.isGateIrisActive()) {
                                target.toggleIrisActive(false);
                                player.sendMessage(Messages.createErrorMessage("IDC accepted. Iris has been deactivated."));
                            }
                        }
                    }

                    if (sourceGate.dialStargate(target, false)) {
                        target.setLastUsedBy(player);
                        player.sendMessage(Messages.Info.CONNECTION_SUCCESSFUL.toString());
                    } else {
                        player.sendMessage(String.format(Messages.Error.TARGET_IN_USE.toString(), target.getGateName(), target.getLastUsedBy()));

                        CommandUtilities.closeGate(sourceGate, false);
                        wormholePlayer.removeStargate(sourceGate);
                    }
                } else {
                    player.sendMessage(Messages.Error.DIALING_SELF.toString());
                    CommandUtilities.closeGate(sourceGate, false);
                    wormholePlayer.removeStargate(sourceGate);
                }
            /*} else {
                player.sendMessage(Messages.Error.BAD_PERMISSIONS.toString());
                wormholePlayer.removeStargate(sourceGate);
            }*/
        } else {
            player.sendMessage(Messages.Error.GATE_NOT_ACTIVE.toString());
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
            return !CommandUtilities.playerCheck(sender) || doDial((Player) sender, arguments);
        }
        return false;
    }
}
