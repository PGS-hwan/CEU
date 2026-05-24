package com.github.hwan.ceu.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.List;

public class ConfigManager {
    private static ConfigManager instance;
    private Configuration mainConfig;
    private Configuration languageConfig;
    private JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mainConfig = plugin.getConfig();
        loadLanguageConfig();
    }

    public static ConfigManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
        return instance;
    }

    private void loadLanguageConfig() {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        this.languageConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public void loadConfigs() {
        // 重新加载主配置
        this.mainConfig = plugin.getConfig();
        // 重新加载语言配置
        loadLanguageConfig();
    }

    // 获取经济系统类型
    public String getEconomyType() {
        return mainConfig.getString("economy.type", "xconomy");
    }

    // 获取升级花费
    public int getUpgradeCost() {
        String economyType = getEconomyType();
        if ("xconomy".equals(economyType)) {
            return mainConfig.getInt("economy.xconomy.upgrade-cost", 20000);
        } else if ("playerpoints".equals(economyType)) {
            return mainConfig.getInt("economy.playerpoints.upgrade-cost", 200);
        }
        return 5000;
    }

    // 获取修复花费
    public int getFixCost() {
        String economyType = getEconomyType();
        if ("xconomy".equals(economyType)) {
            return mainConfig.getInt("economy.xconomy.fix-cost", 50000);
        } else if ("playerpoints".equals(economyType)) {
            return mainConfig.getInt("economy.playerpoints.fix-cost", 500);
        }
        return 50000;
    }

    // 获取升级成功率，百分比形式（0-100）
    public double getSuccessChance() {
        return mainConfig.getDouble("upgrade.success-chance", 50.0);
    }

    // 获取最大附魔等级
    public int getMaxLevel() {
        return mainConfig.getInt("upgrade.max-level", 10);
    }

    // 获取指定附魔的最大等级（可覆盖默认最大等级）
    public int getMaxLevel(Enchantment enchantment) {
        if (enchantment == null) {
            return getMaxLevel();
        }
        String path = "upgrade.single-max-level." + enchantment.getName().toUpperCase();
        int overrideLevel = mainConfig.getInt(path, -1);
        if (overrideLevel > 0) {
            return overrideLevel;
        }
        return getMaxLevel();
    }

    // 检查附魔是否被阻止升级
    public boolean isEnchantmentBlocked(Enchantment enchantment) {
        List<String> blocked = mainConfig.getStringList("upgrade.blocked-enchantments");
        if (blocked == null || blocked.isEmpty()) {
            return false;
        }
        String name = enchantment.getName().toUpperCase();
        for (String blockedName : blocked) {
            if (blockedName != null && blockedName.toUpperCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // 获取消息前缀
    private String translateColors(String text) {
        if (text == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getPrefix() {
        return translateColors(languageConfig.getString("prefix", "&6CEU &8» "));
    }

    // 获取语言消息
    public String getMessage(String path) {
        String message = languageConfig.getString(path, "");
        return translateColors(message);
    }

    // 获取消息并替换变量
    public String getMessage(String path, String... replacements) {
        String message = languageConfig.getString(path, "");
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        return translateColors(message);
    }

    // 获取配置项
    public Object get(String path) {
        return mainConfig.get(path);
    }

    // 获取整数配置
    public int getInt(String path, int defaultValue) {
        return mainConfig.getInt(path, defaultValue);
    }

    // 获取字符串配置
    public String getString(String path, String defaultValue) {
        return mainConfig.getString(path, defaultValue);
    }

    // 获取布尔配置
    public boolean getBoolean(String path, boolean defaultValue) {
        return mainConfig.getBoolean(path, defaultValue);
    }

    // 获取双精度浮点数配置
    public double getDouble(String path, double defaultValue) {
        return mainConfig.getDouble(path, defaultValue);
    }
}
