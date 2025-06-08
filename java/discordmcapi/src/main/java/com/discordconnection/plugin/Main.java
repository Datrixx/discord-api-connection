package com.discordconnection.plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.discordconnection.plugin.commands.RegisterApiCommand;
import com.discordconnection.plugin.commands.TogglePluginCommand;
import com.discordconnection.plugin.listeners.ChatListener;

public class Main extends JavaPlugin {

    private FileConfiguration apiConfig;
    private File apiConfigFile;

    private boolean apiEnabled = true;

    @Override
    public void onEnable() {
        getLogger().info("¡Plugin activado!");
        saveDefaultApiConfig();
        loadApiConfig();

        getLogger().info("Cargando configuración de api-rest.yml...");
        if (apiConfig == null) {
            getLogger().severe("No se pudo cargar la configuración de api-rest.yml. Asegúrate de que el archivo existe.");
            return;
        }
        this.apiEnabled = apiConfig.getBoolean("enabled", true);

        if (getCommand("toggleplugin") != null) {
            getCommand("toggleplugin").setExecutor(new TogglePluginCommand(this));
        } else {
            getLogger().warning("Comando 'toggleplugin' no encontrado en plugin.yml");
        }

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getLogger().log(Level.INFO, "Plugin activado correctamente. Comunicaci\u00f3n con la API est\u00e1 {0}", apiEnabled ? "activada." : "desactivada.");
        getCommand("registerapi").setExecutor(new RegisterApiCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin desactivado.");
        apiConfig.set("enabled", apiEnabled);
        try {
            apiConfig.save(apiConfigFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error al guardar api-rest.yml: {0}", e.getMessage());
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
