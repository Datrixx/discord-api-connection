package com.discordconnection.plugin.listeners;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.discordconnection.plugin.Main;

public class ChatListener implements Listener {
    private final Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!plugin.isApiEnabled()) return;

        String serverId = plugin.getConfig().getString("server_uuid");
        if (serverId == null) return;

        Player player = event.getPlayer();
        String message = event.getMessage();

        try {
            URL url = new URL("http://localhost:4000/sendMessage");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format(
                "{\"server_uuid\":\"%s\",\"username\":\"%s\",\"message\":\"%s\", \"type\":\"chat\"}",
                serverId, player.getName(), message.replace("\"", "\\\"")
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
