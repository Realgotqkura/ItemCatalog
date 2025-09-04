package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
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

public class ChainLightningStaff implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public ChainLightningStaff(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.chainLightningStaff");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.STICK);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#9cf3ff", "Chain Lightning Staff"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A staff that channels lightning"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Chain Lightning"));
        lore.add(RandomUtils.color("&7Strikes a target with lightning"));
        lore.add(RandomUtils.color("&7and arcs to up to 2 nearby entities."));
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
                "ChainLightningStaff"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "ChainLightningStaff"))
            return;

        int cooldown = 8;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        // Find nearest target within 15 blocks
        LivingEntity primaryTarget = null;
        double minDistance = 999;
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.equals(player)) continue;
            double dist = entity.getLocation().distance(player.getLocation());
            if (dist <= 15 && dist < minDistance) {
                primaryTarget = entity;
                minDistance = dist;
            }
        }

        if (primaryTarget == null) return;

        // Strike primary target
        strikeLightning(primaryTarget);

        // Find up to 2 nearby targets for chain effect
        int chains = 0;
        for (LivingEntity nearby : primaryTarget.getNearbyEntities(6, 6, 6).stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e).toList()) {
            if (nearby.equals(player) || nearby.equals(primaryTarget)) continue;
            strikeLightning(nearby);
            chains++;
            if (chains >= 2) break;
        }
    }

    private void strikeLightning(LivingEntity target) {
        target.getWorld().strikeLightningEffect(target.getLocation());
        target.damage(4); // 2 hearts; adjust if needed
    }
}
