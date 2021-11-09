package me.mrbutters.dev.redeemables.menus;

import me.mrbutters.dev.redeemables.Redeemables;
import me.mrbutters.dev.redeemables.Utils.RedeemCode;
import me.mrbutters.dev.redeemables.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ManagerMenu {

    private Player player;
    private Inventory gui;

    public ManagerMenu(Player player) {
        this.player = player;
        this.gui = Bukkit.createInventory(player, 54, Utils.format("&cRedeemable Manager"));
    }

    public void open() {


        ItemStack activeCodesIcon = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta activeCodesMeta = activeCodesIcon.getItemMeta();

        activeCodesMeta.setDisplayName(Utils.format("&6Active Codes:"));
        activeCodesMeta.setLore(Arrays.asList("", Utils.format("&2▍ Next"), Utils.format("&7 Right-Click"), "", Utils.format("&c▍ Back"), Utils.format("&7 Left-Click")));
        activeCodesIcon.setItemMeta(activeCodesMeta);

        for (int x = 0; x<9; x++) {
            gui.setItem(x, activeCodesIcon);
        }

        for (String key : Redeemables.redeemCodes.keySet()) {
            ItemStack codeIcon = new ItemStack(Material.BOOK);
            ItemMeta meta = codeIcon.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(key);

            RedeemCode code = Redeemables.redeemCodes.get(key);

            if (code.getLimit()==-1) {
                codeIcon.setType(Material.ENCHANTED_BOOK);
            }

            codeIcon.setItemMeta(meta);
            gui.addItem(codeIcon);
        }

        ItemStack activeRedeemableIcon = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta activeRedeemableMeta = activeCodesIcon.getItemMeta();

        activeRedeemableMeta.setDisplayName(Utils.format("&aAvailable Redeemables:"));
        activeRedeemableMeta.setLore(Arrays.asList("", Utils.format("&2▍ Next"), Utils.format("&7 Right-Click"), "", Utils.format("&c▍ Back"), Utils.format("&7 Left-Click")));
        activeRedeemableIcon.setItemMeta(activeRedeemableMeta);

        for (int x = 36; x<45; x++) {
            gui.setItem(x, activeRedeemableIcon);
        }

        player.openInventory(gui);
    }

}
