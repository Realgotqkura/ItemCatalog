package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class GlacialBow implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public GlacialBow(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.glacialBow");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.BOW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#9cf3ff", "Glacial Bow"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Freezing ranged weapon"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON HIT &6Frostbite"));
        lore.add(RandomUtils.color("&7Arrows inflict &bSlowness II"));
        lore.add(RandomUtils.color("&7for &a5s &7on enemies struck."));
        lore.add("");
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "GlacialBow"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow))
            return;

        if (!(arrow.getShooter() instanceof Player player))
            return;

        if (!RandomUtils.passedItemChecks(player.getEquipment().getItemInMainHand(), "GlacialBow"))
            return;

        if (!(event.getEntity() instanceof LivingEntity target))
            return;

        // Apply Slowness II for 5 seconds
        target.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                100, // 5s * 20 ticks
                1    // amplifier: 1 = Slowness II
        ));
    }
}
