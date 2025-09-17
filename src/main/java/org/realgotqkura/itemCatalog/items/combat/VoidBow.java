package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class VoidBow implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;
    private static NamespacedKey voidBowKey;

    public VoidBow(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.voidBow");
        voidBowKey = RandomUtils.nskContainer.get("VoidBowArrow");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.BOW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#4b0082", "Void Bow"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Arrows of pure void energy"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Phasing Arrows"));
        lore.add(RandomUtils.color("&7Arrows fired pierce through enemies,"));
        lore.add(RandomUtils.color("&7damaging multiple targets in a line."));
        lore.add("");
        lore.add(RandomUtils.color("&0&kOO&5&oVOID&0&kOO"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "VoidBow"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event){
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        ItemStack held = player.getInventory().getItemInMainHand();
        if(!RandomUtils.passedItemChecks(held, "VoidBow")) return;

        // Mark this arrow as "void-powered"
        arrow.getPersistentDataContainer().set(voidBowKey, PersistentDataType.INTEGER, 1);
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        // Only apply for void-marked arrows
        if (!arrow.getPersistentDataContainer().has(voidBowKey, PersistentDataType.INTEGER)) return;

        // Add voidy visuals
        target.getWorld().spawnParticle(Particle.PORTAL, target.getLocation().add(0, 1, 0), 20, 0.4, 0.6, 0.4, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.6f, 1.5f);

        // Prevent arrow from being removed after hitting
        arrow.setPierceLevel(10); // large pierce so it can hit many
        arrow.setBounce(false);
    }
}
