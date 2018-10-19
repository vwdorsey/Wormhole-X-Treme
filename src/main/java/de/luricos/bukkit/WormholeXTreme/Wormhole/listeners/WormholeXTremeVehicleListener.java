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

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.events.StargateMinecartTeleportEvent;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.StargateRestrictions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import java.util.logging.Level;

/**
 * WormholeXtreme Vehicle Listener.
 * 
 * @author Ben Echols (Lologarithm)
 * @author Dean Bailey (alron)
 */
public class WormholeXTremeVehicleListener implements Listener {

    /** The nospeed. */
    private final static Vector nospeed = new Vector();

    /**
     * Handle stargate minecart teleport event.
     * 
     * @param event
     *            the event
     * @return true, if successful
     */
    private static boolean handleStargateMinecartTeleportEvent(final VehicleMoveEvent event) {
        final Location l = event.getTo();
        final Block ch = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        final Stargate st = StargateManager.getGateFromBlock(ch);
        if ((st != null) && st.isGateActive() && (st.getGateTarget() != null) && (ch.getType() == (st.isGateCustom()
                ? st.getGateCustomPortalMaterial()
                : st.getGateShape() != null
                ? st.getGateShape().getShapePortalMaterial()
                : Material.LEGACY_STATIONARY_WATER))) {
            String gatenetwork;
            if (st.getGateNetwork() != null) {
                gatenetwork = st.getGateNetwork().getNetworkName();
            } else {
                gatenetwork = "Public";
            }
            
            Location target = st.getGateTarget().getGateMinecartTeleportLocation() != null
                    ? st.getGateTarget().getGateMinecartTeleportLocation()
                    : st.getGateTarget().getGatePlayerTeleportLocation();
            final Minecart veh = (Minecart) event.getVehicle();
            final Vector v = veh.getVelocity();
            veh.setVelocity(nospeed);
            final Entity e = veh.getPassenger();
            if ((e != null) && (e instanceof Player)) {
                final Player p = (Player) e;
                WXTLogger.prettyLog(Level.FINE, false, "Minecart Player in gate:" + st.getGateName() + " gate Active: " + st.isGateActive() + " Target Gate: " + st.getGateTarget().getGateName() + " Network: " + gatenetwork);
                if (ConfigManager.getWormholeUseIsTeleport() && ((st.isGateSignPowered() && !WXPermissions.checkPermission(p, st, PermissionType.SIGN)) || (!st.isGateSignPowered() && !WXPermissions.checkPermission(p, st, PermissionType.DIALER)))) {
                    p.sendMessage(ConfigManager.MessageStrings.permissionNo.toString());
                    return false;
                }
                if (st.getGateTarget().isGateIrisActive()) {
                    p.sendMessage(ConfigManager.MessageStrings.errorHeader.toString() + "Remote Iris is locked!");
                    veh.teleport(st.getGateMinecartTeleportLocation() != null
                            ? st.getGateMinecartTeleportLocation()
                            : st.getGatePlayerTeleportLocation());
                    if (ConfigManager.getTimeoutShutdown() == 0) {
                        st.shutdownStargate(true);
                    }
                    return false;
                }
                if (ConfigManager.isUseCooldownEnabled()) {
                    if (StargateRestrictions.isPlayerUseCooldown(p)) {
                        p.sendMessage(ConfigManager.MessageStrings.playerUseCooldownRestricted.toString());
                        p.sendMessage(ConfigManager.MessageStrings.playerUseCooldownWaitTime.toString() + StargateRestrictions.checkPlayerUseCooldownRemaining(p));
                        return false;
                    } else {
                        StargateRestrictions.addPlayerUseCooldown(p);
                    }
                }
            } else {
                if (st.getGateTarget().isGateIrisActive()) {
                    WXTLogger.prettyLog(Level.FINE, false, "Minecart in gate:" + st.getGateName() + " gate Active: " + st.isGateActive() + " Target Gate: " + st.getGateTarget().getGateName() + " Network: " + gatenetwork);
                    veh.teleport(st.getGateMinecartTeleportLocation() != null
                            ? st.getGateMinecartTeleportLocation()
                            : st.getGatePlayerTeleportLocation());
                    if (ConfigManager.getTimeoutShutdown() == 0) {
                        st.shutdownStargate(true);
                    }
                    return false;
                }

            }

            final double speed = v.length();
            final Vector new_speed = new Vector();
            switch (st.getGateTarget().getGateFacing()) {
                case NORTH:
                    new_speed.setX(-1);
                    break;
                case SOUTH:
                    new_speed.setX(1);
                    break;
                case EAST:
                    new_speed.setZ(-1);
                    break;
                case WEST:
                    new_speed.setZ(1);
                    break;
            }
            
            // As we all know stargates accelerate matter.
            new_speed.multiply(speed * 5);
            if (st.getGateTarget().isGateIrisActive()) {
                target = st.getGateMinecartTeleportLocation() != null
                        ? st.getGateMinecartTeleportLocation()
                        : st.getGatePlayerTeleportLocation();
                veh.teleport(target);
                veh.setVelocity(new_speed);
            } else {
                if (e != null) {
                    WXTLogger.prettyLog(Level.FINE, false, "Removing player from cart and doing some teleport hackery");
                    veh.eject();
                    veh.remove();
                    final Minecart newveh = target.getWorld().spawn(target, Minecart.class);
                    final Event teleportevent = new StargateMinecartTeleportEvent(veh, newveh);
                    WormholeXTreme.getThisPlugin().getServer().getPluginManager().callEvent(teleportevent);
                    e.teleport(target);
                    final Vector newnew_speed = new_speed;
                    WormholeXTreme.getScheduler().scheduleSyncDelayedTask(WormholeXTreme.getThisPlugin(), new Runnable() {

                        @Override
                        public void run() {
                            newveh.setPassenger(e);
                            newveh.setVelocity(newnew_speed);
                            newveh.setFireTicks(0);
                        }
                    }, 5);
                } else {
                    veh.teleport(target);
                    veh.setVelocity(new_speed);
                }
            }

            if (ConfigManager.getTimeoutShutdown() == 0) {
                st.shutdownStargate(true);
            }
            return true;
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.vehicle.VehicleListener#onVehicleMove(org.bukkit.event.vehicle.VehicleMoveEvent)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            handleStargateMinecartTeleportEvent(event);
        }
    }
}
