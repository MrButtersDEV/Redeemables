package me.mrbutters.dev.redeemables.commands;

import me.mrbutters.dev.redeemables.Redeemables;
import me.mrbutters.dev.redeemables.Utils.RedeemCode;
import me.mrbutters.dev.redeemables.Utils.Redeemable;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Redeem implements CommandExecutor {

    private static final Redeemables PLUGIN = Redeemables.getPlugin(Redeemables.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length!=1) {
                player.sendMessage(ChatColor.RED + "Invalid Args: /redeem <code>");
                return false;
            }

            String code = args[0];
            if (Redeemables.redeemCodes.containsKey(code)) {
                if (!PLUGIN.getOldCodesConf().contains(player.getUniqueId().toString())) {
                    PLUGIN.getOldCodesConf().createSection(player.getUniqueId().toString());
                }
                ConfigurationSection cs = PLUGIN.getOldCodesConf().getConfigurationSection(player.getUniqueId().toString());
                RedeemCode redeemCode = Redeemables.redeemCodes.get(code);

                if (redeemCode.isOnePerPlayer()) {
                    if (cs.contains(code) && cs.getInt(code)>=1) {
                        player.sendMessage(ChatColor.DARK_AQUA + "You have already redeemed that code!");
                        return false;
                    }
                }

                redeemCode.activate(player);

                // Set times used
                if (cs.contains(code)) {
                    int amtUsed = cs.getInt(code);
                    amtUsed++; // Increase times used by 1 if it has been used before
                    cs.set(code, amtUsed);
                } else {
                    cs.set(code, 1); // Sets to 1 if this code has not been used before.
                }

                // Save Times used
                try {
                    PLUGIN.getOldCodesConf().save(Redeemables.usedCodesFile);
                    PLUGIN.reloadPlayerData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                player.sendMessage(ChatColor.YELLOW + "'" + code + "' was an invalid code!");
            }

        } else {
            sender.sendMessage("Only players may redeem a code");
        }

        return false;
    }

}
