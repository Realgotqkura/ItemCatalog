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

import java.util.ArrayList;
import java.util.List;

public class SoulReaperAxe implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public SoulReaperAxe(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.soulReaperAxe");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#5c0f0f", "Soul Reaper Axe"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8An axe that grows stronger as death draws near"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Executioner's Wrath"));
        lore.add(RandomUtils.color("&7Deal increased damage the lower"));
        lore.add(RandomUtils.color("&7your target's health is."));
        lore.add("");
        lore.add(RandomUtils.color("&0&kOO&4&oREAPER&0&kOO"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "SoulReaperAxe"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack held = player.getInventory().getItemInMainHand();
        if(!RandomUtils.passedItemChecks(held, "SoulReaperAxe")) return;

        double maxHealth = target.getMaxHealth();
        double currentHealth = target.getHealth();

        double healthMissing = maxHealth - currentHealth;
        double percentMissing = healthMissing / maxHealth; // 0.0 to 1.0

        // Scale: up to +50% bonus damage when target is at 1 HP
        double multiplier = 1.0 + (0.5 * percentMissing);
        double newDamage = event.getDamage() * multiplier;
        event.setDamage(newDamage);

        // Effects if the target is weak
        if (percentMissing > 0.5) {
            target.getWorld().spawnParticle(Particle.SOUL, target.getLocation().add(0, 1, 0), 10, 0.3, 0.5, 0.3, 0.05);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.6f, 1.8f);
        }
    }
}
