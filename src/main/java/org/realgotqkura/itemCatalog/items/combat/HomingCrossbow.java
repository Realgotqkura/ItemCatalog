package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
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

public class HomingCrossbow implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public HomingCrossbow(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.homingCrossbow");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#00ffea", "Homing Crossbow"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Bolts that find their mark"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Homing Bolts"));
        lore.add(RandomUtils.color("&7Bolts fired will track down the nearest"));
        lore.add(RandomUtils.color("&7enemy within &a15&7 blocks."));
        lore.add("");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "HomingCrossbow"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event){
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        ItemStack held = player.getInventory().getItemInMainHand();
        if(!RandomUtils.passedItemChecks(held, "HomingCrossbow")) return;

        // Mark arrow
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

        // Start homing logic
        new BukkitRunnable(){
            @Override
            public void run() {
                if (arrow.isDead() || arrow.isOnGround()) {
                    cancel();
                    return;
                }

                LivingEntity target = getNearestTarget(arrow, 15, player);
                if (target == null) return;

                Vector direction = target.getLocation().add(0, 1, 0).toVector().subtract(arrow.getLocation().toVector()).normalize();
                Vector velocity = arrow.getVelocity().multiply(0.9).add(direction.multiply(2)).normalize().multiply(arrow.getVelocity().length());

                arrow.setVelocity(velocity);
                arrow.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 2, 0.05, 0.05, 0.05, 0);
            }
        }.runTaskTimer(plugin, 2, 2);
    }

    private LivingEntity getNearestTarget(Arrow arrow, double radius, Player owner){
        LivingEntity nearest = null;
        double closestDist = radius * radius;

        for (Entity e : arrow.getNearbyEntities(radius, radius, radius)){
            if (!(e instanceof LivingEntity target)) continue;
            if (target.equals(owner)) continue;

            double dist = target.getLocation().distanceSquared(arrow.getLocation());
            if (dist < closestDist){
                closestDist = dist;
                nearest = target;
            }
        }
        return nearest;
    }
}
