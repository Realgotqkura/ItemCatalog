package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TitanChestplate implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;
    private static final HashMap<UUID, Double> cooldowns = new HashMap<>();

    public TitanChestplate(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.titanChestplate");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#ffcc00", "Titan Chestplate"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Forged with the strength of titans"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Titan’s Resilience"));
        lore.add(RandomUtils.color("&7When below &c30% HP&7, gain temporary max health"));
        lore.add(RandomUtils.color("&7and absorption shield for &a8s&7."));
        lore.add(RandomUtils.color("&7Cooldown: &a20&7s"));
        lore.add("");
        lore.add(RandomUtils.color("&0&kXX&6&oTITAN&0&kXX"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "TitanChestplate"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack chest = player.getInventory().getChestplate();
        if (!RandomUtils.passedItemChecks(chest, "TitanChestplate")) return;

        double health = player.getHealth();
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        double threshold = maxHealth * 0.3;

        if (health > threshold) return;

        int cooldown = 20;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns)) return;

        // Boost stats
        AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) return;
        double originalMax = attr.getBaseValue();
        attr.setBaseValue(originalMax + 10); // +5 hearts

        player.setAbsorptionAmount(8); // 4 hearts absorption

        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0,1,0), 30, 0.6,0.8,0.6,0.2);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 0.8f);

        // Reset after 8 seconds
        new BukkitRunnable(){
            @Override
            public void run(){
                attr.setBaseValue(originalMax);
                player.setAbsorptionAmount(0);
            }
        }.runTaskLater(plugin, 20 * 8);
    }
}
