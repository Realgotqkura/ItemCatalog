package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DruidStaff implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public DruidStaff(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.druidStaff");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#71d629", "Druid Staff"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Agricultural marvel"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK BLOCK &6GROWW"));
        lore.add(RandomUtils.color("&7Applies bone meal to crops in a 3x3 area"));
        lore.add(RandomUtils.color("&7Damages for &c1 &7damage every time."));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "DruidStaff");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(event.getHand() != EquipmentSlot.HAND)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "DruidStaff"))
            return;

        if(event.getClickedBlock() == null)
            return;

        List<Location> locs = new ArrayList<>();
        Location initialLoc = event.getClickedBlock().getLocation();
        locs.add(initialLoc);
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                locs.add(initialLoc.clone().add(i,0,j));
            }
        }

        for(Location loc : locs){
            if(loc.getBlock().getType() == Material.AIR)
                return;

            Block block = loc.getBlock();
            if (block.getBlockData() instanceof Ageable crop) {
                int maxAge = crop.getMaximumAge();
                int currentAge = crop.getAge();

                // Simulate bone meal effect
                int increase = 1 + new Random().nextInt(2);
                int newAge = Math.min(currentAge + increase, maxAge);
                crop.setAge(newAge);
                block.setBlockData(crop);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, block.getLocation().add(0.5, 0.5, 0.5), 10);
                player.damage(1);
            }
        }
    }
}
