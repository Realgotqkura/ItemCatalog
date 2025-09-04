package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class TempestTrident implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public TempestTrident(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.tempestTrident");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.TRIDENT);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#21b5ab", "Tempest Trident"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8If zeus and poseidon had a baby"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON THROW &6Lighning"));
        lore.add(RandomUtils.color("&7Every time the trident gets thrown it unleashes"));
        lore.add(RandomUtils.color("&7a lightning, regardless of weather"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "TempestTrident");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void hit(ProjectileLaunchEvent event){
        if(!(event.getEntity() instanceof Trident))
            return;


        Trident trident = (Trident) event.getEntity();

        if(!RandomUtils.passedItemChecks(trident.getItem(), "TempestTrident"))
            return;

        trident.setMetadata("tempestTrident", new FixedMetadataValue(plugin, 1));


    }

    @EventHandler
    public void trident(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident))
            return;

        if(event.getHitBlock() == null)
            return;

        if(!event.getEntity().hasMetadata("tempestTrident"))
            return;

        event.getEntity().getWorld().strikeLightning(event.getHitBlock().getLocation());
    }
}
