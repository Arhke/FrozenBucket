package net.waterraid.GenBucket;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    private static File folder, configPath;
    private static Economy economy;
    public static Main instance;
    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        getCommand("genbucket").setExecutor(new Command());
        folder = this.getDataFolder();
        instance = this;
        configPath = new File(folder + File.separator + "config.yml");
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getLogger().severe("This Plugin Depends on Vault, Please Install Vault");
            Bukkit.getPluginManager().disablePlugin(this);
        }else{
            loadEconomy();
        }

        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(configPath);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
        loadConfig(config);
    }
    @Override
    public void onDisable() {

    }
    public static File getFolder() {
        return folder;
    }
    public static File getConfigPath() {
        return configPath;
    }
    private void loadConfig(YamlConfiguration config) {
        GUI.itemStacks = new ItemStack[27];
        for (String key: config.getKeys(false)) {
            if (key.equals("max")){
                continue;
            }
            int slot = config.getInt(key+".slot");
            if (slot > 16 || slot < 10 ){
                Bukkit.getLogger().warning("Ommiting \""+key + "\" slot config error");
                continue;
            }
            String type = config.getString(key+".type");
            type = type == null?"":type.toLowerCase();
            if (!type.equals("horizontal") && !type.equals("vertical")) {
                Bukkit.getLogger().warning("Ommiting \""+key + "\" type config error");
                continue;
            }
            String block = config.getString(key+".block");
            int cost = config.getInt(key+".cost");
            if (cost < 0){
                Bukkit.getLogger().warning("Ommiting \""+key + "\" cost config error");
                continue;
            }
            GUI.itemStacks[slot] = GUI.craftItem(config.getString(key+".name"), config.getString(key+".lore"),type,block, cost);
        }
        BucketScheduling.maxChanges = Math.max(config.getInt("max"),1);
    }
    private boolean loadEconomy() {
        final RegisteredServiceProvider<Economy> rsp = (RegisteredServiceProvider<Economy>) this.getServer().getServicesManager().getRegistration((Class) Economy.class);
        if (rsp == null) {
            return false;
        }
        this.economy = (Economy) rsp.getProvider();
        return this.economy != null;

    }
    public static Economy getEconomy() {
        return economy;
    }
}
