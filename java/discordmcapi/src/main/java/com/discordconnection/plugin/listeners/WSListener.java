package com.discordconnection.plugin.listeners;

import java.net.URI;
import java.net.URISyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import com.discordconnection.plugin.Main;

public class WSListener extends WebSocketClient {

    private final Main plugin;
    private final String serverUUID;

    public WSListener(URI serverUri, Main plugin, String serverUUID) throws URISyntaxException {
        super(serverUri);
        this.plugin = plugin;
        this.serverUUID = serverUUID;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        plugin.getLogger().info("Conectado al WebSocket del servidor Discord.");
        JSONObject register = new JSONObject();
        register.put("type", "register");
        register.put("server_uuid", serverUUID);
        send(register.toString());
    }

    @Override
    public void onMessage(String message) {
        JSONObject msg = new JSONObject(message);

        if (msg.has("type") && msg.getString("type").equals("chat")) {
            String username = msg.getString("username");
            String content = msg.getString("message");

            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.AQUA + "[Discord] " + ChatColor.RESET + username + ": " + content);
            });
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        plugin.getLogger().info("WebSocket desconectado: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        plugin.getLogger().severe("Error en WebSocket: " + ex.getMessage());
    }
}
