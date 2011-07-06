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
package de.luricos.bukkit.WormholeXTreme.Wormhole.listeners;

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.player.PlayerOrientation;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.logic.StargateHelper;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateKeyring;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateShape;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateUsage;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateUsageManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.StargateRestrictions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WorldUtils;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Lever;

import java.util.logging.Level;

/**
 * WormholeXtreme Player Listener.
 * 
 * @author Ben Echols (Lologarithm)
 * @author Dean Bailey (alron)
 */
public class WormholeXTremePlayerListener extends PlayerListener {

    /**
     * Button lever hit.
     * 
     * @param player
     *            the p
     * @param clickedBlock
     *            the clicked
     * @param direction
     *            the direction
     * @return true, if successful
     */
    protected static boolean buttonLeverHit(Player player, Block clickedBlock, BlockFace direction) {
        Stargate stargate = StargateManager.getGateFromBlock(clickedBlock);

        if (stargate != null) {
            if (WorldUtils.isSameBlock(stargate.getGateDialLeverBlock(), clickedBlock) && ((stargate.isGateSignPowered() && WXPermissions.checkWXPermissions(player, stargate, PermissionType.SIGN)) || (!stargate.isGateSignPowered() && WXPermissions.checkWXPermissions(player, stargate, PermissionType.DIALER)))) {
                
                StargateKeyring keyring = (stargate.isGateSignPowered() ? StargateKeyring.SIGN : StargateKeyring.DIAL);
                if (!StargateUsageManager.getStargateUsage(player, keyring).isHuman())
                    StargateUsageManager.init(player, stargate);
                
                
                boolean activatedGate = handleGateActivationSwitch(player, keyring);
                StargateUsage stargateUsage = StargateUsageManager.getStargateUsage(player);
                
                if ((activatedGate) && (stargateUsage.getEntity().getState().hasActivatedStargate())) {                    
                    WXTLogger.prettyLog(Level.FINE, false, "Gate '" + StargateUsageManager.getStargateUsage(player).getStargate().getGateName() + "' was activated first but gate '" + stargate.getGateName() + "' was activated.");
                    if (stargateUsage.getStargate().getGateName() == null ? stargate.getGateName() != null : !stargateUsage.getStargate().getGateName().equals(stargate.getGateName())) {
//                        WXTLogger.prettyLog(Level.FINE, false, "Player '" + player.getName() + "' attempted to activate gate '" + StargateUsageManager.getStargateUsage(player).getStargate().getGateName() + "' without deactivating the old one. Shutting down old gate and redial current.");
//                        stargateUsage.getStargate().shutdownStargate(false);
//                        
                        StargateUsageManager.removeStargateUsage(player);
                        StargateUsageManager.init(player, stargate);
                        stargateUsage = StargateUsageManager.getStargateUsage(player);
                        
                        // if true gate was lightened up already
                        if (!handleGateActivationSwitch(player, keyring)) {
                            WXTLogger.prettyLog(Level.FINE, false, "Gate '" + stargateUsage.getStargate().getGateName() + "' didnt lightened up.");
                        }
                        
                        WXTLogger.prettyLog(Level.FINE, false, "New gate for player '" + player.getName() + "' was set to stargate '" + stargateUsage.getStargate().getGateName() + "'");
                    }
                } else {
                    WXTLogger.prettyLog(Level.FINE, false, "Gate '" + StargateUsageManager.getStargateUsage(player).getStargate().getGateName() + "' was remote active or player '" + player.getName() + "' has no permission");
                    StargateUsageManager.removeStargateUsage(player);
                }
                
            } else if (WorldUtils.isSameBlock(stargate.getGateIrisLeverBlock(), clickedBlock) && (!stargate.isGateSignPowered() && WXPermissions.checkWXPermissions(player, stargate, PermissionType.DIALER))) {
                Lever lever = new Lever(clickedBlock.getType(), clickedBlock.getData());
                WXTLogger.prettyLog(Level.FINE, false, "Player '" + player.getName() + "' has triggered the iris lever of gate '" + stargate.getGateName() + "' status is now " + (!lever.isPowered()));
                stargate.toggleIrisActive(true);
            } else if (WorldUtils.isSameBlock(stargate.getGateIrisLeverBlock(), clickedBlock) || WorldUtils.isSameBlock(stargate.getGateDialLeverBlock(), clickedBlock)) {
                player.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
            }
            
            StargateUsageManager.removeStargateUsage(player);
            return true;
        } else {
            if (direction == null) {
                switch (clickedBlock.getData()) {
                    case 1:
                        direction = BlockFace.SOUTH;
                        break;
                    case 2:
                        direction = BlockFace.NORTH;
                        break;
                    case 3:
                        direction = BlockFace.WEST;
                        break;
                    case 4:
                        direction = BlockFace.EAST;
                        break;
                    default:
                        break;
                }

                if (direction == null) {
                    return false;
                }
            }
            // Check to see if player has already run the "build" command.
            final StargateShape shape = StargateManager.getPlayerBuilderShape(player);

            Stargate newGate = null;
            if (shape != null) {
                newGate = StargateHelper.checkStargate(clickedBlock, direction, shape);
            } else {
                WXTLogger.prettyLog(Level.FINEST, false, "Attempting to find any gate shapes!");
                newGate = StargateHelper.checkStargate(clickedBlock, direction);
            }

            if (newGate != null) {
                if (WXPermissions.checkWXPermissions(player, newGate, PermissionType.BUILD) && !StargateRestrictions.isPlayerBuildRestricted(player)) {
                    if (newGate.isGateSignPowered()) {
                        player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Stargate Design Valid with Sign Nav.");
                        if (newGate.getGateName().equals("")) {
                            player.sendMessage(ConfigManager.MessageStrings.constructNameInvalid.toString() + "\"\"");
                        } else {
                            final boolean success = StargateManager.completeStargate(player, newGate);
                            if (success) {
                                player.sendMessage(ConfigManager.MessageStrings.constructSuccess.toString());
                                newGate.getGateDialSign().setLine(0, "-" + newGate.getGateName() + "-");
                                newGate.getGateDialSign().setData(newGate.getGateDialSign().getData());
                                newGate.getGateDialSign().update();
                            } else {
                                player.sendMessage("Stargate constrution failed!?");
                            }
                        }

                    } else {
                        // Print to player that it was successful!
                        player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Valid Stargate Design! \u00A73:: \u00A7B<required> \u00A76[optional]");
                        player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Type \'\u00A7F/wxcomplete \u00A7B<name> \u00A76[idc=IDC] [net=NET]\u00A77\' to complete.");
                        // Add gate to unnamed gates.
                        StargateManager.addIncompleteStargate(player, newGate);
                    }
                    return true;
                } else {
                    if (newGate.isGateSignPowered()) {
                        newGate.resetTeleportSign();
                    }
                    StargateManager.removeIncompleteStargate(player);
                    if (StargateRestrictions.isPlayerBuildRestricted(player)) {
                        player.sendMessage(ConfigManager.MessageStrings.playerBuildCountRestricted.toString());
                    }
                    player.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
                    return true;
                }
            } else {
                WXTLogger.prettyLog(Level.FINE, false, player.getName() + " has pressed a button or lever but we did not find a vallid gate shape");
                //player.sendMessage(ConfigManager.MessageStrings.gateWithInvalidShape.toString() + ConfigManager.MessageStrings.gateWithInvalidShapeAssistance);
            }
        }
        return false;
    }

    /**
     * Handle gate activation switch.
     * 
     * @param stargate
     *            the stargate
     * @param player
     *            the player
     * @return true, if successful
     */
    protected static boolean handleGateActivationSwitch(Player player, StargateKeyring keyring) {
        StargateUsage stargateUsage = StargateUsageManager.getStargateUsage(player, keyring);
        Stargate currentStargate = stargateUsage.getStargate();
        
        if (currentStargate.isGateActive() || currentStargate.isGateLightsActive()) {
            // if the gate has a target shut it down when lever changes state
            if (currentStargate.getGateTarget() != null) {
                //Shutdown stargate
                currentStargate.shutdownStargate(true);
                player.sendMessage(ConfigManager.MessageStrings.gateShutdown.toString());
                return true;
            } else {
                // there was no gate target (dial not executed)
                
                // check if there is an active gate already
                final Stargate targetGate = StargateManager.removeActivatedStargate(player);
                
                // if there was an activated gate shut it down and send deactivated message
                if ((targetGate != null) && (currentStargate.getGateId() == targetGate.getGateId())) {
                    currentStargate.stopActivationTimer();
                    currentStargate.setGateActive(false);
                    currentStargate.toggleDialLeverState(false);
                    currentStargate.lightStargate(false);
                    stargateUsage.getEntity().getState().setHasActivatedStargate(false);
                    player.sendMessage(ConfigManager.MessageStrings.gateDeactivated.toString());
                    return true;
                } else {
                    if (currentStargate.isGateLightsActive() && !currentStargate.isGateActive()) {
                        player.sendMessage(ConfigManager.MessageStrings.errorHeader.toString() + "Gate has been activated by someone else already.");
                    } else {
                        player.sendMessage(ConfigManager.MessageStrings.gateRemoteActive.toString());
                    }
                    return false;
                }
            }
        } else {
            if (currentStargate.isGateSignPowered()) {
                if (WXPermissions.checkWXPermissions(player, currentStargate, PermissionType.SIGN)) {
                    if ((currentStargate.getGateDialSign() == null) && (currentStargate.getGateDialSignBlock() != null)) {
                        currentStargate.tryClickTeleportSign(currentStargate.getGateDialSignBlock());
                    }

                    if (currentStargate.getGateDialSignTarget() != null) {
                        if (currentStargate.dialStargate(currentStargate.getGateDialSignTarget(), false)) {
                            player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Stargates connected!");
                            StargateUsageManager.getStargateUsage(player).getEntity().getState().setHasActivatedStargate(true);
                            WXTLogger.prettyLog(Level.FINE, false, "Player '" + player.getName() + "' has activated gate '" + StargateUsageManager.getStargateUsage(player).getStargate().getGateName() + "'");
                            return true;
                        } else {
                            player.sendMessage(ConfigManager.MessageStrings.gateRemoteActive.toString());
                            return false;
                        }
                    } else {
                        player.sendMessage(ConfigManager.MessageStrings.targetInvalid.toString());
                        return false;
                    }
                } else {
                    player.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
                    return false;
                }
            } else {
                //Activate Stargate
                player.sendMessage(ConfigManager.MessageStrings.gateActivated.toString());
                player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Chevrons locked! \u00A73:: \u00A7B<required> \u00A76[optional]");
                player.sendMessage(ConfigManager.MessageStrings.normalHeader.toString() + "Type \'\u00A7F/dial \u00A7B<gatename> \u00A76[idc]\u00A77\'");
                StargateManager.addActivatedStargate(player, currentStargate);
                currentStargate.startActivationTimer(player);
                currentStargate.lightStargate(true);
                return true;
            }
        }
    }

    /**
     * Handle player interact event.
     * 
     * @param event
     *            the event
     * @return true, if successful
     */
    protected static boolean handlePlayerInteractEvent(final PlayerInteractEvent event) {
        final Block clickedBlock = event.getClickedBlock();
        final Player player = event.getPlayer();

        if ((clickedBlock != null) && ((clickedBlock.getType().equals(Material.STONE_BUTTON)) || (clickedBlock.getType().equals(Material.LEVER)))) {
            if (buttonLeverHit(player, clickedBlock, null)) {
                return true;
            }
        } else if ((clickedBlock != null) && (clickedBlock.getType().equals(Material.WALL_SIGN))) {
            final Stargate stargate = StargateManager.getGateFromBlock(clickedBlock);
            if (stargate != null) {
                if (WXPermissions.checkWXPermissions(player, stargate, PermissionType.SIGN)) {
                    if (stargate.tryClickTeleportSign(clickedBlock, player)) {
                        return true;
                    }
                } else {
                    player.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Handle player move event.
     * 
     * @param event
     *            the event
     * @return true, if successful
     */
    protected void handlePlayerMoveEvent(final PlayerMoveEvent event) {
        Player player = event.getPlayer();
        final Location toLocFinal = event.getTo();
        final Block gateBlockFinal = toLocFinal.getWorld().getBlockAt(toLocFinal.getBlockX(), toLocFinal.getBlockY(), toLocFinal.getBlockZ());
        Stargate stargate = StargateManager.getGateFromBlock(gateBlockFinal);
        
        if ((stargate != null) && (stargate.isGateActive()) && (stargate.getGateTarget() != null) && (gateBlockFinal.getTypeId() == (stargate.isGateCustom()
                ? stargate.getGateCustomPortalMaterial().getId()
                : stargate.getGateShape() != null
                ? stargate.getGateShape().getShapePortalMaterial().getId()
                : Material.STATIONARY_WATER.getId())) && (!StargateUsageManager.getKeyring(stargate).equals(StargateKeyring.NONE)) 
                && (!StargateUsageManager.getStargateUsage(player, StargateUsageManager.getKeyring(stargate)).getEntity().getState().hasUsedStargate())) {

            StargateKeyring keyring = StargateUsageManager.getKeyring(stargate);
            StargateUsageManager.setStargateUsage(player, stargate, keyring);

            // you are not a human return
            if (!StargateUsageManager.getStargateUsage(player, keyring).getEntity().isHuman()) {
                WXTLogger.prettyLog(Level.WARNING, true, "Detected an unknown entity in Gate!");
                StargateUsageManager.removeStargateUsage(player);
                return;
            }
            
            StargateUsage stargateUsage = StargateUsageManager.getStargateUsage(player, keyring);
            
            String gatenetwork;
            if (stargate.getGateNetwork() != null) {
                gatenetwork = stargate.getGateNetwork().getNetworkName();
            } else {
                gatenetwork = "Public";
            }
            
            WXTLogger.prettyLog(Level.FINE, false, "Player in gate:" + stargate.getGateName() + " gate Active: " + stargate.isGateActive() + " Target Gate: " + stargate.getGateTarget().getGateName() + " Network: " + gatenetwork);

            
            // Teleportation logic
            if (!stargateUsage.getEntity().getState().hasReceivedIrisLockMessage()) {
                if (ConfigManager.getWormholeUseIsTeleport() && ((stargate.isGateSignPowered() && !WXPermissions.checkWXPermissions(player, stargate, PermissionType.SIGN)) || (!stargate.isGateSignPowered() && !WXPermissions.checkWXPermissions(player, stargate, PermissionType.DIALER)))) {
                    player.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());

                    stargateUsage.getEntity().getState().setHasPermission(false);
                    return;
                }

                if (ConfigManager.isUseCooldownEnabled()) {
                    if (StargateRestrictions.isPlayerUseCooldown(player)) {
                        player.sendMessage(ConfigManager.MessageStrings.playerUseCooldownRestricted.toString());
                        player.sendMessage(ConfigManager.MessageStrings.playerUseCooldownWaitTime.toString() + StargateRestrictions.checkPlayerUseCooldownRemaining(player));

                        // set cooldown state for entity
                        stargateUsage.getEntity().getState().setHasUseCooldown(true);

                        return;
                    } else {
                        StargateRestrictions.addPlayerUseCooldown(player);
                    }
                }

                if ((stargate.getGateTarget().isGateIrisActive()) && (!stargateUsage.getEntity().getState().hasReceivedIrisLockMessage())) {
                    player.sendMessage(ConfigManager.MessageStrings.errorHeader.toString() + "Remote Iris is locked!");
                    stargateUsage.getEntity().getState().setHasReceivedIrisLockMessage(true);
                    int wkbCount = ConfigManager.getWormholeKickbackBlockCount();
                    
                    if (wkbCount > 0) {
                        // teleport the player back two steps
                        // Against zero
                        // X North/South
                        // Y Up/Down
                        // Z West/East
                        // yaw 0 West (+z)
                        // yaw 90 North (-x)
                        // yaw 180 East (-z)
                        // yaw 270 South (+x)
                        player.setNoDamageTicks(5);

                        // get kickback direction
                        PlayerOrientation direction = stargateUsage.getEntity().getKickBackDirection(stargateUsage.getStargate().getGateFacing().getOppositeFace());

                        double pLocX = stargateUsage.getEntity().getPlayer().getLocation().getX();
                        double pLocY = stargateUsage.getEntity().getPlayer().getLocation().getY();
                        double pLocZ = stargateUsage.getEntity().getPlayer().getLocation().getZ();

                        WXTLogger.prettyLog(Level.FINE, false, "PlayerOrientation: " + direction.getName());
                        WXTLogger.prettyLog(Level.FINE, false, "old X:"+pLocX+", Y:"+pLocY+", Z:"+pLocZ);

                        // if needed move them far away
                        switch (direction) {
                            case NORTH:
                                WXTLogger.prettyLog(Level.FINE, false, "NORTH: " + pLocX + " - 2 = " + (pLocX-2d));
                                pLocX -= (double) wkbCount;
                                break;
                            case SOUTH:
                                WXTLogger.prettyLog(Level.FINE, false, "SOUTH: " + pLocX + " + 2 = " + (pLocX+2d));
                                pLocX += (double) wkbCount;
                                break;                        
                            case EAST:
                                WXTLogger.prettyLog(Level.FINE, false, "EAST: " + pLocZ + " - 2 = " + (pLocZ-2d));
                                pLocZ -= (double) wkbCount;
                                break;
                            case WEST:
                                WXTLogger.prettyLog(Level.FINE, false, "WEST: " + pLocZ + " + 2 = " + (pLocZ+2d));
                                pLocY += (double) wkbCount;
                                break;
                        }

                        // find new location for player
                        Location newLoc = new Location(player.getWorld(), pLocX, pLocY, pLocZ, player.getLocation().getYaw(), player.getLocation().getPitch());
                        pLocY = player.getWorld().getHighestBlockYAt(newLoc);

                        if (ConfigManager.getGateTransportMethod()) {
                            event.setTo(newLoc);
                            WXTLogger.prettyLog(Level.FINE, false, "Player was kicked back via event");
                        } else {
                            player.teleport(newLoc);
                            WXTLogger.prettyLog(Level.FINE, false, "Player was kicked back via teleport");
                            
                        }
                        
                        WXTLogger.prettyLog(Level.FINE, false, "new X:"+pLocX+", Y:"+pLocY+", Z:"+pLocZ);
                    }
                    
                    return;
                }

                final Location target = stargate.getGateTarget().getGatePlayerTeleportLocation();
                player.setNoDamageTicks(5);
                if (ConfigManager.getGateTransportMethod()) {
                    event.setTo(target);
                    WXTLogger.prettyLog(Level.FINE, false, "Player was transported via event");
                } else {
                    player.teleport(target);
                    WXTLogger.prettyLog(Level.FINE, false, "Player was transported via teleport");
                }

                if ((target != stargate.getGatePlayerTeleportLocation()) && (!stargateUsage.getEntity().getState().hasUsedStargate())) {
                    WXTLogger.prettyLog(Level.INFO, false, player.getName() + " used wormhole: " + stargate.getGateName() + " to go to: " + stargate.getGateTarget().getGateName());
                    stargateUsage.getEntity().getState().setHasUsedStargate(true);
                }

                if (ConfigManager.getTimeoutShutdown() == 0) {
                    stargate.shutdownStargate(true);
                }
            } else {
                WXTLogger.prettyLog(Level.FINE, false, "Player '" + player.getName() + "' has received IRISLOCK_MESSASGE unlocking player.");
                StargateUsageManager.removeStargateUsage(player);
            }
            
            return;
        } else if ((stargate != null) && (StargateUsageManager.getStargateUsage(player) != null) && (StargateUsageManager.getStargateUsage(player).getEntity().getState().hasReachedDestination())) {
            // @TODO: can be used for later stargate monitoring
            WXTLogger.prettyLog(Level.FINE, false, "Player '" + player.getName() + "' has safely reached destination.");
            StargateUsageManager.removeStargateUsage(player);
        }
        
        return;
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.player.PlayerListener#onPlayerBucketEmpty(org.bukkit.event.player.PlayerBucketEmptyEvent)
     */
    @Override
    public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
        if (!event.isCancelled()) {
            final Stargate stargate = StargateManager.getGateFromBlock(event.getBlockClicked());
            if ((stargate != null) || StargateManager.isBlockInGate(event.getBlockClicked())) {
                event.setCancelled(true);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.player.PlayerListener#onPlayerBucketFill(org.bukkit.event.player.PlayerBucketFillEvent)
     */
    @Override
    public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
        if (!event.isCancelled()) {
            final Stargate stargate = StargateManager.getGateFromBlock(event.getBlockClicked());
            if ((stargate != null) || StargateManager.isBlockInGate(event.getBlockClicked())) {
                event.setCancelled(true);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.player.PlayerListener#onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent)
     */
    @Override
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            WXTLogger.prettyLog(Level.FINE, false, "Caught Player: \"" + event.getPlayer().getName() + "\" Event type: \"" + event.getType().toString() + "\" Action Type: \"" + event.getAction().toString() + "\" Event Block Type: \"" + event.getClickedBlock().getType().toString() + "\" Event World: \"" + event.getClickedBlock().getWorld().toString() + "\" Event Block: " + event.getClickedBlock().toString() + "\"");
            
            if (handlePlayerInteractEvent(event)) {
                event.setCancelled(true);
                WXTLogger.prettyLog(Level.FINE, false, "Cancelled Player: \"" + event.getPlayer().getName() + "\" Event type: \"" + event.getType().toString() + "\" Action Type: \"" + event.getAction().toString() + "\" Event Block Type: \"" + event.getClickedBlock().getType().toString() + "\" Event World: \"" + event.getClickedBlock().getWorld().toString() + "\" Event Block: " + event.getClickedBlock().toString() + "\"");
            }
        } else {
            WXTLogger.prettyLog(Level.FINE, false, "Caught and ignored Player: \"" + event.getPlayer().getName() + "\" Event type: \"" + event.getType().toString() + "\"");
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.player.PlayerListener#onPlayerMove(org.bukkit.event.player.PlayerMoveEvent)
     */
    @Override
    public void onPlayerMove(final PlayerMoveEvent event) {
        handlePlayerMoveEvent(event);
        Player player = event.getPlayer();
        
        if (StargateUsageManager.getStargateUsage(player).isHuman() && (StargateUsageManager.getStargateUsage(player).getEntity().getState().hasUsedStargate())) {
            StargateUsage stargateUsage = StargateUsageManager.getStargateUsage(player);
            
            if ((stargateUsage != null) && (stargateUsage.getEntity().getState().hasUsedStargate())) {
                if (ConfigManager.isGateArrivalWelcomeMessageEnabled()) {
                    player.sendMessage(
                            String.format(ConfigManager.MessageStrings.playerUsedStargate.toString(),
                            "Gate " + stargateUsage.getStargate().getGateTarget().getGateName(),
                            " - created by " + stargateUsage.getStargate().getGateTarget().getGateOwner()));
                    WXTLogger.prettyLog(Level.FINE, false, "has received GATE_WELCOME_MESSAGE");
                } else{
                    WXTLogger.prettyLog(Level.FINE, false, "has disabled SHOW_GATE_WELCOME_MESSAGE");
                }
                
                stargateUsage.getEntity().getState().setHasReachedDestination(true);
            }
        }
    }
}
