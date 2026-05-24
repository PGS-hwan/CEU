package com.github.hwan.ceu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CEUTabCompleter implements TabCompleter {
    private Map<String, SubCommand> subCommands;

    public CEUTabCompleter(Map<String, SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // 第一个参数，显示可用的子命令
            String prefix = args[0].toLowerCase();
            for (String subCommandKey : subCommands.keySet()) {
                if (subCommandKey.startsWith(prefix)) {
                    SubCommand subCommand = subCommands.get(subCommandKey);
                    // 检查权限
                    if (subCommand.hasPermission(sender)) {
                        completions.add(subCommandKey);
                    }
                }
            }
        } else if (args.length > 1) {
            // 后续参数，委托给子命令
            String subCommandName = args[0].toLowerCase();
            SubCommand subCommand = subCommands.get(subCommandName);
            
            if (subCommand != null && subCommand.hasPermission(sender)) {
                List<String> subCompletions = subCommand.getTabComplete(sender, args);
                if (subCompletions != null) {
                    completions.addAll(subCompletions);
                }
            }
        }

        return completions;
    }
}
