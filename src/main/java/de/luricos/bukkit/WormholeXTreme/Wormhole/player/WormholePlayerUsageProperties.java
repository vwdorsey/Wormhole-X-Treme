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
package de.luricos.bukkit.WormholeXTreme.Wormhole.player;

/**
 * Stargate usage entity permission helper
 * 
 * @author lycano
 */
public class WormholePlayerUsageProperties {
    private boolean hasUseCooldown;
    private boolean hasUsedStargate;
    private boolean hasPermission;
    private boolean hasReachedDestination;
    private boolean hasActivatedStargate;
    private boolean hasDialedStargate;
    private boolean hasReceivedIrisLockMessage;
    private boolean hasShutdownGate;
    private boolean hasReceivedRemoteActiveMessage;
    private boolean hasReceivedWasActivatedOther;
    private boolean hasReceivedInvalidTargetMessage;
    private boolean hasReceivedNoPermissionMessage;

    public WormholePlayerUsageProperties() {
        this(false, false, false, false, false, false, false, false, false, false, false, false);
    }
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown) {
        this(hasUseCooldown, false, false, false, false, false, false, false, false, false, false, false);
    }
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate) {
        this(hasUseCooldown, hasActivatedStargate, false, false, false, false, false, false, false, false, false, false);
    }
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate) {
        this(hasUseCooldown, hasActivatedStargate, hasUsedStargate, false, false, false, false, false, false, false, false, false);
    }
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission) {
        this(hasUseCooldown, hasActivatedStargate, hasUsedStargate, hasPermission, false, false, false, false, false, false, false, false);
    }
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission, boolean hasReachedDestination) {
        this(hasUseCooldown, hasActivatedStargate, hasUsedStargate, hasPermission, hasReachedDestination, false, false, false, false, false, false, false);
    }
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission, boolean hasReachedDestination, boolean hasDialedStargate) {
        this(hasUseCooldown, hasActivatedStargate, hasUsedStargate, hasPermission, hasReachedDestination, hasDialedStargate, false, false, false, false, false, false);
    }    

    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission, boolean hasReachedDestination, boolean hasDialedStargate, boolean hasShutdownGate) {
        this(hasUseCooldown, hasActivatedStargate, hasUsedStargate, hasPermission, hasReachedDestination, hasDialedStargate, hasShutdownGate, false, false, false, false, false);
    }    
    
    public WormholePlayerUsageProperties(boolean hasUseCooldown, boolean hasActivatedStargate, boolean hasUsedStargate, boolean hasPermission, boolean hasReachedDestination, boolean hasDialedStargate, boolean hasReceivedIrisLockMessage, boolean hasShutdownGate, boolean hasReceivedRemoteActiveMessage, boolean hasReceivedWasActivatedOther, boolean hasReceivedInvalidTargetMessage, boolean hasReceivedNoPermissionMessage) {
        this.hasUseCooldown = hasUseCooldown;
        this.hasActivatedStargate = hasActivatedStargate;
        this.hasUsedStargate = hasUsedStargate;
        this.hasPermission = hasPermission;
        this.hasReachedDestination = hasReachedDestination;
        this.hasDialedStargate = hasDialedStargate;
        this.hasReceivedIrisLockMessage = hasReceivedIrisLockMessage;
        this.hasShutdownGate = hasShutdownGate;
        this.hasReceivedRemoteActiveMessage = hasReceivedRemoteActiveMessage;
        this.hasReceivedWasActivatedOther = hasReceivedWasActivatedOther;
        this.hasReceivedInvalidTargetMessage = hasReceivedInvalidTargetMessage;
        this.hasReceivedNoPermissionMessage = hasReceivedNoPermissionMessage;
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
        return this.hasDialedStargate;
    }
    
    public boolean hasShutdownGate() {
        return this.hasShutdownGate;
    }
    
    public boolean hasReceivedIrisLockMessage() {
        return this.hasReceivedIrisLockMessage;
    }
    
    public boolean hasReceivedRemoteActiveMessage() {
        return this.hasReceivedRemoteActiveMessage;
    }
    
    public boolean hasReceivedInvalidTargetMessage() {
        return this.hasReceivedInvalidTargetMessage;
    }
    
    public boolean hasReceivedNoPermissionMessage() {
        return this.hasReceivedNoPermissionMessage;
    }    
    
    public boolean setHasReceivedWasActivatedOther() {
        return this.hasReceivedWasActivatedOther;
    }    
    
    public void setHasUseCooldown(boolean hasUseCooldown) {
        this.hasUseCooldown = hasUseCooldown;
    }
    
    public void setHasActivatedStargate(boolean activated) {
        this.hasActivatedStargate = activated;
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
    
    public void setHasDialedStargate(boolean dialed) {
        this.hasDialedStargate = dialed;
    }    

    public void setHasReceivedIrisLockMessage(boolean received) {
        this.hasReceivedIrisLockMessage = received;
    }
    
    public void setHasShutdownGate(boolean shutdown) {
        this.hasShutdownGate = shutdown;
    }
    
    public void setHasReceivedRemoteActiveMessage(boolean active) {
        this.hasReceivedRemoteActiveMessage = active;
    }
    
    public void setHasReceivedWasActivatedOther(boolean other) {
        this.hasReceivedWasActivatedOther = other;
    }
    
    public void setHasReceivedInvalidTargetMessage(boolean received) {
        this.hasReceivedInvalidTargetMessage = received;
    }
    
    public void setHasReceivedNoPermissionMessage(boolean received) {
        this.hasReceivedNoPermissionMessage = received;
    }    
}
