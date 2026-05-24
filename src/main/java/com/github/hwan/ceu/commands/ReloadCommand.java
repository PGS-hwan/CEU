package com.github.hwan.ceu.commands;

import com.github.hwan.ceu.CustomEnchantmentUpgrader;
import com.github.hwan.ceu.utils.ConfigManager;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {
    private ConfigManager configManager;
    private CustomEnchantmentUpgrader pluginInstance;

    public ReloadCommand(ConfigManager configManager) {
        super("reload", "ceu.admin", false);
        this.configManager = configManager;
        this.pluginInstance = CustomEnchantmentUpgrader.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!checkPermission(sender, configManager)) return;

        try {
            pluginInstance.reloadConfig();
            configManager.loadConfigs();
            sender.sendMessage(configManager.getPrefix() + configManager.getMessage("reload.success"));
        } catch (Exception e) {
            sender.sendMessage(configManager.getPrefix() + configManager.getMessage("reload.failed"));
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
