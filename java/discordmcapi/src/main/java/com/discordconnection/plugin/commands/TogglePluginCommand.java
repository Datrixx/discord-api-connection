package com.discordconnection.plugin.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.discordconnection.plugin.Main;

public class TogglePluginCommand implements CommandExecutor {
    private final Main plugin;

    public TogglePluginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("discordmcapi.toggle")) {
            sender.sendMessage("§cNo tienes permiso para usar este comando.");
            return true;
        }

        boolean current = plugin.isApiEnabled();
        plugin.setApiEnabled(!current);

        String estado = plugin.isApiEnabled() ? "§aActivada" : "§cDesactivada";
        sender.sendMessage("§eComunicación con la API ahora está: " + estado);
        return true;
    }


}
