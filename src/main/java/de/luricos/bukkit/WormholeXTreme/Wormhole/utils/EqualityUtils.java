package de.luricos.bukkit.WormholeXTreme.Wormhole.utils;

/**
 * Utility Methods used for Equality and Range Checking
 *
 * @author sir-dizzle
 */
public class EqualityUtils {

    public static boolean intValueInRange(int value, int min, int max) {
        return (value >= min && value <= max);
    }
}
