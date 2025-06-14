package com.discordconnection.plugin.listeners;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.discordconnection.plugin.Main;
public class JoinQuitListener implements Listener {

    private final Main plugin;

    public JoinQuitListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        sendEvent(event.getPlayer(), "join");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        sendEvent(event.getPlayer(), "quit");
    }

    private void sendEvent(Player player, String action) {
        if (!plugin.isApiEnabled()) return;

        String serverId = plugin.getConfig().getString("server_uuid");
        if (serverId == null || serverId.isEmpty()) return;

        try {
            URL url = new URL("http://localhost:4000/sendMessage");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String message = action.equals("join") ?
                "🟢 Se ha unido al servidor" :
                "🔴 Ha salido del servidor";

            String json = String.format(
                "{\"server_uuid\":\"%s\",\"username\":\"%s\",\"message\":\"%s\", \"type\": \"status\" }",
                serverId, player.getName(), message
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
