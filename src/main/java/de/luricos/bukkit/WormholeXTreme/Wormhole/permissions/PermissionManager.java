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
package de.luricos.bukkit.WormholeXTreme.Wormhole.permissions;

import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.events.WormholeSystemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author lycano
 */
public class PermissionManager {

    protected PermissionBackend backend = null;
    protected ConfigManager configManager;

    public PermissionManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.initBackend();
    }

    private void initBackend() {
        // @TODO ues configManager instead of static call
        String backendName = ConfigManager.getConfigurations().get(ConfigManager.ConfigKeys.PERMISSIONS_BACKEND).getStringValue();

        if (backendName == null || backendName.isEmpty()) {
            backendName = PermissionBackend.defaultBackend;
            ConfigManager.setPermissionBackend(backendName);
        }

        this.setBackend(backendName);
    }

    public void setBackend(String backendName) {
        synchronized (this) {
            this.backend = PermissionBackend.getBackend(backendName, this, configManager);
            this.backend.initialize();
        }

        this.callEvent(WormholeSystemEvent.Action.PERMISSION_BACKEND_CHANGED);
    }

    protected void callEvent(WormholeSystemEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    protected void callEvent(WormholeSystemEvent.Action action) {
        this.callEvent(new WormholeSystemEvent(action));
    }

    public void reset() {
        if (this.backend != null) {
            this.backend.reload();
        }

        this.callEvent(WormholeSystemEvent.Action.RELOADED);
    }

    public void end() {
        reset();
    }

    public boolean has(Player player, String permissionString) {
        return backend.has(player, permissionString);
    }

    public boolean hasPermission(Player player, String permissionString) {
        return backend.hasPermission(player, permissionString);
    }
}
