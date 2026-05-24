package com.github.hwan.ceu.commands;

import com.github.hwan.ceu.utils.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.HashMap;
import java.util.Map;

public class CEUCommand implements CommandExecutor {
    private Map<String, SubCommand> subCommands = new HashMap<>();
    private ConfigManager configManager;

    public CEUCommand(ConfigManager configManager) {
        this.configManager = configManager;
        registerSubCommands();
    }

    private void registerSubCommands() {
        // 注册所有子命令
        subCommands.put("fix", new FixCommand(configManager));
        subCommands.put("repair", new FixCommand(configManager)); // repair 别名
        subCommands.put("upgrade", new UpgradeCommand(configManager));
        subCommands.put("admin", new AdminCommand(configManager));
        subCommands.put("help", new HelpCommand(configManager));
        subCommands.put("reload", new ReloadCommand(configManager));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subCommandName = args[0].toLowerCase();

        // 获取子命令
        SubCommand subCommand = subCommands.get(subCommandName);
        if (subCommand == null) {
            sender.sendMessage(configManager.getPrefix() + configManager.getMessage("error.unknown_command"));
            return true;
        }

        // 检查权限
        if (!subCommand.hasPermission(sender)) {
            sender.sendMessage(configManager.getPrefix() + configManager.getMessage("permission.denied"));
            return true;
        }

        // 检查是否仅玩家可用
        if (subCommand.isPlayerOnly() && !(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage(configManager.getMessage("error.console_only_error"));
            return true;
        }

        // 执行子命令
        try {
            subCommand.execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(configManager.getPrefix() + configManager.getMessage("error.execute_failed"));
            e.printStackTrace();
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(configManager.getMessage("help.title"));
        sender.sendMessage(configManager.getMessage("help.fix"));
        sender.sendMessage(configManager.getMessage("help.repair"));
        sender.sendMessage(configManager.getMessage("help.upgrade"));
        if (sender.hasPermission("ceu.admin")) {
            sender.sendMessage(configManager.getMessage("help.admin_fix"));
            sender.sendMessage(configManager.getMessage("help.admin_repair"));
            sender.sendMessage(configManager.getMessage("help.admin_upgrade"));
            sender.sendMessage(configManager.getMessage("help.reload"));
        }
        sender.sendMessage(configManager.getMessage("help.footer"));
    }

    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }
}
