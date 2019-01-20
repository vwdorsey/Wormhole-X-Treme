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

import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.util.logging.Level;

/**
 * WormholeXTreme Block Listener.
 * 
 * @author Ben Echols (Lologarithm)
 * @author Dean Bailey (alron)
 */
public class WormholeXTremeBlockListener implements Listener {

    /**
     * Handle block break.
     * 
     * @param player
     *            the player
     * @param stargate
     *            the stargate
     * @param block
     *            the block
     * @return true, if successful
     */
    private static boolean handleBlockBreak(final Player player, final Stargate stargate, final Block block) {
        //final boolean allowed = WXPermissions.checkPermission(player, stargate, PermissionType.DAMAGE);
        if (/*allowed*/ true) {
            if (!WorldUtils.isSameBlock(stargate.getGateDialLeverBlock(), block)) {
                if ((stargate.getGateDialSignBlock() != null) && WorldUtils.isSameBlock(stargate.getGateDialSignBlock(), block)) {
                    player.sendMessage("Destroyed DHD Sign. You will be unable to change dialing target from this gate.");
                    player.sendMessage("You can rebuild it later.");
                    stargate.setGateDialSign(null);
                } else if (block.getType() == (stargate.isGateCustom()
                        ? stargate.getGateCustomIrisMaterial()
                        : stargate.getGateShape() != null
                        ? stargate.getGateShape().getShapeIrisMaterial()
                        : Material.STONE)) {
                    return true;
                } else {
                    if (stargate.isGateActive()) {
                        stargate.setGateActive(false);
                        stargate.fillGateInterior(Material.AIR);
                    }
                    if (stargate.isGateLightsActive()) {
                        stargate.lightStargate(false);
                        stargate.stopActivationTimer();
                        
                        StargateManager.removeActivatedStargate(stargate.getGateName());
                    }
                    stargate.resetTeleportSign();
                    stargate.setupGateSign(false);
                    if (!stargate.getGateIrisDeactivationCode().equals("")) {
                        stargate.setupIrisLever(false);
                    }
                    if (stargate.isGateRedstonePowered()) {
                        stargate.setupRedstone(false);
                    }
                    StargateManager.removeStargate(stargate);
                    player.sendMessage("Stargate Destroyed: " + stargate.getGateName());
                }
            } else {
                player.sendMessage("Destroyed DHD. You will be unable to dial out from this gate.");
                player.sendMessage("You can rebuild it later.");
            }
            return false;
        } else {
            if (player != null) {
                WXTLogger.prettyLog(Level.FINE, false, "Player: " + player.getName() + " denied block destroy on: " + stargate.getGateName());
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockBreak(org.bukkit.event.block.BlockBreakEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            final Block block = event.getBlock();
            final Stargate stargate = StargateManager.getGateFromBlock(block);
            final Player player = event.getPlayer();
            if ((stargate != null) && handleBlockBreak(player, stargate, block)) {
                event.setCancelled(true);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockBurn(org.bukkit.event.block.BlockBurnEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!event.isCancelled()) {
            final Location current = event.getBlock().getLocation();
            final Stargate closest = StargateManager.findClosestStargate(current);
            //TODO This is bad, very bad for performance!
            if ((closest != null) && (closest.isGateActive() || closest.isGateRecentlyActive()) && ((closest.isGateCustom()
                    ? closest.getGateCustomPortalMaterial()
                    : closest.getGateShape() != null
                    ? closest.getGateShape().getShapePortalMaterial()
                    : Material.WATER) == Material.LAVA)) {
                final double blockDistanceSquared = StargateManager.distanceSquaredToClosestGateBlock(current, closest);
                if (((blockDistanceSquared <= (closest.isGateCustom()
                        ? closest.getGateCustomWooshDepthSquared()
                        : closest.getGateShape() != null
                        ? closest.getGateShape().getShapeWooshDepthSquared()
                        : 0)) && ((closest.isGateCustom()
                        ? closest.getGateCustomWooshDepth()
                        : closest.getGateShape() != null
                        ? closest.getGateShape().getShapeWooshDepth()
                        : 0) != 0)) || (blockDistanceSquared <= 25)) {
                    WXTLogger.prettyLog(Level.FINE, false, "Blocked Gate: \"" + closest.getGateName() + "\" Proximity Block Burn Distance Squared: \"" + blockDistanceSquared + "\"");
                    event.setCancelled(true);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockDamage(org.bukkit.event.block.BlockDamageEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDamage(BlockDamageEvent event) {
        if (!event.isCancelled()) {
            final Stargate stargate = StargateManager.getGateFromBlock(event.getBlock());
            final Player player = event.getPlayer();
            /*if ((stargate != null) && (player != null) && !WXPermissions.checkPermission(player, stargate, PermissionType.DAMAGE)) {
                event.setCancelled(true);
                WXTLogger.prettyLog(Level.FINE, false, "Player: " + player.getName() + " denied damage on: " + stargate.getGateName());
            }*/
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockFlow(org.bukkit.event.block.BlockFromToEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (!event.isCancelled()) {
            if (StargateManager.isBlockInGate(event.getToBlock()) || StargateManager.isBlockInGate(event.getBlock())) {
                event.setCancelled(true);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockIgnite(org.bukkit.event.block.BlockIgniteEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!event.isCancelled()) {
            final Location current = event.getBlock().getLocation();
            final Stargate closest = StargateManager.findClosestStargate(current);
            if ((closest != null) && (closest.isGateActive() || closest.isGateRecentlyActive()) && ((closest.isGateCustom()
                    ? closest.getGateCustomPortalMaterial()
                    : closest.getGateShape() != null
                    ? closest.getGateShape().getShapePortalMaterial()
                    : Material.WATER) == Material.LAVA)) {
                final double blockDistanceSquared = StargateManager.distanceSquaredToClosestGateBlock(current, closest);
                if (((blockDistanceSquared <= (closest.isGateCustom()
                        ? closest.getGateCustomWooshDepthSquared()
                        : closest.getGateShape().getShapeWooshDepthSquared())) && ((closest.isGateCustom()
                        ? closest.getGateCustomWooshDepth()
                        : closest.getGateShape() != null
                        ? closest.getGateShape().getShapeWooshDepth()
                        : 0) != 0)) || (blockDistanceSquared <= 25)) {
                    WXTLogger.prettyLog(Level.FINE, false, "Blocked Gate: \"" + closest.getGateName() + "\" Block Type: \"" + event.getBlock().getType().toString() + "\" Proximity Block Ignite: \"" + event.getCause().toString() + "\" Distance Squared: \"" + blockDistanceSquared + "\"");
                    event.setCancelled(true);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockPhysics(org.bukkit.event.block.BlockPhysicsEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (!event.isCancelled()) {
            final Block block = event.getBlock();
            if (StargateManager.isBlockInGate(block) && (!block.getType().equals(Material.REDSTONE_WIRE))) {
                event.setCancelled(true);
            }
        }
    }
}
