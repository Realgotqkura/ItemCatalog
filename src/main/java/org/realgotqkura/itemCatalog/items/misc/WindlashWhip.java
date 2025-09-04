package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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

public class WindlashWhip implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public WindlashWhip(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.windlashWhip");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#99ffff", "Windlash Whip"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Harness the power of wind"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Pull"));
        lore.add(RandomUtils.color("&7Pulls all nearby enemies 3 blocks towards you."));
        lore.add(RandomUtils.color("&7Cooldown: &a8&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "WindlashWhip"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "WindlashWhip"))
            return;

        int cooldown = 8;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        // Pull nearby living entities within 5 blocks
        for (LivingEntity entity : player.getNearbyEntities(5, 5, 5).stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e).toList()) {
            if (entity.equals(player)) continue;

            Vector direction = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
            entity.setVelocity(direction.multiply(1.5)); // pulls 3 blocks-ish

            // Particle + sound feedback
            player.getWorld().spawnParticle(Particle.CLOUD, entity.getLocation(), 15, 0.5, 1, 0.5, 0.05);
            player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
        }
    }
}
