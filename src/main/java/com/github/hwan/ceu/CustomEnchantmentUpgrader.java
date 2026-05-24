package com.github.hwan.ceu;

import com.github.hwan.ceu.commands.CEUCommand;
import com.github.hwan.ceu.commands.CEUTabCompleter;
import com.github.hwan.ceu.utils.ConfigManager;
import java.io.File;
import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.black_ixx.playerpoints.PlayerPoints;

public final class CustomEnchantmentUpgrader extends JavaPlugin {
    public static CustomEnchantmentUpgrader pluginInstance;
    public static CustomEnchantmentUpgrader instance;
    public static XConomyAPI xConomyAPI;
    public static PlayerPoints playerPointsAPI;
    public static Configuration pluginConfig;
    public static String activeEconomyType;
    
    private static ConfigManager configManager;

    public void onEnable() {
        pluginInstance = this;
        instance = this;
        
        // 初始化配置管理器
        configManager = ConfigManager.getInstance(this);
        
        // 加载配置文件
        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            saveDefaultConfig();
        }
        saveDefaultConfig(); // 确保资源中的配置文件被复制
        
        // 重新加载配置以确保版本号正确
        reloadConfig();
        pluginConfig = getConfig();
        configManager.loadConfigs();
        
        // 初始化 XConomy API
        if (Bukkit.getPluginManager().getPlugin("XConomy") != null) {
            xConomyAPI = new XConomyAPI();
            activeEconomyType = "xconomy";
        }
        
        // 初始化 PlayerPoints API
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
            playerPointsAPI = (PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints");
            if (activeEconomyType == null) {
                activeEconomyType = "playerpoints";
            }
        }

        if (configManager.getBoolean("debug", false)) {
            getLogger().info("Debug mode enabled.");
            getLogger().info("Upgrade cost: " + configManager.getUpgradeCost());
            getLogger().info("Fix cost: " + configManager.getFixCost());
            getLogger().info("Max enchantment level: " + configManager.getMaxLevel());
        }
        
        // 注册命令
        CEUCommand ceuCommand = new CEUCommand(configManager);
        PluginCommand cmd = getCommand("ceu");
        if (cmd != null) {
            cmd.setExecutor(ceuCommand);
            cmd.setTabCompleter(new CEUTabCompleter(ceuCommand.getSubCommands()));
        }
        
        getLogger().info("========================================");
        getLogger().info("");
        getLogger().info("  ██████╗███████╗██╗   ██╗");
        getLogger().info(" ██╔════╝██╔════╝██║   ██║");
        getLogger().info(" ██║     █████╗  ██║   ██║");
        getLogger().info(" ██║     ██╔══╝  ██║   ██║");
        getLogger().info(" ╚██████╗███████╗╚██████╔╝");
        getLogger().info("  ╚═════╝╚══════╝ ╚═════╝");
        getLogger().info("");
        getLogger().info("Custom Enchantment Upgrader - " + getDescription().getVersion());

        boolean xConomyEnabled = xConomyAPI != null;
        boolean playerPointsEnabled = playerPointsAPI != null;

        if (xConomyEnabled) {
            getLogger().info("Xconomy: \u001B[32m已启用\u001B[0m");
        }
        if (playerPointsEnabled) {
            getLogger().info("PlayerPoints: \u001B[32m已启用\u001B[0m");
        }
        if (!xConomyEnabled && !playerPointsEnabled) {
            getLogger().info("\u001B[31m未发现受支持的经济系统\u001B[0m");
        }

        getLogger().info("");
        getLogger().info("========================================");

    }

    public void onDisable() {
        getLogger().info("UnLoaded CustomEnchantmentUpgrader Plugin.");
    }
    
    public static CustomEnchantmentUpgrader getInstance() {
        return instance;
    }
    
    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
