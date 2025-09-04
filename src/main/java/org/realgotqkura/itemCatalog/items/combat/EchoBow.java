package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class EchoBow implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public EchoBow(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.echoBow");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.BOW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&x&E&9&7&3&B&3E&x&D&F&6&F&B&5c&x&D&4&6&C&B&6h&x&C&A&6&8&B&8o &x&B&5&6&0&B&BB&x&A&A&5&D&B&Co&x&A&0&5&9&B&Ew"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A Deadly weapon"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON ENTITY HIT &6Bullseye"));
        lore.add(RandomUtils.color("&7If the initial shot of the bow is a hit"));
        lore.add(RandomUtils.color("&a2 &7more arrows drop from above, hitting"));
        lore.add(RandomUtils.color("&7the target."));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "EchoBow");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void shoot(EntityShootBowEvent event){

        if(!RandomUtils.passedItemChecks(event.getBow(), "EchoBow"))
            return;

        event.getProjectile().setMetadata("echoBowKey", new FixedMetadataValue(plugin, 1));
    }

    @EventHandler
    public void hit(ProjectileHitEvent event) {
        if (!event.getEntity().hasMetadata("echoBowKey"))
            return;

        if (!(event.getHitEntity() instanceof LivingEntity))
            return;

        new BukkitRunnable(){

            @Override
            public void run() {
                LivingEntity hitEntity = (LivingEntity) event.getHitEntity();
                for (int i = 0; i < 2; i++) {
                    // Location above the target (5+ blocks higher, staggered by i)
                    Location spawn = hitEntity.getLocation().clone().add(0, 7 + i, 0);

                    // Direction from spawn to target
                    Vector velocity = hitEntity.getLocation().toVector()
                            .subtract(spawn.toVector())
                            .normalize()
                            .multiply(1.5); // speed multiplier

                    // Spawn the arrow properly aimed
                    Arrow extraArrow = hitEntity.getWorld().spawnArrow(spawn, velocity, 1.5f, 0f);
                    extraArrow.setShooter(event.getEntity().getShooter());
                    extraArrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED); // optional
                }
                cancel();
            }
        }.runTaskLater(plugin, 10);
    }

}
