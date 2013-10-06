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
package de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.backends;

import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionBackend;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.entity.Player;

/**
 * @author lycano
 */
public class BukkitPermissionsSupport extends PermissionBackend {

    public BukkitPermissionsSupport(de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionManager manager, ConfigurationManager configManager, String providerName) {
        super(manager, configManager, providerName);
    }

    @Override
    public void initialize() {
        WXTLogger.info("Attached to Bukkit");
    }

    @Override
    public void reload() {
        this.end();
        this.initialize();
    }

    @Override
    public void end() {
        WXTLogger.info("Detached from BukkitPermissionsSupport");
    }

    @Override
    public boolean hasPermission(Player player, String permissionString) {
        return player.hasPermission(permissionString);
    }
}