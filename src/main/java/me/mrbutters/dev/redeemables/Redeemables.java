package me.mrbutters.dev.redeemables;

import me.mrbutters.dev.redeemables.Utils.RedeemCode;
import me.mrbutters.dev.redeemables.Utils.Redeemable;
import me.mrbutters.dev.redeemables.commands.CreateRedeemCode;
import me.mrbutters.dev.redeemables.commands.Redeem;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public final class Redeemables extends JavaPlugin {

    public static HashMap<String, RedeemCode> redeemCodes = new HashMap<>();
    public static HashMap<String, Redeemable> availableRedeemables = new HashMap<>();
    public static HashSet<String> previousCodes = new HashSet<String>();
    public static List<String> redeemOptions = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        createOldCodesFile();
        setupRedeemables();

        try {
            FileInputStream fis = new FileInputStream(getDataFolder()+"\\RedeemCodesData.redeemcodes");
            ObjectInputStream ois = new ObjectInputStream(fis);
            redeemCodes = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        // Commands
        getCommand("redeem").setExecutor(new Redeem());
        getCommand("redeemables").setExecutor(new CreateRedeemCode());

    }

    @Override
    public void onDisable() {

        try {
            FileOutputStream fos = new FileOutputStream(getDataFolder()+"\\RedeemCodesData.redeemcodes");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(redeemCodes);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in hashmap.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void setupRedeemables() {
        // Make folder if it does not exist
        File redeemDir = new File(getDataFolder(), "Redeemables");
        //File exampleFile = new File(redeemDir, "Redeemables/example.yml");
        if (!redeemDir.exists()){
            //theDir.mkdirs();
            //exampleFile.getParentFile().mkdirs();
            saveResource("Redeemables"+File.separator+"example.yml", false);
        }

        for (File file : redeemDir.listFiles()) {
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
            int version = conf.getInt("RedeemableVersion");
            switch (version) {
                case 1:
                case 0:
                default:
                    availableRedeemables.put(conf.getString("Redeemable.name"), new Redeemable(conf.getStringList("Redeemable.commands")));
                    redeemOptions.add(conf.getString("Redeemable.name"));
                    break;
            }
        }
    }

    //codes.yml
    public static File usedCodesFile;
    private FileConfiguration oldCodesConf;

    public FileConfiguration getOldCodesConf() {
        return this.oldCodesConf;
    }

    private void createOldCodesFile() {
        usedCodesFile = new File(getDataFolder(), "usedcodes.yml");
        if (!usedCodesFile.exists()) {
            usedCodesFile.getParentFile().mkdirs();
            saveResource("usedcodes.yml", false);
        }

        oldCodesConf = new YamlConfiguration();
        try {
            oldCodesConf.load(usedCodesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadPlayerData(){
        oldCodesConf = YamlConfiguration.loadConfiguration(usedCodesFile);
    }

}
