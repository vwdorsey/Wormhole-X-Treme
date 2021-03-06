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
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.logging.Level;

/**
 * WormholeXtreme Entity Listener.
 * 
 * @author Ben Echols (Lologarithm)
 * @author Dean Bailey (alron)
 */
public class WormholeXTremeEntityListener implements Listener {

    /**
     * Handle entity explode event.
     * 
     * @param explodeBlocks the explode blocks
     * @return true, if successful
     */
    private static boolean handleEntityExplodeEvent(final List<Block> explodeBlocks) {
        for (Block explodeBlock : explodeBlocks) {
            if (StargateManager.isBlockInGate(explodeBlock)) {
                final Stargate s = StargateManager.getGateFromBlock(explodeBlock);
                WXTLogger.prettyLog(Level.FINE, false, "Blocked Creeper Explosion on Stargate: \"" + s.getGateName() + "\"");
                return true;
            }
        }
        return false;
    }

    /**
     * Handle Player damage event.
     * 
     * @param event the event
     * @return true, if successful
     */
    private static boolean handlePlayerDamageEvent(final EntityDamageEvent event) {
        final Player p = (Player) event.getEntity();
        final Location current = p.getLocation();
        final Stargate closest = StargateManager.findClosestStargate(current);
        if ((closest != null) && (((closest.isGateCustom()
                ? closest.getGateCustomPortalMaterial()
                : closest.getGateShape() != null
                ? closest.getGateShape().getShapePortalMaterial()
                : Material.WATER) == Material.LAVA) || ((closest.getGateTarget() != null) && ((closest.getGateTarget().isGateCustom()
                ? closest.getGateTarget().getGateCustomPortalMaterial()
                : closest.getGateTarget().getGateShape() != null
                ? closest.getGateTarget().getGateShape().getShapePortalMaterial()
                : Material.WATER) == Material.LAVA)))) {
            final double blockDistanceSquared = StargateManager.distanceSquaredToClosestGateBlock(current, closest);
            if ((closest.isGateActive() || closest.isGateRecentlyActive()) && (((blockDistanceSquared <= (closest.isGateCustom()
                    ? closest.getGateCustomWooshDepthSquared()
                    : closest.getGateShape() != null
                    ? closest.getGateShape().getShapeWooshDepthSquared()
                    : 0)) && ((closest.isGateCustom()
                    ? closest.getGateCustomWooshDepth()
                    : closest.getGateShape() != null
                    ? closest.getGateShape().getShapeWooshDepth()
                    : 0) != 0)) || (blockDistanceSquared <= 16))) {
                WXTLogger.prettyLog(Level.FINE, false, "Blocked Gate: \"" + closest.getGateName() + "\" Proximity Event: \"" + event.getCause().toString() + "\" On: \"" + p.getName() + "\" Distance Squared: \"" + blockDistanceSquared + "\"");
                p.setFireTicks(0);
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.entity.EntityListener#onEntityDamage(org.bukkit.event.entity.EntityDamageEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!event.isCancelled() && (event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA))) {
            if (event.getEntity() instanceof Player) {
                if (handlePlayerDamageEvent(event)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.entity.EntityListener#onEntityExplode(org.bukkit.event.entity.EntityExplodeEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            final List<Block> explodeBlocks = event.blockList();
            if (handleEntityExplodeEvent(explodeBlocks)) {
                event.setCancelled(true);
            }
        }
    }
}