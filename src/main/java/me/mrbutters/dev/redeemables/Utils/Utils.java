package me.mrbutters.dev.redeemables.Utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final ConsoleCommandSender CONSOLE = Bukkit.getServer().getConsoleSender();

    public static void runCmds(Player p, List<String> cmds) {
        for (String s : cmds) {
            //s = PlaceholderAPI.setPlaceholders(p, s);
            s = s.replace("{USER}", p.getName());
            if (s.startsWith("[console] ")) {
                s = s.replace("[console] ", "");
                Bukkit.dispatchCommand(CONSOLE, s);
            } else if (s.startsWith("[broadcast] ")) {
                s = s.replace("[broadcast] ", "");
                broadcast(s);
            } else if (s.startsWith("[player] ")) {
                s = s.replace("[player] ", "");
                p.performCommand(s);
            } else if (s.startsWith("[message] ")) {
                s = s.replace("[message] ", "");
                p.sendMessage(format(s));
            } else if (s.startsWith("[sound]")) {
                // [sound] SOUND_NAME:VOL:PITCH
                s = s.replace("[sound] ", "");
                String split[] = s.split(";");
                Location loc = p.getLocation();
                p.playSound(loc, Sound.valueOf(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
            }
        }
    }

    public static void broadcast(String msg) {
        msg = format(msg);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static String format(String msg) {
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18") || Bukkit.getVersion().contains("1.19")) {
            Matcher match = pattern.matcher(msg);
            while (match.find()) {
                String color = msg.substring(match.start(), match.end());
                msg = msg.replace(color, ChatColor.of(color.replace("&#","#")) + "");
                match = pattern.matcher(msg);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }


}
