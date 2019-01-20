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
package de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands;

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigLoader;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.Messages;
import de.luricos.bukkit.WormholeXTreme.Wormhole.logic.StargateHelper;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateDBManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionsManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions;
//import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.WXPermissions.PermissionType;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.CommandUtilities;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.EqualityUtils;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * The Class Wormhole.
 * 
 * @author alron
 * @author sir-dizzle
 */
public class Wormhole implements CommandExecutor {

    private static final String INFO_VALID_TIMEOUT = "Valid timeout is between 10 and 60 seconds.";
    private static final String INFO_VALID_BOOLS = "Valid boolean options are: true and false";

    private static final String ERR_INVALID_TARGET = "Chosen target is invalid. Ensure you have the correct gate.";

    /**
     * Do activate timeout.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doActivateTimeout(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Current activate_timeout is: " + ConfigLoader.getConfig().timeouts().predial());
            sender.sendMessage(INFO_VALID_TIMEOUT);
            return true;
        }

        try {
            final int timeout = Integer.parseInt(args[1]);
            if ((timeout >= 10) && (timeout <= 60)) {
                ConfigLoader.getConfig().timeouts().setPredial(timeout);
                sender.sendMessage("activate_timeout set to: " + ConfigLoader.getConfig().timeouts().predial());
            } else {
                sender.sendMessage("Invalid activate_timeout: " + args[1]);
                sender.sendMessage(INFO_VALID_TIMEOUT);
                return false;
            }
        } catch (final NumberFormatException e) {
            sender.sendMessage("Invalid activate_timeout: " + args[1]);
            sender.sendMessage(INFO_VALID_TIMEOUT);
            return false;
        }

        return true;
    }

    /** TODO: Integrate Cooldown Groups into new config format.
     * Do cooldown.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    /*
    private static boolean doCooldown(final CommandSender sender, final String[] args) {
        if ((args.length >= 2) && isValidGroupName(args[1])) {
            if (args.length == 3) {
                try {
                    final int timeout = Integer.parseInt(args[2]);
                    if ((timeout >= 15) && (timeout <= 3600)) {
                        doCooldownGroup(args[1], true, timeout);
                        sender.sendMessage("Wormhole cooldown time set to: " + args[2]);
                    } else {
                        sender.sendMessage("Invalid cooldown time: " + args[2]);
                        sender.sendMessage("Valid cooldown times are between 15 and 3600 seconds.");
                    }
                } catch (final NumberFormatException e) {
                    sender.sendMessage("Invalid cooldown time: " + args[2]);
                    sender.sendMessage("Valid cooldown times are between 15 and 3600 seconds.");
                }
            } else {
                sender.sendMessage("Current cooldown time is: " + doCooldownGroup(args[1], false, 0));
                sender.sendMessage("Valid cooldown times are between 15 and 3600 seconds.");
            }
        } else if ((args.length == 2) && CommandUtilities.isBoolean(args[1])) {
            ConfigManager.setUseCooldownEnabled(Boolean.valueOf(args[1].toLowerCase()));
            sender.sendMessage("Wormhole use cooldowns set to: " + args[1].toLowerCase());
        } else {
            sender.sendMessage("Command: /wormhole cooldown [false|true|group] <time>");
            sender.sendMessage("Valid groups are 'one', 'two', and 'three'.");
            sender.sendMessage("Valid cooldown times are between 15 and 3600 seconds.");
            sender.sendMessage("Wormhole use cooldowns currently enabled: " + ConfigManager.isUseCooldownEnabled());
        }

        return true;
    }*/

    /**
     * Do cooldown group.
     * 
     * @param groupName the group name
     * @param set the set
     * @param timeoutValue the timeout value
     * @return the int
     */
    /*
    private static int doCooldownGroup(final String groupName, final boolean set, final int timeoutValue) {
        int group = 0;
        int oldValue = 0;

        if (groupName.equalsIgnoreCase("one")) {
            group = 1;
        } else if (groupName.equalsIgnoreCase("two")) {
            group = 2;
        } else if (groupName.equalsIgnoreCase("three")) {
            group = 3;
        }

        switch (group) {
            case 1:
                if (set) {
                    oldValue = ConfigManager.getUseCooldownGroupOne();
                    ConfigManager.setUseCooldownGroupOne(timeoutValue);
                }
                return set
                        ? oldValue
                        : ConfigManager.getUseCooldownGroupOne();
            case 2:
                if (set) {
                    oldValue = ConfigManager.getUseCooldownGroupTwo();
                    ConfigManager.setUseCooldownGroupTwo(timeoutValue);
                }
                return set
                        ? oldValue
                        : ConfigManager.getUseCooldownGroupTwo();
            case 3:
                if (set) {
                    oldValue = ConfigManager.getUseCooldownGroupThree();
                    ConfigManager.setUseCooldownGroupThree(timeoutValue);
                }
                return set
                        ? oldValue
                        : ConfigManager.getUseCooldownGroupThree();
            default:
                return -1;
        }
    }*/

    /**
     * Do custom.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doCustom(final CommandSender sender, final String[] args) {
        if (!EqualityUtils.intValueInRange(args.length, 2, 3)) {
            sender.sendMessage("Syntax: /wormhole custom <stargate, -all> [boolean]");
            sender.sendMessage("Valid boolean options are: true or false");
            return true;
        }

        if (args[1].equalsIgnoreCase("-all") && (args.length == 3) && CommandUtilities.isBoolean(args[2])) {
            for (final Stargate stargate : StargateManager.getAllGates()) {
                setGateCustomAll(stargate, args[2].equalsIgnoreCase("true"));
            }
            sender.sendMessage("All stargates with valid shapes have been set to custom mode: " + args[2]);
            return true;
        } else if (StargateManager.isStargate(args[1])) {
            final Stargate stargate = StargateManager.getStargate(args[1]);
            if (args.length == 3) {
                if (CommandUtilities.isBoolean(args[2])) {
                    if (stargate.getGateShape() != null) {
                        setGateCustomAll(stargate, args[2].equalsIgnoreCase("true"));
                        sender.sendMessage("Stargate is custom: " + stargate.isGateCustom());

                        StargateDBManager.stargateToSQL(stargate);
                    } else {
                        sender.sendMessage("No gate shape to base custom data off of!");
                        sender.sendMessage("Make sure the proper shape file is available!");
                    }
                } else {
                    sender.sendMessage("Invalid boolean option: " + args[2]);
                    sender.sendMessage("Syntax: /wormhole custom [stargate, -all] [boolean]");
                    sender.sendMessage(INFO_VALID_BOOLS);
                }
            } else {
                sender.sendMessage("Stargate is custom: " + stargate.isGateCustom());
                sender.sendMessage(INFO_VALID_BOOLS);
            }
        } else {
            sender.sendMessage(ERR_INVALID_TARGET);
            sender.sendMessage("Syntax: /wormhole custom <stargate, -all> [boolean]");
            sender.sendMessage(INFO_VALID_BOOLS);
        }

        return true;
    }

    /**
     * Do iris material.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doIrisMaterial(final CommandSender sender, final String[] args) {
        if (!EqualityUtils.intValueInRange(args.length, 2, 3)){
            sender.sendMessage("Syntax: /wormhole irismaterial [stargate] <material>");
            sender.sendMessage("Valid materials are: STONE, DIAMOND_BLOCK, GLASS, IRON_BLOCK, BEDROCK, and LAPIS_BLOCK");
            return true;
        }

        if (StargateManager.isStargate(args[1])) {
            final Stargate stargate = StargateManager.getStargate(args[1]);
            if (stargate.isGateCustom()) {
                if (args.length == 3) {
                    Material m = null;
                    try {
                        m = Material.valueOf(args[2].trim().toUpperCase());
                    } catch (final Exception e) {
                        WXTLogger.prettyLog(Level.FINE, false, "Caught Exception on iris material" + e.getMessage());
                    }

                    if ((m != null) && ((m == Material.DIAMOND_BLOCK) || (m == Material.GLASS) || (m == Material.IRON_BLOCK) || (m == Material.BEDROCK) || (m == Material.STONE) || (m == Material.LAPIS_BLOCK))) {
                        stargate.setGateCustomIrisMaterial(m);
                        sender.sendMessage(args[1] + " iris material set to: " + stargate.getGateCustomIrisMaterial());

                        StargateDBManager.stargateToSQL(stargate);
                    } else {
                        sender.sendMessage("Invalid Iris Material: " + args[2]);
                        sender.sendMessage("Valid materials are: STONE, DIAMOND_BLOCK, GLASS, IRON_BLOCK, BEDROCK, and LAPIS_BLOCK");
                    }
                } else {
                    sender.sendMessage(args[1] + " iris material is currently: " + stargate.getGateCustomIrisMaterial());
                    sender.sendMessage("Valid materials are: STONE, DIAMOND_BLOCK, GLASS, IRON_BLOCK, BEDROCK, and LAPIS_BLOCK");
                }
            } else {
                sender.sendMessage("Stargate is not in custom mode. Set it with the '/wormhole custom' command");
            }
        } else {
            sender.sendMessage(ERR_INVALID_TARGET);
            sender.sendMessage("Syntax: /wormhole irismaterial [stargate] <material>");
            sender.sendMessage("Valid materials are: STONE, DIAMOND_BLOCK, GLASS, IRON_BLOCK, BEDROCK, and LAPIS_BLOCK");
        }

        return true;
    }

    private static boolean doLightMaterial(final CommandSender sender, final String[] args) {
        final String VALID_MATERIALS = "Valid materials are: GLOWSTONE, GLOWING_REDSTONE_ORE";

        if(!EqualityUtils.intValueInRange(args.length, 2, 3)) {
            sender.sendMessage("Syntax: /wormhole lightmaterial [stargate] <material>");
            sender.sendMessage(VALID_MATERIALS);
            return true;
        }

        if (StargateManager.isStargate(args[1])) {
            final Stargate stargate = StargateManager.getStargate(args[1]);
            if (stargate.isGateCustom()) {
                if (args.length == 3) {
                    Material m = null;
                    try {
                        m = Material.valueOf(args[2].trim().toUpperCase());
                    } catch (final Exception e) {
                        WXTLogger.prettyLog(Level.FINE, false, "Caught Exception on light material" + e.getMessage());
                    }

                    if ((m != null) && ((m == Material.GLOWSTONE) || (m == Material.REDSTONE_ORE))) {
                        stargate.setGateCustomLightMaterial(m);
                        sender.sendMessage(args[1] + " light material set to: " + stargate.getGateCustomLightMaterial());
                    } else {
                        sender.sendMessage("Invalid Light Material: " + args[2]);
                        sender.sendMessage(VALID_MATERIALS);
                    }
                } else {
                    sender.sendMessage(args[1] + " light material is currently: " + stargate.getGateCustomLightMaterial());
                    sender.sendMessage("Valid materials are: GLOWSTONE, GLOWING_REDSTONE_ORE");
                }
            } else {
                sender.sendMessage("Stargate is not in custom mode. Set it with the '/wormhole custom' command");
            }
        } else {
            sender.sendMessage(ERR_INVALID_TARGET);
            sender.sendMessage("Syntax: /wormhole lightmaterial [stargate] <material>");
            sender.sendMessage("Valid materials are: GLOWSTONE, GLOWING_REDSTONE_ORE");
        }

        return true;
    }

    /**
     * Do owner.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doOwner(final CommandSender sender, final String[] args) {
        if (!EqualityUtils.intValueInRange(args.length, 2, 3)) {
            sender.sendMessage("Arguments are missing. Please check your syntax.");
            sender.sendMessage("Syntax: /wormhole owner <stargate> [new owner]");
            return true;
        }

        String gateName = args[1];
        String owner = args[2];

        final Stargate s = StargateManager.getStargate(gateName);
        if (s != null) {
            if (args.length == 3) {
                String newOwner = args[3];
                s.setGateOwner(newOwner);
                s.setupGateSign(true);

                StargateDBManager.stargateToSQL(s);
                sender.sendMessage("Gate: " + s.getGateName() + " Now owned by: " + s.getGateOwner());
            } else if (args.length == 2) {
                sender.sendMessage("Gate: " + s.getGateName() + " Owned by: " + s.getGateOwner());
            }
        } else {
            sender.sendMessage(String.format("The gate you are trying to modify doesn't exist: %s", gateName));
        }

        return true;
    }

    /**
     * Do perms.
     * 
     * @param sender the sender
     * @param args the args
     */
    private static void doPerms(final CommandSender sender, final String[] args) {
        if (!(CommandUtilities.playerCheck(sender)))
            return;

        final Player p = (Player) sender;
        //PermissionsManager.handlePermissionRequest(p, args);
    }

    /**
     * Do Portal Material.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static void doPortalMaterial(final CommandSender sender, final String[] args) {
        final String VALID_MATERIALS = "Valid materials are: WATER, LAVA, AIR, NETHER_PORTAL";

        if (EqualityUtils.intValueInRange(args.length, 2, 3)) {
            sender.sendMessage("Syntax: /wormhole portalmaterial <stargate> [material]");
            sender.sendMessage(VALID_MATERIALS);
            return;
        }

        String gateName = args[1];
        String gateMaterial = args[2];
        if (!(StargateManager.isStargate(gateName))) {
            sender.sendMessage(ERR_INVALID_TARGET);
        }

        final Stargate stargate = StargateManager.getStargate(args[1]);
        if (stargate.isGateCustom()) {
            if (args.length == 3) {
                Material m;
                try {
                    m = Material.valueOf(args[2].trim().toUpperCase());

                    if ((m == Material.LAVA) || (m == Material.WATER) || (m == Material.AIR) || (m == Material.NETHER_PORTAL)) {
                        stargate.setGateCustomPortalMaterial(m);
                        sender.sendMessage(gateName + " portal material set to: " + stargate.getGateCustomPortalMaterial());

                        StargateDBManager.stargateToSQL(stargate);
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid Portal Material: " + gateMaterial);
                    sender.sendMessage("Valid materials are: WATER, LAVA, AIR, NETHER_PORTAL");
                } catch (Exception e) {
                    WXTLogger.prettyLog(Level.FINE, false, "Caught Exception on portal material" + e.getMessage());
                    sender.sendMessage("An exception has occurred. See server console for details.");
                }
            } else {
                sender.sendMessage(gateName + " portal material is currently: " + stargate.getGateCustomPortalMaterial());
                sender.sendMessage("Valid materials are: WATER, LAVA, AIR, NETHER_PORTAL");
            }
        } else {
            sender.sendMessage("Stargate is not in custom mode. Set it with the '/wormhole custom' command");
        }
    }

    /**
     * Do redstone.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doRedstone(final CommandSender sender, final String[] args) {
        if (!EqualityUtils.intValueInRange(args.length, 2, 3)) {
            sender.sendMessage(ERR_INVALID_TARGET);
            sender.sendMessage("Syntax: /wormhole redstone <stargate> [boolean]");
            sender.sendMessage("Valid boolean options are: true and false");
            return true;
        }

        if (StargateManager.isStargate(args[1])) {
            final Stargate stargate = StargateManager.getStargate(args[1]);
            if (args.length == 3) {
                if (CommandUtilities.isBoolean(args[2])) {
                    stargate.setGateRedstonePowered(Boolean.valueOf(args[2].trim().toLowerCase()));
                    if (stargate.isGateRedstonePowered()) {
                        stargate.setupRedstone(true);
                    } else {
                        stargate.setupRedstone(false);
                    }
                    sender.sendMessage(args[1] + " is redstone powered: " + stargate.isGateRedstonePowered());
                } else {
                    sender.sendMessage("Invalid boolean option: " + args[2]);
                    sender.sendMessage("Syntax: /wormhole redstone <stargate> [boolean]");
                    sender.sendMessage("Valid boolean options are: true and false");
                }
            } else {
                sender.sendMessage(args[1] + " is redstone powered: " + stargate.isGateRedstonePowered());
                sender.sendMessage("Valid boolean options are: true and false");
            }

            return true;
        }

        sender.sendMessage("Syntax: /wormhole redstone <stargate> [boolean]");
        sender.sendMessage("Valid boolean options are: true and false");
        return true;
    }

    /**
     * Do regenerate.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doRegenerate(final CommandSender sender, final String[] args) {
        if (args.length >= 2) {
            final Stargate s = StargateManager.getStargate(args[1]);
            if (s != null) {

                if ((s.getGateShape() != null) && StargateHelper.isStargateShape(s.getGateShape().getShapeNameKey())) {
                    //TODO: regenerate and upgrade stargates from 2d shape to 3d shape here.
                    // Handle the breaking out of shapes into multiple names for things like sign dial 
                    // by checking all the shape names for occurances of the shapeName then test from the longest
                    // shapeName to the shortest.
                }

                s.toggleDialLeverState(true);
                if ((s.getGateIrisDeactivationCode() != null) && (s.getGateIrisDeactivationCode().length() > 0)) {
                    s.setupIrisLever(true);
                }
                if (s.isGateRedstonePowered()) {
                    s.setupRedstone(true);
                }
                s.setupGateSign(true);
                if (s.isGateSignPowered()) {
                    s.resetTeleportSign();
                }
                sender.sendMessage("Regenerating Gate: " + s.getGateName());
            } else {
                sender.sendMessage(Messages.Error.GATE_NAME_INVALID + "\"" + args[1] + "\"");
            }

            return true;
        }

        sender.sendMessage("Syntax: /wormhole regenerate <stargate> [boolean]");
        sender.sendMessage(Messages.Error.GATE_UNSPECIFIED.toString());
        return true;
    }


    /**
     * Do restrict.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    /* TODO: Integrate Restriction Groups into new config format.
    private static boolean doRestrict(final CommandSender sender, final String[] args) {
        if ((args.length == 2) && CommandUtilities.isBoolean(args[1])) {
            ConfigManager.setBuildRestrictionEnabled(Boolean.valueOf(args[1].toLowerCase()));
            sender.sendMessage("Wormhole build count restrictions set to: " + args[1].toLowerCase());
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage("Syntax: /wormhole restrict <group, true, false> [count]");
            sender.sendMessage("Valid groups are 'one', 'two', and 'three'.");
            sender.sendMessage("Valid restriction values are between 1 and 200.");
            sender.sendMessage("Wormhole build count restriction enabled: " + ConfigManager.isBuildRestrictionEnabled());
            return true;
        }

        if ((args.length == 2) && (isValidGroupName(args[1]))) {
            sender.sendMessage("Syntax: /wormhole restrict <group, true, false> [count]");
            sender.sendMessage("Current restriction count is: " + doRestrictionGroup(args[1], false, 0));
            sender.sendMessage("Valid restriction values are between 1 and 200.");
            return true;
        }

        if (args.length == 3) {
            try {
                final int gateCount = Integer.parseInt(args[2]);
                if ((gateCount >= 1) && (gateCount <= 200)) {
                    doCooldownGroup(args[1], true, gateCount);
                    sender.sendMessage("Wormhole build restriction count: " + args[2]);
                } else {
                    sender.sendMessage("Build restriction count: " + args[2]);
                    sender.sendMessage("Valid restriction values are between 1 and 200.");
                }
            } catch (final NumberFormatException e) {
                sender.sendMessage("Invalid restriction count: " + args[2]);
                sender.sendMessage("Valid restriction values are between 1 and 200.");
            }
        }

        return true;
    }*/

    /**
     * Do restriction group.
     * 
     * @param groupName the group name
     * @param set the set
     * @param gateCount the gate count
     * @return the int
     */
    /* TODO: Integrate Restriction Groups into new config format.
    private static int doRestrictionGroup(final String groupName, final boolean set, final int gateCount) {
        int group = 0;
        int oldValue = 0;

        if (groupName.equalsIgnoreCase("one")) {
            group = 1;
        } else if (groupName.equalsIgnoreCase("two")) {
            group = 2;
        } else if (groupName.equalsIgnoreCase("three")) {
            group = 3;
        }

        switch (group) {
            case 1:
                if (set) {
                    oldValue = ConfigManager.getBuildRestrictionGroupOne();
                    ConfigManager.setBuildRestrictionGroupOne(gateCount);
                }
                return set
                        ? oldValue
                        : ConfigManager.getBuildRestrictionGroupOne();
            case 2:
                if (set) {
                    oldValue = ConfigManager.getBuildRestrictionGroupTwo();
                    ConfigManager.setBuildRestrictionGroupTwo(gateCount);
                }
                return set
                        ? oldValue
                        : ConfigManager.getBuildRestrictionGroupTwo();
            case 3:
                if (set) {
                    oldValue = ConfigManager.getBuildRestrictionGroupThree();
                    ConfigManager.setBuildRestrictionGroupThree(gateCount);
                }
                return set
                        ? oldValue
                        : ConfigManager.getBuildRestrictionGroupThree();
            default:
                return -1;
        }
    }
*/
    /**
     * Do shutdown timeout.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doShutdownTimeout(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Syntax: /wormhole shutdown_timeout [timeout]");
            sender.sendMessage("Current shutdown_timeout is: " + ConfigLoader.getConfig().timeouts().postdial());
            sender.sendMessage("Valid timeout is between 0 and 60 seconds.");
            return true;
        }

        try {
            final int timeout = Integer.parseInt(args[1]);
            if ((timeout > -1) && (timeout <= 60)) {
                ConfigLoader.getConfig().timeouts().setPostdial(timeout);
                sender.sendMessage("shutdown_timeout set to: " + ConfigLoader.getConfig().timeouts().postdial());
            } else {
                sender.sendMessage("Invalid shutdown_timeout: " + args[1]);
                sender.sendMessage("Valid timeout is between 0 and 60 seconds.");
                return true;
            }
        } catch (final NumberFormatException e) {
            sender.sendMessage("Invalid shutdown_timeout: " + args[1]);
            sender.sendMessage("Valid timeout is between 0 and 60 seconds.");
            return true;
        }

        return true;
    }

    /**
     * Do simple permissions.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    /* TODO: Permissions are a mess with the change over to a the new format.
    private static boolean doSimplePermissions(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Syntax: /wormhole simple [true, false]");
            sender.sendMessage("Simple Permissions: " + ConfigManager.getSimplePermissions());
            sender.sendMessage("Valid options: true/yes, false/no");
            return true;
        }

        Player player = null;
        boolean simple;
        if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("yes")) {
            simple = true;
        } else if (args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("no")) {
            simple = false;
        } else {
            sender.sendMessage("Invalid Setting: " + args[1]);
            sender.sendMessage("Valid options: true/yes, false/no");
            return true;
        }

        if ((WormholeXTreme.getPermissionManager() != null) && CommandUtilities.playerCheck(sender)) {
            player = (Player) sender;
            if (simple && !WormholeXTreme.getPermissionManager().has(player, "wormhole.simple.config")) {
                sender.sendMessage("You currently do not have the 'wormhole.simple.config' permission.");
                sender.sendMessage("Please make sure you have this permission before running this command again.");
                return true;
            } else if (!simple && !WormholeXTreme.getPermissionManager().has(player, "wormhole.config")) {
                sender.sendMessage("You currently do not have the 'wormhole.config' permission.");
                sender.sendMessage("Please make sure you have this permission before running this command again.");
                return true;
            }
        }

        ConfigManager.setSimplePermissions(simple);
        sender.sendMessage("Simple Permissions set to: " + ConfigManager.getSimplePermissions());

        if (player != null) {
            WXTLogger.prettyLog(Level.INFO, false, "Simple Permissions set to: \"" + simple + "\" by: \"" + player.getName() + "\"");
        }

        return true;
    }
    */
    /**
     * Do woosh depth.
     * 
     * @param sender the sender
     * @param args the args
     * @return true, if successful
     */
    private static boolean doWooshDepth(final CommandSender sender, final String[] args) {
        if(!EqualityUtils.intValueInRange(args.length, 2, 3)) {
            sender.sendMessage("Syntax: /wormhole wooshdepth <stargate> [depth]");
            sender.sendMessage("Valid depth: 0 - 5");
            return true;
        }

        if (StargateManager.isStargate(args[1])) {
            final Stargate stargate = StargateManager.getStargate(args[1]);
            if (stargate.isGateCustom()) {
                if (args.length == 3) {
                    try {
                        final int wooshDepth = Integer.parseInt(args[2].trim());
                        if ((wooshDepth >= 0) && (wooshDepth <= 5)) {
                            stargate.setGateCustomWooshDepth(wooshDepth);
                            stargate.setGateCustomWooshDepthSquared(wooshDepth * wooshDepth);
                            sender.sendMessage(args[1] + " woosh depth set to: " + stargate.getGateCustomWooshDepth());
                        } else {
                            sender.sendMessage("Invalid woosh depth: " + args[2]);
                            sender.sendMessage("Valid depth: 0 - 5");
                        }
                    } catch (final NumberFormatException e) {
                        sender.sendMessage("Invalid woosh depth: " + args[2]);
                        sender.sendMessage("Valid depth: 0 - 5");
                    }
                } else {
                    sender.sendMessage(args[1] + " woosh depth is currently: " + stargate.getGateCustomWooshDepth());
                    sender.sendMessage("Valid depth: 0 - 5");
                }
            } else {
                sender.sendMessage("Stargate is not in custom mode. Set it with the '/wormhole custom' command");
            }
        } else {
            sender.sendMessage(Messages.Error.TARGET_INVALID.toString());
            sender.sendMessage("Command: /wormhole wooshdepth <stargate> [depth]");
            sender.sendMessage("Valid depth: 0 - 5");
        }

        return true;
    }

    /**
     * Checks if is valid group name.
     * 
     * @param groupName the group name
     * @return true, if is valid group name
     */
    private static boolean isValidGroupName(final String groupName) {
        return groupName.equalsIgnoreCase("one") || groupName.equalsIgnoreCase("two") || groupName.equalsIgnoreCase("three");
    }

    /**
     * Sets the gate custom all.
     * 
     * @param stargate the stargate
     * @param customEnabled the custom enabled
     */
    private static void setGateCustomAll(final Stargate stargate, final boolean customEnabled) {
        if (stargate.getGateShape() != null) {
            if (customEnabled) {
                stargate.setGateCustom(true);
                if (stargate.getGateCustomIrisMaterial() == null) {
                    stargate.setGateCustomIrisMaterial(stargate.getGateShape().getShapeIrisMaterial());
                }
                if (stargate.getGateCustomLightMaterial() == null) {
                    stargate.setGateCustomLightMaterial(stargate.getGateShape().getShapeLightMaterial());
                }
                if (stargate.getGateCustomPortalMaterial() == null) {
                    stargate.setGateCustomPortalMaterial(stargate.getGateShape().getShapePortalMaterial());
                }
                if (stargate.getGateCustomStructureMaterial() == null) {
                    stargate.setGateCustomStructureMaterial(stargate.getGateShape().getShapeStructureMaterial());
                }
                if (stargate.getGateCustomLightTicks() == -1) {
                    stargate.setGateCustomLightTicks(stargate.getGateShape().getShapeLightTicks());
                }
                if (stargate.getGateCustomWooshTicks() == -1) {
                    stargate.setGateCustomWooshTicks(stargate.getGateShape().getShapeWooshTicks());
                }
                if (stargate.getGateCustomWooshDepth() == -1) {
                    stargate.setGateCustomWooshDepth(stargate.getGateShape().getShapeWooshDepth());
                }
                if (stargate.getGateCustomWooshDepthSquared() == -1) {
                    stargate.setGateCustomWooshDepthSquared(stargate.getGateShape().getShapeWooshDepthSquared());
                }
            } else {
                stargate.setGateCustom(false);
            }

            StargateDBManager.stargateToSQL(stargate);
            return;
        }

        WXTLogger.prettyLog(Level.FINE, false, stargate.getGateName() + " has no valid shape file. Unable to enable custom.");
    }

    /**
     * Set Logging level
     * 
     * @param sender
     * @param args
     * @return 
     */
    public static boolean doLogging(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Logging is currently set to '" + ConfigLoader.getConfig().plugin().logLevel() + "'.");
            return true;
        }

        if (args.length >= 2) {
            String logLevel = args[1];

            if (logLevel != null && !"".equals(logLevel)) {
                List<String> allowedArgs = new ArrayList<>(Arrays.asList("SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST"));
                if (allowedArgs.indexOf(logLevel.toUpperCase()) != -1) {
                    ConfigLoader.getConfig().plugin().setLogLevel(logLevel.toUpperCase());
                    WXTLogger.setLogLevel(Level.parse(args[1]));
                }
            }

            sender.sendMessage("Logging set to '" + ConfigLoader.getConfig().plugin().logLevel() + "'. See server.log for detailed log output.");
        }

        return true;
    }
    
    /**
     * Toggle SHOW_GATE_WELCOME_MESSAGE
     * 
     * @param sender
     * @param args
     * @return 
     */
    public static boolean toggleShowGWM(CommandSender sender, String[] args, boolean getValue) {
        if (args.length < 1) {
            return true;
        }

        if (sender instanceof Player) {
            if (!getValue) {
                ConfigLoader.getConfig().welcome().setEnabled(!ConfigLoader.getConfig().welcome().isEnabled());
            }

            sender.sendMessage("GATE_WELCOME_MESSAGE '" + (ConfigLoader.getConfig().welcome().isEnabled() ? "\u00A72enabled" : "\u00A74disabled") + Messages.MessageColor + "'.");
        }

        return true;
    }    
    
    /**
     * Toggle transportation method
     * 
     * @param sender
     * @param args
     * @return true EVENT, false TELEPORT
     */
    public static boolean toggleTransportMethod(CommandSender sender, String[] args, boolean getValue) {
        if (args.length < 1 ) {
            return true;
        }

        if (sender instanceof Player) {
            if (!getValue) {
                ConfigLoader.getConfig().plugin().setTransportType(ConfigLoader.getConfig().plugin().transportType().equals("event") ? "tp" : "event" );
            }

            sender.sendMessage(String.format("Transportation method %s %s.", ((getValue) ? "is" : "changed to"), (ConfigLoader.getConfig().plugin().transportType().equals("event") ? "EVENT" : "TELEPORT") ));
        }

        return true;
    }

    /**
     * Toggle transportation method
     * 
     * @param sender
     * @param args
     * @return true EVENT, false TELEPORT
     *//* TODO: Figure out how Wormhole Kickback will be defined in the new config.
    public static boolean setWormholeKickbackBlockCount(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 2) {
                int configVal = Integer.parseInt(args[1]);
                if (configVal >= 0) {
                    player.sendMessage("Wormhole kickback block count changed from '" + ConfigManager.getWormholeKickbackBlockCount() + "' to '" + configVal + "'");
                    ConfigManager.setWormholeKickbackBlockCount(configVal);
                } else {
                    player.sendMessage("Wormhole kickback block count has to be a number. " + args[1].getClass().getName() + " found.");
                }
            } else {
                player.sendMessage("Wormhole kickback block count: '" + ConfigManager.getWormholeKickbackBlockCount() + "'");
            }
        }

        return true;
    }*/

    public static boolean doShowPermissions(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Syntax: /wormhole permissions [provider]");
            sender.sendMessage("Valid providers: pex, bukkit");
            return true;
        }

//        sender.sendMessage("Selected Permission-Provider: " + WormholeXTreme.getPermissionManager().getBackend().getProviderName());
        return true;
    }

    /**
     * Rotates Gate facing [legacy feature)
     *
     * This can mix up your teleport location if used in bukkit > 1.4.x
     * @param sender
     * @param args
     * @return
     */
    /* TODO: Might remove this. I'm gonna guess most people will have done everything by now.
    public static boolean doFixGates(CommandSender sender, String[] args) {
        boolean force = false;
        for (String arg: args) {
            if (arg.equalsIgnoreCase("-f"))
                force = true;
        }

        if (!(force)) {
            String bukkitVersion = Bukkit.getVersion();
            sender.sendMessage(String.format("%sYour Server-Version is: %s. This is a legacy feature that will rotate all gate facings! If you know what you are doing type add -f to the end of the command",
                    ConfigManager.MessageStrings.normalHeader, bukkitVersion));

            return true;
        }

        if (!sender.isOp()) {
            sender.sendMessage(ConfigManager.MessageStrings.errorHeader + "You need to be Op to execute this command!");
            return true;
        }

        final ArrayList<Stargate> gates = StargateManager.getAllGates();
        if (args.length >= 2) {
            Stargate gate = StargateManager.getStargate(args[1]);
            if (gate != null) {
                sender.sendMessage(String.format("%sSet GateFace of '%s' from '%s' to '%s'",
                        ConfigManager.MessageStrings.normalHeader.toString(),
                        args[1],
                        gate.getGateFacing().name(),
                        WorldUtils.getPerpendicularLeftDirection(gate.getGateFacing())));

                gate.setGateFacing(WorldUtils.getPerpendicularLeftDirection(gate.getGateFacing()));
                StargateDBManager.stargateToSQL(gate);
            } else {
                sender.sendMessage("Gate '" + args[0] + "' not found in database");
            }
        } else {
            // fix all gates
            for (final Stargate gate : gates) {
                WXTLogger.prettyLog(Level.INFO,false,"Fixing saved gate '" + gate.getGateName() + "', Current GateFace: " + gate.getGateFacing().name());
                if (gate.isGateActive() || gate.isGateLightsActive()) {
                    gate.shutdownStargate(false);
                }
                BlockFace currentFacing = gate.getGateFacing();
                BlockFace targetFacing = WorldUtils.getPerpendicularLeftDirection(currentFacing);

                WXTLogger.prettyLog(Level.INFO, false, "Set facing from '" + currentFacing.name() + "' to '" + targetFacing.name() +"'");
                gate.setGateFacing(targetFacing);

                StargateDBManager.stargateToSQL(gate);
                WXTLogger.prettyLog(Level.INFO, false, "Saving gate: '" + gate.getGateName() + "', GateFace: '" + gate.getGateFacing().name() + "'");
            }

            sender.sendMessage("All existing Stargate facings are now fully rotated.");
        }

        return true;
    }*/

    public static boolean doShowInfo(CommandSender sender, String[] args) {
        final ArrayList<Stargate> gates = StargateManager.getAllGates();
        if (args.length < 2) {
            for (final Stargate gate : gates) {
                if (gate.isGateActive() || gate.isGateLightsActive()) {
                    gate.shutdownStargate(false);
                }
                WXTLogger.prettyLog(Level.INFO, false, "GateFace for '" + gate.getGateName() + "' is set to '" + gate.getGateFacing().name() + "'");
            }

            sender.sendMessage("Check your console log");
            return true;
        }

        Stargate gate = StargateManager.getStargate(args[1]);
        if (gate != null) {
            sender.sendMessage("GateFace for '" + args[1] + "' is set to '" + gate.getGateFacing() + "'");
        } else {
            sender.sendMessage("Gate '" + args[0] + "' not found in database");
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
   //     if (!CommandUtilities.playerCheck(sender) || WXPermissions.checkPermission((Player) sender, PermissionType.CONFIG)) {
            final String[] a = CommandUtilities.commandEscaper(args);
            if ((a.length > 4) || (a.length == 0)) {
                return false;
            }
            if (a[0].equalsIgnoreCase("owner")) {
                return doOwner(sender, a);
            } else if (a[0].equalsIgnoreCase("perm") || a[0].equalsIgnoreCase("perms")) {
                doPerms(sender, a);
            } /*else if (a[0].equalsIgnoreCase("portalmaterial")) {
                return doPortalMaterial(sender, a);
            } */else if (a[0].equalsIgnoreCase("irismaterial")) {
                return doIrisMaterial(sender, a);
            } else if (a[0].equalsIgnoreCase("timeout") || a[0].equalsIgnoreCase("shutdown_timeout")) {
                return doShutdownTimeout(sender, a);
            } else if (a[0].equalsIgnoreCase("activate_timeout")) {
                return doActivateTimeout(sender, a);
            } /*else if (a[0].equalsIgnoreCase("simple")) {
                return doSimplePermissions(sender, a);
            }*/ else if (a[0].equalsIgnoreCase("regenerate") || a[0].equalsIgnoreCase("regen")) {
                return doRegenerate(sender, a);
            } else if (a[0].equalsIgnoreCase("redstone")) {
                return doRedstone(sender, a);
            } else if (a[0].equalsIgnoreCase("custom")) {
                return doCustom(sender, a);
            } else if (a[0].equalsIgnoreCase("lightmaterial")) {
                return doLightMaterial(sender, a);
            } else if (a[0].equalsIgnoreCase("wooshdepth")) {
                return doWooshDepth(sender, a);
            } /*else if (a[0].equalsIgnoreCase("cooldown")) {
                return doCooldown(sender, a);
            } /*else if (a[0].equalsIgnoreCase("restrict")) {
                return doRestrict(sender, a);
            }*/ else if (a[0].equalsIgnoreCase("debug")) {
                return doLogging(sender, a);
            } else if (a[0].equalsIgnoreCase("toggle_gwm")) {
                return toggleShowGWM(sender, a, false);
            } else if (a[0].equalsIgnoreCase("toggle_transport")) {
                return toggleTransportMethod(sender, a, false);
            } else if (a[0].equalsIgnoreCase("show_gwm")) {
                return toggleShowGWM(sender, a, true);
            } else if (a[0].equalsIgnoreCase("show_transport")) {
                return toggleTransportMethod(sender, a, true);
            } /*else if (a[0].equalsIgnoreCase("kickback_count")) {
                return setWormholeKickbackBlockCount(sender, a);                
            } */else if (a[0].equalsIgnoreCase("permissions")) {
                return doShowPermissions(sender, a);
            } /*else if ((a[0].equalsIgnoreCase("legacyfixgate")) || (a[0].equalsIgnoreCase("legacyfixgates"))) {
                return doFixGates(sender, a);
            } */else if (a[0].equalsIgnoreCase("gateinfo")) {
                return doShowInfo(sender, a);
            } else {
                sender.sendMessage( "Given command is invalid: " + a[0]);
            }
        /*} else {
            sender.sendMessage("You do not have permission to complete this operation.");
        }*/

        return true;
    }
}
