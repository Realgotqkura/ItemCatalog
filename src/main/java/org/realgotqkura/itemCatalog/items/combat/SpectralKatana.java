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

public class SpectralKatana implements Listener {
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public SpectralKatana(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.spectralKatana");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#a0e6ff", "Spectral Katana"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Blade forged from spectral steel"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON HIT &6Phantom Strike"));
        lore.add(RandomUtils.color("&7Deals bonus &cTrue Damage &7that"));
        lore.add(RandomUtils.color("&7ignores shields and armor."));
        lore.add("");
        lore.add(RandomUtils.color("&0&kOO&b&oKATANA&0&kOO"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "SpectralKatana"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof LivingEntity target)) return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "SpectralKatana"))
            return;

        // Base true damage (ignores armor) = 2 hearts
        double trueDamage = 4.0;

        // Cancel shields blocking
        if(target instanceof Player p){
            if(p.isBlocking()){
                p.setCooldown(Material.SHIELD, 60); // disable shield briefly
                p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 0.9f);
            }
        }

        // Apply the true damage (bypasses armor by direct health modification)
        double newHealth = target.getHealth() - trueDamage;
        target.setHealth(Math.max(0, newHealth));

        // Add particle + sound for flair
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0,1,0), 20, 0.5, 0.5, 0.5, 0.1);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1.3f);
    }
}
