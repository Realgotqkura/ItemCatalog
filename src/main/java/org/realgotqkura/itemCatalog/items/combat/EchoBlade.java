package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class EchoBlade implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public EchoBlade(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.echoBlade");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#ff9cf3", "Echo Blade"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Main echo weapon"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON HIT &6Lag"));
        lore.add(RandomUtils.color("&7After being hit, the entity gets damaged"));
        lore.add(RandomUtils.color("&7again after &a0.5&7s dealing &c50&7% of"));
        lore.add(RandomUtils.color("&7the original damage."));
        lore.add("");
        lore.add(RandomUtils.color("&0&kEE&4&oOP&0&kEE"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "EchoBlade");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity))
            return;

        LivingEntity damager = (LivingEntity) event.getDamager();

        if (!RandomUtils.passedItemChecks(damager.getEquipment().getItemInMainHand(), "EchoBlade"))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity hitEntity = (LivingEntity) event.getEntity();

        new BukkitRunnable(){

            @Override
            public void run() {
                hitEntity.damage(event.getDamage()/2);
                cancel();
            }
        }.runTaskLater(plugin, 10);
    }
}
