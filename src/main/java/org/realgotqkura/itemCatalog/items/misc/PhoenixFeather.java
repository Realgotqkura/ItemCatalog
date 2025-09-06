package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

public class PhoenixFeather implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public PhoenixFeather(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.phoenixFeather");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.FEATHER);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#ff9933", "Phoenix Feather"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Rebirth in flames"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Rebirth"));
        lore.add(RandomUtils.color("&7When you would die, revive with &c5 ❤&7 instead"));
        lore.add(RandomUtils.color("&7and create a fiery explosion."));
        lore.add(RandomUtils.color("&7Cooldown: &a60&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "PhoenixFeather"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "PhoenixFeather") &&
                !RandomUtils.passedItemChecks(player.getInventory().getItemInOffHand(), "PhoenixFeather"))
            return;

        double finalHealth = player.getHealth() - event.getFinalDamage();

        if (finalHealth > 0)
            return; // not lethal

        int cooldown = 60;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        event.setCancelled(true);

        // Revive with 5 hearts
        player.setHealth(Math.min(player.getMaxHealth(), 10.0));

        // Fire burst effect
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 80, 1.5, 1.0, 1.5, 0.05);
        player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation(), 80, 1.5, 1.0, 1.5, 0.05);
        player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 20, 1, 0.5, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.5f, 1f); // Replace with another if phoenix not present
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.2f, 0.8f);

        // Ignite nearby enemies
        player.getNearbyEntities(4, 4, 4).forEach(e -> {
            if (e instanceof Player nearby && nearby.equals(player)) return;
            if (e instanceof org.bukkit.entity.LivingEntity le) {
                le.setFireTicks(60);
                le.damage(4, player);
                Vector launchDir = player.getLocation().toVector().subtract(le.getLocation().toVector());
                le.setVelocity(launchDir.multiply(1.2f));
            }
        });
    }
}
