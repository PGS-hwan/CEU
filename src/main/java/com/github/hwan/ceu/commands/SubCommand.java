package com.github.hwan.ceu.commands;

import com.github.hwan.ceu.utils.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public abstract class SubCommand {
    protected String name;
    protected String permission;
    protected boolean playerOnly;

    public SubCommand(String name, String permission, boolean playerOnly) {
        this.name = name;
        this.permission = permission;
        this.playerOnly = playerOnly;
    }

    /**
     * 执行子命令
     */
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * 获取命令的 tab 补全列表
     */
    public abstract List<String> getTabComplete(CommandSender sender, String[] args);

    /**
     * 获取命令名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取所需权限
     */
    public String getPermission() {
        return permission;
    }

    /**
     * 检查权限
     */
    public boolean hasPermission(CommandSender sender) {
        if (permission == null) {
            return true;
        }
        return sender.hasPermission(permission);
    }

    /**
     * 检查是否仅玩家可用
     */
    public boolean isPlayerOnly() {
        return playerOnly;
    }

    /**
     * 检查是否是玩家
     */
    protected boolean checkPlayer(CommandSender sender, ConfigManager configManager) {
        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("error.console_only_error"));
            return false;
        }
        return true;
    }

    /**
     * 检查权限
     */
    protected boolean checkPermission(CommandSender sender, ConfigManager configManager) {
        if (!hasPermission(sender)) {
            sender.sendMessage(configManager.getMessage("permission.denied"));
            return false;
        }
        return true;
    }
}
