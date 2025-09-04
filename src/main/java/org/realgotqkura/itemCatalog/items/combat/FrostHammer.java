package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.*;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FrostHammer implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public FrostHammer(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.frostHammer");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#4ad8ff", "Frost Hammer"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Frozen relic of the north"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Frost Slam"));
        lore.add(RandomUtils.color("&7Freezes enemies in a &a5&7 block radius"));
        lore.add(RandomUtils.color("&7Slowness III for &a5s"));
        lore.add(RandomUtils.color("&7Cooldown: &a15&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "FrostHammer"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "FrostHammer"))
            return;

        int cooldown = 15;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        Location center = player.getLocation();
        World world = center.getWorld();

        // Freeze effect
        for (LivingEntity entity : world.getLivingEntities()) {
            if (entity.equals(player)) continue;
            if (entity.getLocation().distance(center) <= 5) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2)); // Slowness III, 5s
                world.spawnParticle(Particle.SNOWFLAKE, entity.getLocation(), 20, 1, 1, 1, 0.05);
                world.playSound(entity.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 0.5f);
            }
        }

        // Slam feedback
        world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.8f);
        world.spawnParticle(Particle.ITEM_SNOWBALL, center, 50, 2, 0.5, 2, 0.1);
    }
}
