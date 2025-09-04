package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VolcanicPickaxe implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public VolcanicPickaxe(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.volcanicPickaxe");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#d66329", "Volcanic Pickaxe"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8QOL pickaxe"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lBLOCK BREAK &6Smelt"));
        lore.add(RandomUtils.color("&7Returns the smelted version of an item"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "VolcanicPickaxe");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void breakBl(BlockBreakEvent event){
        Player player = event.getPlayer();

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "VolcanicPickaxe"))
            return;


        Collection<ItemStack> drops = event.getBlock().getDrops(player.getInventory().getItemInMainHand(), player);
        event.setDropItems(false);
        for(ItemStack stack : drops){
            ItemStack result = RandomUtils.getSmeltingResult(stack);
            if(result == null) {
                player.getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
            }else{
                result.setAmount(stack.getAmount());

                player.getWorld().dropItemNaturally(event.getBlock().getLocation(), result);
            }



        }
    }
}
