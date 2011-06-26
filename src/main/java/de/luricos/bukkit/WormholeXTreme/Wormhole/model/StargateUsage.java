/**
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme>
 *
 * Copyright (C) 2011  Ben Echols
 *                     Dean Bailey
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
package de.luricos.bukkit.WormholeXTreme.Wormhole.model;

import org.bukkit.entity.Player;

/**
 * StargateUsage helper class
 * 
 * @author lycano
 */
public class StargateUsage {

    private Player player;
    private Stargate stargate;
    private boolean hasPlayerUsedStargate = false;
    private boolean hasPlayerPermission = false;
    private boolean hasPlayerUseCooldown = false;
    private boolean isGateIrisActive = false;
    private boolean isGateActive = false;

    public StargateUsage() {
    }

    public StargateUsage setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public StargateUsage setStargate(Stargate stargate) {
        this.stargate = stargate;
        return this;
    }

    public StargateUsage setPlayerUsedStargate(boolean used) {
        this.hasPlayerUsedStargate = used;
        return this;
    }

    public StargateUsage setHasPlayerPermission(boolean has) {
        this.hasPlayerPermission = has;
        return this;
    }

    public StargateUsage setHasPlayerUseCooldown(boolean cooldown) {
        this.hasPlayerUseCooldown = cooldown;
        return this;
    }

    public StargateUsage setIsGateIrisActive(boolean active) {
        this.isGateIrisActive = active;
        return this;
    }

    public StargateUsage setIsGateActive(boolean active) {
        this.isGateActive = active;
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Stargate getStargate() {
        return this.stargate;
    }

    public boolean hasPlayerUsedStargate() {
        return this.hasPlayerUsedStargate;
    }

    public boolean hasPlayerPermission() {
        return this.hasPlayerPermission;
    }

    public boolean hasPlayerUseCooldown() {
        return this.hasPlayerUseCooldown;
    }

    public boolean isGateIrisActive() {
        return this.isGateIrisActive;
    }

    public boolean isGateActive() {
        return this.isGateActive;
    }
}
