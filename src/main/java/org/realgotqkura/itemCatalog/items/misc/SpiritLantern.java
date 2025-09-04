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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SpiritLantern implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public SpiritLantern(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.spiritLantern");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.LANTERN);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#66ffff", "Spirit Lantern"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Light of the unseen"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Reveal Spirits"));
        lore.add(RandomUtils.color("&7Reveals all nearby invisible entities for 10s."));
        lore.add(RandomUtils.color("&7Cooldown: &a20&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "SpiritLantern"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "SpiritLantern"))
            return;

        int cooldown = 20;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        // Reveal nearby invisible entities within 10 blocks
        for (LivingEntity entity : player.getNearbyEntities(10, 10, 10).stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e).toList()) {
            if (entity.equals(player)) continue;

            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1)); // 10s
        }

        // Particle + sound feedback
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 50, 2, 1, 2, 0.05);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1f);
    }
}
