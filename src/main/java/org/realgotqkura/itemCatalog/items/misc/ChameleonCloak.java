package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChameleonCloak implements Listener {

    static HashMap<Player, Location> pastLocations = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public ChameleonCloak(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.chameleonCloak");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(Color.GREEN);
        meta.setDisplayName(RandomUtils.color("&x&1&C&F&6&5&7C&x&3&1&E&E&5&6h&x&4&5&E&5&5&4a&x&5&A&D&D&5&3m&x&6&F&D&5&5&2e&x&8&3&C&C&5&1l&x&9&8&C&4&4&Fe&x&A&C&B&B&4&Eo&x&C&1&B&3&4&Dn &x&E&A&A&2&4&AC&x&F&F&9&A&4&9l&x&F&F&9&A&4&9o&x&F&F&9&A&4&9a&x&F&F&9&A&4&9k"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Very Sneaky"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lSTANDING STILL &6Steaaddyyy"));
        lore.add(RandomUtils.color("&7Makes you vanish (totally invisible) if you"));
        lore.add(RandomUtils.color("&7don't move for &a3&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "ChameleonCloak");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void move(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(!RandomUtils.passedItemChecks(player.getEquipment().getChestplate(), "ChameleonCloak"))
            return;

        if(!pastLocations.containsKey(player)) {
            pastLocations.put(player, player.getLocation());
            return;
        }

        pastLocations.replace(player, player.getLocation());
    }

    public static void runnable(ItemCatalog plugin){
        new BukkitRunnable(){

            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){

                    if(!RandomUtils.passedItemChecks(player.getEquipment().getChestplate(), "ChameleonCloak"))
                        continue;

                    if(!pastLocations.containsKey(player))
                        continue;

                    player.setInvisible(pastLocations.get(player).equals(player.getLocation()));
                }
            }
        }.runTaskTimer(plugin, 20, 3*20);
    }
}
