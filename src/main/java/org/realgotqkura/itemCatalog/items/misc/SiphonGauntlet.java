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
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SiphonGauntlet implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public SiphonGauntlet(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.siphonGauntlet");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.GOLDEN_HOE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#ff66cc", "Siphon Gauntlet"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A gauntlet that feeds off your foes"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Hunger Drain"));
        lore.add(RandomUtils.color("&7Drains 2 hunger from all nearby mobs"));
        lore.add(RandomUtils.color("&7and restores it to you."));
        lore.add(RandomUtils.color("&7Cooldown: &a10&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "SiphonGauntlet"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "SiphonGauntlet"))
            return;

        int cooldown = 10;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        // Find nearby living entities within 5 blocks
        for (LivingEntity entity : player.getNearbyEntities(5, 5, 5).stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e).toList()) {
            if (entity.equals(player)) continue;

            // Simulate "draining hunger": damage for effect, restore to player
            entity.damage(0); // optional visual hit (or leave 0)
            int currentFood = player.getFoodLevel();
            player.setFoodLevel(Math.min(currentFood + 2, 20)); // restore 2 hunger per mob

            // Particle + sound feedback
            player.getWorld().spawnParticle(Particle.HEART, entity.getLocation(), 5, 0.5, 1, 0.5, 0.05);
            player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_BURP, 1f, 1f);
        }
    }
}
