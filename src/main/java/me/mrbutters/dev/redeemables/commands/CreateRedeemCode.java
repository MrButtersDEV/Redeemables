package me.mrbutters.dev.redeemables.commands;

import me.mrbutters.dev.redeemables.Redeemables;
import me.mrbutters.dev.redeemables.Utils.RedeemCode;
import me.mrbutters.dev.redeemables.Utils.Redeemable;
import me.mrbutters.dev.redeemables.Utils.TimeAfter;
import me.mrbutters.dev.redeemables.menus.ManagerMenu;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class CreateRedeemCode implements TabExecutor {

    private static final Redeemables PLUGIN = Redeemables.getPlugin(Redeemables.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //                  0         1     2    3   4   5    6   7    8     9
        // \redeemables generate (Example) week day hour min sec mili limit t/f
        if (sender.hasPermission("redeemables.create")) {
            if (args.length>=0 && args.length<=10) {
                if (args[0].equalsIgnoreCase("generate")) {
                        String name = args[1]; // Redeemable
                    if (Redeemables.availableRedeemables.containsKey(name)) {
                        Redeemable r = Redeemables.availableRedeemables.get(name);
                        TimeAfter ta = new TimeAfter(Long.parseLong(args[2]), Long.parseLong(args[3]), Long.parseLong(args[4]), Long.parseLong(args[5]), Long.parseLong(args[6]), Long.parseLong(args[7]));
                        RedeemCode code = new RedeemCode(r, ta, Integer.parseInt(args[8]), Boolean.parseBoolean(args[9]));

                        TextComponent message = new TextComponent(ChatColor.GREEN + "Created Code: '"+ChatColor.AQUA+code.getCode()+ChatColor.GREEN+"'");
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, code.getCode()));
                        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN+"Click to Copy")));
                        sender.spigot().sendMessage(message);
                    }
                } else if (args[0].equalsIgnoreCase("timeleft")) {

                    if (!Redeemables.redeemCodes.containsKey(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Invalid code!");
                        return false;
                    }

                    long time = System.currentTimeMillis();
                    long targetTime = Redeemables.redeemCodes.get(args[1]).getTimeExpire();
                    long timeLeft = targetTime-(time);
                    timeLeft = timeLeft/1000;

                    long daysLeft = timeLeft/86400;
                    long hoursLeft = (timeLeft%86400)/3600;
                    long minLeft = ((timeLeft%86400)%3600)/60;
                    long secLeft = ((timeLeft%86400)%3600)%60;

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTime left: &b" + daysLeft+"d&a, &b" + hoursLeft + "h&a, &b" + minLeft + "m&a, &b" + secLeft +"s"));

                } else if (args[0].equalsIgnoreCase("limit")) {

                    if (!Redeemables.redeemCodes.containsKey(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Invalid code!");
                        return false;
                    }

                    int uses = Redeemables.redeemCodes.get(args[1]).getUses();
                    int limit = Redeemables.redeemCodes.get(args[1]).getLimit();
                    boolean oneTimePer = Redeemables.redeemCodes.get(args[1]).isOnePerPlayer();

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUses: (&b"+uses+"&a/&b"+limit+"&a)"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOne Time Per User: &b"+oneTimePer));

                } else if (args[0].equalsIgnoreCase("manager")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        new ManagerMenu(player).open();
                    } else {
                        sender.sendMessage(ChatColor.RED + "You need to be a player to open this menu!");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid args!");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        int length = args.length;
        switch (length) {
            case 1:
                return Arrays.asList("generate", "timeleft", "limit", "manager");
            case 2:
                return Redeemables.redeemOptions;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return Arrays.asList("0", "1");
            case 9:
                return Arrays.asList("1", "10", "50", "100", "limit");
            case 10:
                return Arrays.asList("true", "false");
        }

        return null;
    }
}
