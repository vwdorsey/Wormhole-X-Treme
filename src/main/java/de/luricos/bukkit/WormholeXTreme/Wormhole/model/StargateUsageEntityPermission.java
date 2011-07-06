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

/**
 * Stargate usage entity permission helper
 * 
 * @author lycano
 */
public class StargateUsageEntityPermission {
    private boolean hasUseCooldown = false;
    private boolean hasUsedStargate = false;
    private boolean hasPermission = false;
    private boolean hasReachedDestination = false;
    private boolean hasActivatedStargate = false;
    private boolean hasDialedStargate = false;
    private boolean hasReceivedIrisLockMessage = false;

    public StargateUsageEntityPermission() {
    }
    
    public StargateUsageEntityPermission(boolean hasUseCooldown) {
        this.hasUseCooldown = hasUseCooldown;
    }

    public StargateUsageEntityPermission(boolean hasUseCooldown, boolean hasActivatedStargate) {
        this.hasUseCooldown = hasUseCooldown;
        this.hasActivatedStargate = hasActivatedStargate;
    }

    public StargateUsageEntityPermission(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate) {
        this.hasUseCooldown = hasUseCooldown;
        this.hasActivatedStargate = hasActivatedStargate;
        this.hasUsedStargate = hasUsedStargate;
    }

    public StargateUsageEntityPermission(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission) {
        this.hasUseCooldown = hasUseCooldown;
        this.hasActivatedStargate = hasActivatedStargate;
        this.hasUsedStargate = hasUsedStargate;
        this.hasPermission = hasPermission;
    }

    public StargateUsageEntityPermission(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission, boolean hasReachedDestination) {
        this.hasUseCooldown = hasUseCooldown;
        this.hasActivatedStargate = hasActivatedStargate;
        this.hasUsedStargate = hasUsedStargate;
        this.hasPermission = hasPermission;
        this.hasReachedDestination = hasReachedDestination;
    }
    
    public StargateUsageEntityPermission(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission, boolean hasReachedDestination, boolean hasReceivedIrisLockMessage) {
        this.hasUseCooldown = hasUseCooldown;
        this.hasActivatedStargate = hasActivatedStargate;
        this.hasUsedStargate = hasUsedStargate;
        this.hasPermission = hasPermission;
        this.hasReachedDestination = hasReachedDestination;
        this.hasReceivedIrisLockMessage = hasReceivedIrisLockMessage;
    }
    
    
    public boolean hasUseCooldown() {
        return this.hasUseCooldown;
    }
    
    public boolean hasActivatedStargate() {
        return this.hasActivatedStargate;
    }
    
    public boolean hasUsedStargate() {
        return this.hasUsedStargate;
    }
    
    public boolean hasPermission() {
        return this.hasPermission;
    }
    
    public boolean hasReachedDestination() {
        return this.hasReachedDestination;
    }
    
    public boolean hasDialedStargate() {
        return this.hasDialedStargate();
    }
    
    public void setHasUseCooldown(boolean hasUseCooldown) {
        this.hasUseCooldown = hasUseCooldown;
    }
    
    public void setHasActivatedStargate(boolean activated) {
        this.hasActivatedStargate = activated;
    }
    
    public void setHasDialedStargate(boolean dialed) {
        this.hasDialedStargate = dialed;
    }
    
    public void setHasUsedStargate(boolean hasUsedStargate) {
        this.hasUsedStargate = hasUsedStargate;
    }
    
    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
    
    public void setHasReachedDestination(boolean hasReachedDestination) {
        this.hasReachedDestination = hasReachedDestination;
    }

    public void setHasReceivedIrisLockMessage(boolean received) {
        this.hasReceivedIrisLockMessage = true;
    }
    
    public boolean hasReceivedIrisLockMessage() {
        return this.hasReceivedIrisLockMessage;
    }
}
