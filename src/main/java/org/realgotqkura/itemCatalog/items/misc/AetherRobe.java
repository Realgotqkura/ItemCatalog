package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.List;

public class AetherRobe implements Listener {

    private static int customModelDataNum;

    public AetherRobe(ItemCatalog plugin) {
        customModelDataNum = plugin.getConfig().getInt("customModelData.aetherRobe");

        // Task to apply slow falling effect while falling
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack chest = player.getInventory().getChestplate();
                    if (!RandomUtils.passedItemChecks(chest, "AetherRobe")) continue;

                    if (!player.isOnGround() && player.getVelocity().getY() < -0.3) {
                        player.setVelocity(player.getVelocity().setY(-0.2)); // soften descent
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#87ceeb", "Aether Robe"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Woven with skybound threads"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Glide"));
        lore.add(RandomUtils.color("&7Slowly glide down when falling"));
        lore.add(RandomUtils.color("&7No fall damage taken"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "AetherRobe"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Player player = (Player) event.getEntity();
        ItemStack chest = player.getInventory().getChestplate();
        if (!RandomUtils.passedItemChecks(chest, "AetherRobe")) return;

        event.setCancelled(true); // no fall damage
    }
}
