package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.*;

public class SoulDagger implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;

    // Tracks hit counts per attacker/target pair
    private static Map<UUID, Map<UUID, Integer>> hitTracker = new HashMap<>();

    public SoulDagger(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.soulDagger");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#8a2be2", "Soul Dagger"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8The blade bites deeper each time..."));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Soul Shred"));
        lore.add(RandomUtils.color("&7Every &a3rd&7 hit on the same target"));
        lore.add(RandomUtils.color("&7deals &cbonus true damage&7."));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "SoulDagger"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player))
            return;

        if (!(event.getEntity() instanceof LivingEntity target))
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "SoulDagger"))
            return;

        UUID attackerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();

        // Track hit counts
        hitTracker.putIfAbsent(attackerId, new HashMap<>());
        Map<UUID, Integer> targetHits = hitTracker.get(attackerId);

        int hits = targetHits.getOrDefault(targetId, 0) + 1;
        targetHits.put(targetId, hits);

        if (hits >= 3) {
            // Reset counter for that target
            targetHits.put(targetId, 0);

            // Deal bonus true damage (ignores armor)
            double bonusDamage = 4.0; // 2 hearts
            target.damage(bonusDamage, player);

            // Particle + sound feedback
            target.getWorld().spawnParticle(Particle.SOUL, target.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.05);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1.2f);
        }
    }
}
