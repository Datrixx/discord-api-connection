package com.discordconnection.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.discordconnection.plugin.commands.RegisterApiCommand;
import com.discordconnection.plugin.commands.TogglePluginCommand;
import com.discordconnection.plugin.listeners.ChatListener;
import com.discordconnection.plugin.listeners.JoinQuitListener;
import com.discordconnection.plugin.listeners.WSListener;

public class Main extends JavaPlugin {

    private FileConfiguration apiConfig;
    private File apiConfigFile;
    private String server_uuid;
    private WSListener wsListener;
    private boolean apiEnabled = true;

    @Override
    public void onEnable() {
        saveDefaultApiConfig();
        loadApiConfig();

        if (apiConfig == null) {
            getLogger().severe("No se pudo cargar la configuración de api-rest.yml. Asegúrate de que el archivo existe.");
            return;
        }

        apiEnabled = apiConfig.getBoolean("enabled", true);

        if (getCommand("toggleplugin") != null) {
            getCommand("toggleplugin").setExecutor(new TogglePluginCommand(this));
        } else {
            getLogger().warning("Comando 'toggleplugin' no encontrado en plugin.yml");
        }

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);

        if (getCommand("registerapi") != null) {
            getCommand("registerapi").setExecutor(new RegisterApiCommand(this));
        } else {
            getLogger().warning("Comando 'registerapi' no encontrado en plugin.yml");
        }

        server_uuid = getConfig().getString("server_uuid");
        getLogger().log(Level.INFO, "Plugin activado correctamente. Comunicación con la API está {0}", apiEnabled ? "activada." : "desactivada.");
        getLogger().log(Level.INFO, "UUID cargada desde config.yml: {0}", server_uuid);

        try {
            URI uri = new URI("ws://localhost:5001");
            wsListener = new WSListener(uri, this, server_uuid);
            wsListener.connect();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al conectar WebSocket: {0}", e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        apiConfig.set("enabled", apiEnabled);
        try {
            apiConfig.save(apiConfigFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error al guardar api-rest.yml: {0}", e.getMessage());
        }
        if (wsListener != null) {
            try {
                wsListener.close();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Error deteniendo WebSocket: {0}", e.getMessage());
            }
        }
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
        apiConfig.set("enabled", apiEnabled);
        try {
            apiConfig.save(apiConfigFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error al guardar api-rest.yml: {0}", e.getMessage());
        }
    }

    public void saveDefaultApiConfig() {
        apiConfigFile = new File(getDataFolder(), "api-rest.yml");
        if (!apiConfigFile.exists()) {
            saveResource("api-rest.yml", false);
        }
    }

    public void loadApiConfig() {
        apiConfigFile = new File(getDataFolder(), "api-rest.yml");
        apiConfig = YamlConfiguration.loadConfiguration(apiConfigFile);
    }

    public FileConfiguration getApiConfig() {
        return apiConfig;
    }
}
