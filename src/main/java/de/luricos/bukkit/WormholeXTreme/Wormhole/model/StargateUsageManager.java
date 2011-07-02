/*
 *   Wormhole X-Treme Plugin for Bukkit
 *   Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 *   Copyright (C) 2011 Ben Echols
 *                      Dean Bailey
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.model;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luricos
 */
public class StargateUsageManager {
    protected static Map<String, StargateUsage> stargateUsageMap = new HashMap<String, StargateUsage>();
    
    public StargateUsageManager() {
    }
    
    public StargateUsage getStargateUsage(Player player) {
        String playerName = player.getName();
        if (playerName == null)
            return null;
        
        return this.getStargateUsage(playerName);
    }
    
    public StargateUsage getStargateUsage(String playerName) {
        if (StargateUsageManager.stargateUsageMap.containsKey(playerName)) {
            return StargateUsageManager.stargateUsageMap.get(playerName);
        }
        
        return null;
    }
    
    public final void addStargateUsage(Player player) {
        String playerName = player.getName();
        if (playerName == null)
            return;
            
        if (!StargateUsageManager.stargateUsageMap.containsKey(player.getName())) {
            StargateUsage stargateUsage = new StargateUsage();
            stargateUsage.setPlayer(player);
            
            //@TODO: monitor for adding a player to keyring
            StargateUsageManager.stargateUsageMap.put(playerName, stargateUsage);
        }
    }
    
    public void removeStargateUsage(Player player) {
        String playerName = player.getName();
        if (playerName == null)
            return;
        
        if (StargateUsageManager.stargateUsageMap.containsKey(playerName)) {
            //@TODO: monitor for removing a player from keyring
            StargateUsageManager.stargateUsageMap.remove(playerName);
        }
    }
}
