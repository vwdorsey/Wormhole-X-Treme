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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.xml.crypto.Data;
import java.nio.ByteBuffer;

/**
 * WormholeXTreme DataUtils.
 * 
 * @author Ben Echols (Lologarithm)
 */
public class DataUtils {

    private DataUtils() {}

    /**
     * Block from bytes.
     * 
     * @param bytes the bytes
     * @param w the w
     * @return the block
     */
    public static Block blockFromBytes(final byte[] bytes, final World w) {
        final ByteBuffer b = ByteBuffer.wrap(bytes);
        return w.getBlockAt(b.getInt(), b.getInt(), b.getInt());
    }

    /**
     * Block location to bytes.
     * 
     * @param l the l
     * @return the byte[]
     */
    public static byte[] blockLocationToBytes(final Location l) {
        final ByteBuffer bb = ByteBuffer.allocate(12);

        bb.putInt(l.getBlockX());
        bb.putInt(l.getBlockY());
        bb.putInt(l.getBlockZ());

        return bb.array();
    }

    /**
     * Block to bytes.
     * 
     * @param b the b
     * @return the byte[]
     */
    public static byte[] blockToBytes(final Block b) {
        final ByteBuffer bb = ByteBuffer.allocate(12);

        bb.putInt(b.getX());
        bb.putInt(b.getY());
        bb.putInt(b.getZ());

        return bb.array();
    }

     /**
      * Byte array to int.
      * 
      * @param b the b
      * @param index the index
      * @return the int
      */
     public static int byteArrayToInt(final byte[] b, final int index) {
         return (b[index] << 24) + ((b[index + 1] & 0xFF) << 16) + ((b[index + 2] & 0xFF) << 8) + (b[index + 3] & 0xFF);
     }
     
    /**
     * Byte to boolean.
     * 
     * @param b the b
     * @return true, if successful
     */
    public static boolean byteToBoolean(final byte b) {
        return b >= 1;
    }

     /**
      * Int to byte array.
      * 
      * @param value the value
      * @return the byte[]
      */
     public static byte[] intToByteArray(final int value) {
         return new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
     }
     
    /**
     * Location from bytes.
     * 
     * @param bytes
     *            the bytes
     * @param w
     *            the w
     * @return the location
     */
    public static Location locationFromBytes(final byte[] bytes, final World w) {
        final ByteBuffer b = ByteBuffer.wrap(bytes);
        return new Location(w, b.getDouble(), b.getDouble(), b.getDouble(), b.getFloat(), b.getFloat());
    }

    /**
     * Location to bytes.
     * 
     * @param l the l
     * @return the byte[]
     */
    public static byte[] locationToBytes(final Location l) {
        final ByteBuffer b = ByteBuffer.allocate(32);
        b.putDouble(l.getX());
        b.putDouble(l.getY());
        b.putDouble(l.getZ());
        b.putFloat(l.getPitch());
        b.putFloat(l.getYaw());

        return b.array();
    }
}