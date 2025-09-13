package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ObsidianMace implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;

    // Track hit counts per player
    private static final HashMap<UUID, Integer> hitCounts = new HashMap<>();

    public ObsidianMace(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.obsidianMace");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#4b0082", "Obsidian Mace"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Forged from volcanic depths"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON HIT &6Crushing Blow"));
        lore.add(RandomUtils.color("&7Every 4th hit causes an explosion"));
        lore.add("");
        lore.add(RandomUtils.color("&0&kOO&5&oMACE&0&kOO"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "ObsidianMace"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof LivingEntity target)) return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "ObsidianMace"))
            return;

        UUID id = player.getUniqueId();
        int count = hitCounts.getOrDefault(id, 0) + 1;

        if(count >= 4){
            count = 0; // reset counter

            Location loc = target.getLocation();
            target.getWorld().spawnParticle(Particle.EXPLOSION, loc, 3);
            target.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.8f);

            // Small explosion effect (without block damage)
            for(Entity e : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)){
                if(e instanceof LivingEntity le && !le.equals(player)){
                    le.damage(6, player); // extra 3 hearts true-ish damage
                }
            }
        }

        hitCounts.put(id, count);
    }
}
