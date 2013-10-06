package de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands;

import de.luricos.bukkit.WormholeXTreme.Wormhole.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Map;

/**
 * @author lycano
 */
public class GateCommands extends WormholeCommand {

    @Command(name = "wormhole",
            syntax = "gate info [GateName]",
            description = "Print gate info for selected Wormhole",
            permission = "")
    public void printGateInfo(Plugin plugin, CommandSender sender, Map<String, String> args) {
        sender.sendMessage("print gate info command executed");
    }
}
