package org.realgotqkura.itemCatalog.utilities.data;

import org.realgotqkura.itemCatalog.ItemCatalog;

public class DataUtils {


    public void loadCustomData(ItemCatalog plugin){
        checkIntIfNull(plugin, "customModelData.flameSword", 20);
        checkIntIfNull(plugin, "customModelData.chronoClock", 21);
        checkIntIfNull(plugin, "customModelData.soulScythe", 22);
        checkIntIfNull(plugin, "customModelData.itemMagnet", 23);
        checkIntIfNull(plugin, "customModelData.echoBow", 24);
        checkIntIfNull(plugin, "customModelData.doubleEdgedSword", 25);
        checkIntIfNull(plugin, "customModelData.giantHammer", 26);
        checkIntIfNull(plugin, "customModelData.sandstormDagger", 27);
        checkIntIfNull(plugin, "customModelData.tempestTrident", 28);
        checkIntIfNull(plugin, "customModelData.huntersNet", 29);
        checkIntIfNull(plugin, "customModelData.volcanicPickaxe", 30);
        checkIntIfNull(plugin, "customModelData.chameleonCloak", 31);
        checkIntIfNull(plugin, "customModelData.druidStaff", 32);
        checkIntIfNull(plugin, "customModelData.metalDetector", 33);
        checkIntIfNull(plugin, "customModelData.echoBlade", 34);
        checkIntIfNull(plugin, "customModelData.glacialBow", 35);
        checkIntIfNull(plugin, "customModelData.frostHammer", 36);
        checkIntIfNull(plugin, "customModelData.chainLightningStaff", 37);
        checkIntIfNull(plugin, "customModelData.siphonGauntlet", 38);
        checkIntIfNull(plugin, "customModelData.windlashWhip", 39);
        checkIntIfNull(plugin, "customModelData.cursedCoin", 40);
        checkIntIfNull(plugin, "customModelData.spiritLantern", 41);
        checkIntIfNull(plugin, "customModelData.phoenixFeather", 42);
        checkIntIfNull(plugin, "customModelData.soulDagger", 43);
        checkIntIfNull(plugin, "customModelData.tempestBoots", 44);
        checkIntIfNull(plugin, "customModelData.abyssalHarpoon", 45);
        checkIntIfNull(plugin, "customModelData.luminaraSpear", 46);
        checkIntIfNull(plugin, "customModelData.obsidianMace", 47);
        checkIntIfNull(plugin, "customModelData.spectralKatana", 48);
        checkIntIfNull(plugin, "customModelData.soulReaperAxe", 49);
        checkIntIfNull(plugin, "customModelData.voidBow", 50);
        checkIntIfNull(plugin, "customModelData.homingCrossbow", 51);
        checkIntIfNull(plugin, "customModelData.etherealHelm", 52);
        checkIntIfNull(plugin, "customModelData.titanChestplate", 53);
        checkIntIfNull(plugin, "customModelData.phantomLeggings", 54);
        checkIntIfNull(plugin, "customModelData.stormCloak", 55);
    }

    private static void checkIntIfNull(ItemCatalog plugin, String configLine, int defaultValue) {
        if (plugin.getConfig().get(configLine) == null) {
            plugin.getConfig().set(configLine, defaultValue);
            plugin.saveConfig();
        }
    }
}
