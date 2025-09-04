package org.realgotqkura.itemCatalog;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.commands.DebugCommand;
import org.realgotqkura.itemCatalog.commands.GuiCommand;
import org.realgotqkura.itemCatalog.commands.tabcompleters.DebugCmdTabComp;
import org.realgotqkura.itemCatalog.guis.AllItemsGui;
import org.realgotqkura.itemCatalog.guis.CombatItemsGUI;
import org.realgotqkura.itemCatalog.guis.InitialGUI;
import org.realgotqkura.itemCatalog.guis.MiscItemsGUI;
import org.realgotqkura.itemCatalog.items.ItemsManager;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;
import org.realgotqkura.itemCatalog.utilities.data.DataUtils;
import org.realgotqkura.itemCatalog.utilities.data.PlayerDataConfig;
import org.realgotqkura.itemCatalog.utilities.logger.GLogger;
import org.realgotqkura.itemCatalog.utilities.logger.GLoggerSeverity;

public final class ItemCatalog extends JavaPlugin {

    public static ItemCatalog instance;
    private PlayerDataConfig playerData;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        playerData = new PlayerDataConfig(this);

        RandomUtils.loadNskContainer(this);


        new BukkitRunnable(){

            @Override
            public void run() {
                ItemsManager.loadItems();
                ItemsManager.loadItemEvents(instance);
                ItemsManager.loadRunnables(instance);
                cancel();
            }
        }.runTaskLater(this, 1);

        DataUtils utils = new DataUtils();
        utils.loadCustomData(this);

        registerGuiEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        GLogger.log(GLoggerSeverity.INFO, "Disabling ItemCatalog");
    }

    public PlayerDataConfig getPlayerData() {
        return playerData;
    }

    public void registerGuiEvents(){
        this.getServer().getPluginManager().registerEvents(new InitialGUI(this), this);
        this.getServer().getPluginManager().registerEvents(new CombatItemsGUI(this), this);
        this.getServer().getPluginManager().registerEvents(new MiscItemsGUI(this), this);
        this.getServer().getPluginManager().registerEvents(new AllItemsGui(this), this);
    }

    public void registerCommands(){
        this.getCommand("itemcatalog").setExecutor(new GuiCommand(this));
        this.getCommand("itcat_debug").setExecutor(new DebugCommand(this));
        this.getCommand("itcat_debug").setTabCompleter(new DebugCmdTabComp());
    }
}
