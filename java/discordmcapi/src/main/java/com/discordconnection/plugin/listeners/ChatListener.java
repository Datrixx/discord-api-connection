package com.discordconnection.plugin.listeners;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener implements Listener {
    private final JavaPlugin plugin;

    public ChatListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String serverId = plugin.getConfig().getString("server_uuid");
        if (serverId == null) return;

        Player player = event.getPlayer();
        String message = event.getMessage();

        try {
            URL url = new URL("http://localhost:3000/message");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format("{\"server_uuid\":\"%s\",\"username\":\"%s\",\"message\":\"%s\"}",
                    serverId, player.getName(), message.replace("\"", "\\\""));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            conn.getResponseCode(); // enviar

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
