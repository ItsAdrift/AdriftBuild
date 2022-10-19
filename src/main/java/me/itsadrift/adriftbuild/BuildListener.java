package me.itsadrift.adriftbuild;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class BuildListener implements Listener {

    private AdriftBuild main;
    public BuildListener(AdriftBuild main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (handle(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (handle(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onTrample(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            if (handle(e.getPlayer()))
                e.setCancelled(true);
        }
    }

    private boolean handle(Player player) {
        boolean block = main.buildMap.getOrDefault(player.getUniqueId(), false);
        if (block)
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(HexUtils.colour(main.getConfig().getString("messages.notAllowed"))));
        return block;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("adriftbuild.use")) {
            if (main.getConfig().contains("data." + e.getPlayer().getUniqueId().toString() + ".default")) {
                boolean status = main.getConfig().getBoolean("data." + e.getPlayer().getUniqueId().toString() + ".default");
                main.buildMap.put(e.getPlayer().getUniqueId(), status);
                if (status) {
                    sendDelayedMsg(e.getPlayer(), HexUtils.colour(main.getConfig().getString("messages.toggled").replace("{STATUS}", main.getConfig().getString("messages.enabled"))), 40);
                }
            } else {
                main.buildMap.put(e.getPlayer().getUniqueId(), true);
                sendDelayedMsg(e.getPlayer(), HexUtils.colour(main.getConfig().getString("messages.toggled").replace("{STATUS}", main.getConfig().getString("messages.enabled"))), 40);
            }
        }
    }

    public void sendDelayedMsg(Player player, String msg, long delay) {
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run() {
                player.sendMessage(HexUtils.colour(msg));
            }
        }, delay);
    }

}
