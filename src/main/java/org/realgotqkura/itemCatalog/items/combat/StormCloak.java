package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StormCloak implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;
    private static final HashMap<UUID, Double> cooldowns = new HashMap<>();

    public StormCloak(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.stormCloak");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.ELYTRA);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#00ffff", "Storm Cloak"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Cloak charged with the fury of storms"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON DAMAGE &6Electric Surge"));
        lore.add(RandomUtils.color("&7Deals &c6 damage &7to all nearby enemies"));
        lore.add(RandomUtils.color("&7within &a5 blocks&7 and knocks them back."));
        lore.add(RandomUtils.color("&7Cooldown: &a12s"));
        lore.add("");
        lore.add(RandomUtils.color("&0&kXX&b&oSTORM&0&kXX"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "StormCloak"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack cloak = player.getInventory().getChestplate();
        if (!RandomUtils.passedItemChecks(cloak, "StormCloak")) return;

        int cooldown = 12;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns)) return;

        // AoE
        double radius = 5;
        for (LivingEntity e : player.getNearbyEntities(radius, radius, radius).stream()
                .filter(en -> en instanceof LivingEntity && !en.equals(player))
                .map(en -> (LivingEntity) en).toList()) {

            e.damage(6, player);
            Vector knockback = e.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(0.5).setY(0.4);
            e.setVelocity(knockback);

            e.getWorld().spawnParticle(Particle.CRIT, e.getLocation().add(0,1,0), 15, 0.5,0.5,0.5, 0.05);
        }

        // Visual + sound
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation().add(0,1,0), 30, 1,1,1, 0.05);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1.2f);
    }
}
