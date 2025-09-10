package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LuminaraSpear implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public LuminaraSpear(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.luminaraSpear");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.TRIDENT);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#ffff99", "Luminara Spear"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Spear of radiant light"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Light Spear"));
        lore.add(RandomUtils.color("&7Throws a spear of light that stuns enemies"));
        lore.add(RandomUtils.color("&7Deals &a4 &7damage"));
        lore.add(RandomUtils.color("&7Cooldown: &a15&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "LuminaraSpear");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "LuminaraSpear"))
            return;

        event.setCancelled(true);

        int cooldown = 15;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        Vector dir = player.getLocation().getDirection().normalize().multiply(1.5);
        Location start = player.getEyeLocation();

        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {
                if(ticks > 20) cancel(); // lasts ~1s

                Location current = start.clone().add(dir.clone().multiply(ticks * 0.5));
                player.getWorld().spawnParticle(Particle.END_ROD, current, 5, 0.2, 0.2, 0.2, 0.05);

                // Damage & stun entities
                for(Entity e : current.getWorld().getNearbyEntities(current, 1, 1, 1)){
                    if(e instanceof LivingEntity le && !le.equals(player)){
                        le.damage(4, player); // 2 hearts
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 2)); // stun for 2s
                        le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));
                    }
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
