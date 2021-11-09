package me.mrbutters.dev.redeemables.Utils;

import me.mrbutters.dev.redeemables.Redeemables;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Random;

public class RedeemCode implements Serializable {

    private String code;
    private long timeNow;
    private long timeExpire;
    private int limit;
    private int uses;
    boolean onePerPlayer;

    private Redeemable redeemable;

    public RedeemCode(Redeemable redeemable, TimeAfter timeAfter, int gLimit, boolean onePerPlayer) {
        this.onePerPlayer = onePerPlayer;
        this.redeemable = redeemable;
        this.limit = gLimit;
        this.uses = 0;
        this.code = genCode();
        this.timeNow = System.currentTimeMillis();
        this.timeExpire = System.currentTimeMillis()+timeAfter.getTimeAfter();

        Redeemables.previousCodes.add(code);
        Redeemables.redeemCodes.put(code, this);
    }

    public boolean isValid() {
        return timeExpire > System.currentTimeMillis() && (uses < limit || uses == -1);
    }

    public void activate(Player p) {
        if (isValid()) {
            redeemable.run(p);
            this.uses++;
        } else {
            p.sendMessage(ChatColor.YELLOW + "Invalid code: '" + code + "' has expired or reached its redeemable limit!");
        }
    }

    public boolean isOnePerPlayer() {
        return onePerPlayer;
    }

    public String getCode() {
        return code;
    }

    public long getTimeNow() {
        return timeNow;
    }

    public long getTimeExpire() {
        return timeExpire;
    }

    public int getLimit() {
        return limit;
    }

    public int getUses() {
        return uses;
    }

    public Redeemable getRedeemable() {
        return redeemable;
    }

    private static String genCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        if (Redeemables.previousCodes.contains(generatedString)) {
            generatedString = genCode();
        }

        return generatedString;
    }

}
