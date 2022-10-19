package me.itsadrift.adriftbuild;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BuildCommand implements CommandExecutor, TabCompleter {

    public AdriftBuild main;
    public BuildCommand(AdriftBuild main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player) || !sender.hasPermission("adriftbuild.use")) {
            sender.sendMessage(colour(main.getConfig().getString("messages.noPermission")));
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("def") || args[0].equalsIgnoreCase("default")) {
                String status = "";

                if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on")) {
                    main.getConfig().set("data." + player.getUniqueId().toString() + ".default", true);
                    status = main.getConfig().getString("messages.enabled");
                } else {
                    main.getConfig().set("data." + player.getUniqueId().toString() + ".default", false);
                    status = main.getConfig().getString("messages.disabled");
                }
                player.sendMessage(colour(main.getConfig().getString("messages.setDefault").replace("{DEFAULT_STATUS}", status)));
                main.saveConfig();
            }
            return false;
        }

        if (main.buildMap.containsKey(player.getUniqueId())) {
            main.buildMap.replace(player.getUniqueId(), !main.buildMap.getOrDefault(player.getUniqueId(), true));
        } else {
            main.buildMap.put(player.getUniqueId(), false);
        }


        String status = main.buildMap.get(player.getUniqueId()) ? main.getConfig().getString("messages.enabled") : main.getConfig().getString("messages.disabled");

        player.sendMessage(colour(main.getConfig().getString("messages.toggled").replace("{STATUS}", status)));

        return false;
    }

    private String colour(String s) {
        return HexUtils.colour(s);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("default");
        }
        if (args.length == 2) {
            return Arrays.asList("on", "off");
        }
        return Arrays.asList("");
    }
}
