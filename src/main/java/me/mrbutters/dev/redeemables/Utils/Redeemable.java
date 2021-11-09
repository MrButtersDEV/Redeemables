package me.mrbutters.dev.redeemables.Utils;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.List;

public class Redeemable implements Serializable {

    private List<String> commands;

    public Redeemable(List<String> commands) {
        this.commands = commands;
    }

    public void run(Player p) {
        Utils.runCmds(p, commands);
    }

}
