package com.github.hwan.ceu.commands;

import com.github.hwan.ceu.CustomEnchantmentUpgrader;
import com.github.hwan.ceu.utils.ConfigManager;
import me.yic.xconomy.api.XConomyAPI;
import me.yic.xconomy.data.syncdata.PlayerData;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FixCommand extends SubCommand {
    private ConfigManager configManager;

    public FixCommand(ConfigManager configManager) {
        super("fix", null, true);
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!checkPlayer(sender, configManager)) return;

        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInHand();

        if (itemInHand == null) {
            player.sendMessage(getPrefix() + configManager.getMessage("fix.no_item"));
            return;
        }

        if (itemInHand.getType().getMaxDurability() <= 0 || itemInHand.getDurability() <= 0) {
            player.sendMessage(getPrefix() + configManager.getMessage("fix.no_durability"));
            return;
        }

        String economyType = configManager.getEconomyType();
        if (economyType == null) {
            player.sendMessage(getPrefix() + configManager.getMessage("economy.no_system"));
            return;
        }

        int fixCost = configManager.getFixCost();

        if ("xconomy".equals(economyType)) {
            handleXConomy(player, itemInHand, fixCost);
        } else if ("playerpoints".equals(economyType)) {
            handlePlayerPoints(player, itemInHand, fixCost);
        }
    }

    private void handleXConomy(Player player, ItemStack itemInHand, int fixCost) {
        XConomyAPI api = CustomEnchantmentUpgrader.xConomyAPI;
        if (api == null) {
            player.sendMessage(getPrefix() + configManager.getMessage("economy.system_error"));
            return;
        }

        PlayerData playerData = api.getPlayerData(player.getName());
        if (playerData.getBalance().longValue() >= fixCost) {
            api.changePlayerBalance(playerData.getUniqueId(), playerData.getName(),
                new BigDecimal(fixCost), false);
            itemInHand.setDurability((short) 0);
            player.sendMessage(getPrefix() + configManager.getMessage("fix.success"));
        } else {
            player.sendMessage(getPrefix() + configManager.getMessage("fix.insufficient_balance"));
        }
    }

    private void handlePlayerPoints(Player player, ItemStack itemInHand, int fixCost) {
        PlayerPoints api = CustomEnchantmentUpgrader.playerPointsAPI;
        if (api == null || api.getAPI() == null) {
            player.sendMessage(getPrefix() + configManager.getMessage("economy.system_error"));
            return;
        }

        int currentPoints = api.getAPI().look(player.getUniqueId());
        if (currentPoints < fixCost) {
            player.sendMessage(getPrefix() + configManager.getMessage("fix.insufficient_balance"));
            return;
        }

        boolean taken = api.getAPI().take(player.getUniqueId(), fixCost);
        if (!taken) {
            taken = api.getAPI().takeAsync(player.getUniqueId(), fixCost).join();
        }
        if (!taken) {
            player.sendMessage(getPrefix() + configManager.getMessage("economy.system_error"));
            return;
        }

        itemInHand.setDurability((short) 0);
        player.sendMessage(getPrefix() + configManager.getMessage("fix.success"));
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    private String getPrefix() {
        return configManager.getPrefix();
    }
}
