package com.discordconnection.plugin.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RegisterApiCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public RegisterApiCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Solo jugadores pueden ejecutar este comando.");
            return true;
        }

        String uuid = UUID.randomUUID().toString();
        plugin.getConfig().set("server_uuid", uuid);
        plugin.saveConfig();

        player.sendMessage("Tu ID de servidor es: " + uuid);
        registerWithApi(uuid, "NombreServidor");

        return true;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void registerWithApi(String uuid, String serverName) {

        try {
            URL url = new URL("http://localhost:3000/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format("{\"id\":\"%s\", \"serverName\": \"%s\"}", uuid, serverName);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            conn.getResponseCode(); 

        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "Error al registrar el servidor con la API: {0}", e.getMessage());
        }
    }
}

