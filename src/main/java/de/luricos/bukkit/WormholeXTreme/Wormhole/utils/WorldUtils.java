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
package de.luricos.bukkit.WormholeXTreme.Wormhole.utils;

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Button;
import org.bukkit.material.Sign;

import java.util.logging.Level;

/**
 * WormholeXTreme WorldUtils.
 * 
 * @author Ben Echols (Lologarithm)
 */
public class WorldUtils {

    /**
     * Gets the degrees from block face.
     * 
     * @param blockFace
     *            the block face
     * @return the degrees from block face
     */
    public static Float getDegreesFromBlockFace(final BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
                return (float) 180;
            case EAST:
                return (float) 270;
            case SOUTH:
                return (float) 0;
            case WEST:
                return (float) 90;
            default:
                return (float) 0;
        }
    }

    /**
     * Gets the inverse direction.
     * 
     * @param blockFace
     *            the blockFace
     * @return the inverse direction
     */
    public static BlockFace getInverseDirection(final BlockFace blockFace) {
        return blockFace.getOppositeFace();
    }

    /**
     * Lever facing data from block face.
     *
     * use button facings for facing calculation because we have a block face here.
     * 
     * @param blockFace the BlockFace
     * @return the byte
     */
    public static byte getLeverFacingByteFromBlockFace(final BlockFace blockFace) {
        return getButtonFacingByteFromBlockFace(blockFace);
    }

    public static byte getButtonFacingByteFromBlockFace(final BlockFace blockFace) {
        Button buttonFacing = new Button(Material.STONE_BUTTON);
        buttonFacing.setFacingDirection(blockFace);

        return buttonFacing.getData();
    }

    /**
     * Gets the lever toggle byte.
     * 
     * @param leverState
     *            the lever state byte
     * @param isActive
     *            is this an active toggle?
     * @return the lever toggle byte
     */
    public static byte getLeverToggleByte(final byte leverState, final boolean isActive) {
        return (byte) (isActive
                ? (leverState & 0x8) != 0x8
                ? leverState ^ 0x8
                : leverState
                : (leverState & 0x8) == 0x8
                ? leverState ^ 0x8
                : leverState);
    }

    /**
     * Gets the perpendicular right direction.
     * 
     * @param blockFace
     *            the blockFace
     * @return the perpendicular right direction
     */
    public static BlockFace getPerpendicularRightDirection(final BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
            case UP:
                return BlockFace.EAST;
            case SOUTH:
            case DOWN:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.NORTH;
            case NORTH_EAST:
                return BlockFace.SOUTH_EAST;
            case SOUTH_WEST:
                return BlockFace.NORTH_WEST;
            case NORTH_WEST:
                return BlockFace.NORTH_EAST;
            case SOUTH_EAST:
                return BlockFace.SOUTH_WEST;
            default:
                return blockFace;
        }
    }

    /**
     * Gets the perpendicular left direction.
     *
     * @param blockFace
     *            the blockFace
     * @return the perpendicular left direction
     */
    public static BlockFace getPerpendicularLeftDirection(final BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
            case UP:
                return BlockFace.WEST;
            case SOUTH:
            case DOWN:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.NORTH;
            case WEST:
                return BlockFace.SOUTH;
            case NORTH_EAST:
                return BlockFace.NORTH_WEST;
            case SOUTH_WEST:
                return BlockFace.SOUTH_EAST;
            case NORTH_WEST:
                return BlockFace.SOUTH_WEST;
            case SOUTH_EAST:
                return BlockFace.NORTH_EAST;
            default:
                return blockFace;
        }
    }

    /**
     * Get the Sign facing byte data from block face.
     * If no face is up or down we default to south (same as bukkit).
     * 
     * @param blockFace
     *            the BlockFace
     * @return the byte
     */
    public static byte getSignFacingByteFromBlockFace(final BlockFace blockFace) {
        Sign signFacing = new Sign(Material.WALL_SIGN);
        signFacing.setFacingDirection(blockFace);

        return signFacing.getData();
    }

    /**
     * Checks if is same block.
     * 
     * @param b1
     *            the b1
     * @param b2
     *            the b2
     * @return true, if is same block
     */
    public static boolean isSameBlock(final Block b1, final Block b2) {
        if ((b1 == null) || (b2 == null)) {
            return false;
        }

        return (b1.getX() == b2.getX()) && (b1.getY() == b2.getY()) && (b1.getZ() == b2.getZ());
    }

    /**
     * Schedule chunk load.
     * 
     * @param b
     *            the b
     */
    public static void scheduleChunkLoad(final Block b) {
        final World w = b.getWorld();
        final Chunk c = b.getChunk();
        if (WormholeXTreme.getWorldHandler() != null) {
            WormholeXTreme.getWorldHandler().addStickyChunk(c, "WormholeXTreme");
        } else {
            final int cX = c.getX();
            final int cZ = c.getZ();
            if (!w.isChunkLoaded(cX, cZ)) {
                WXTLogger.prettyLog(Level.FINE, false, "Loading chunk: " + c.toString() + " on: " + w.getName());
                w.loadChunk(cX, cZ);
            }
        }
    }

    /**
     * Schedule chunk unload.
     * 
     * @param b
     *            the b
     */
    public static void scheduleChunkUnload(final Block b) {
        final World w = b.getWorld();
        final Chunk c = b.getChunk();
        if (WormholeXTreme.getWorldHandler() != null) {
            WormholeXTreme.getWorldHandler().removeStickyChunk(c, "WormholeXTreme");
        } else {
            final int cX = c.getX();
            final int cZ = c.getZ();
            if (w.isChunkLoaded(cX, cZ)) {
                WXTLogger.prettyLog(Level.FINE, false, "Scheduling chunk unload: " + c.toString() + " on: " + w.getName());
                w.unloadChunkRequest(cX, cZ);
            }
        }
    }
}
