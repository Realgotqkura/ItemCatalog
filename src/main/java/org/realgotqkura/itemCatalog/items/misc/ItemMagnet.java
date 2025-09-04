package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemMagnet implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public ItemMagnet(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.itemMagnet");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.COMPASS);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&x&3&D&6&2&F&FI&x&3&D&6&2&F&Ft&x&3&D&6&2&F&Fe&x&3&D&6&2&F&Fm &x&8&B&3&B&9&9M&x&B&1&2&7&6&6a&x&D&8&1&4&3&3g&x&F&F&0&0&0&0n&x&F&F&0&0&0&0e&x&F&F&0&0&0&0t"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A regular old magnet"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lWHEN HELD &6Attract"));
        lore.add(RandomUtils.color("&7Attracts items drops"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "ItemMagnet");
        stack.setItemMeta(meta);
        return stack;
    }

    public static void runnable(ItemCatalog plugin){
        new BukkitRunnable(){

            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){

                    if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "ItemMagnet") && !RandomUtils.passedItemChecks(player.getInventory().getItemInOffHand(), "ItemMagnet"))
                        continue;

                    for(Entity entity : player.getNearbyEntities(10, 4, 10)){
                        if(entity instanceof Item item){
                            Vector dir = player.getLocation().toVector().subtract(item.getLocation().toVector());
                            dir = dir.normalize().multiply(0.3f);
                            item.setVelocity(dir);
                        }
                    }
                }
            }

        }.runTaskTimer(plugin, 20, 8);
    }
}
