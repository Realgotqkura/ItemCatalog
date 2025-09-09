package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AbyssalHarpoon implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public AbyssalHarpoon(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.abyssalHarpoon");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#1a2d66", "Abyssal Harpoon"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Hook from the deep"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Abyssal Pull"));
        lore.add(RandomUtils.color("&7Pulls the nearest entity you're looking at"));
        lore.add(RandomUtils.color("&7Cooldown: &a12&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "AbyssalHarpoon");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void harpoonUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "AbyssalHarpoon"))
            return;

        int cooldown = 12;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        // Ray trace to find target
        LivingEntity target = null;
        double range = 20.0;
        List<Entity> nearby = player.getNearbyEntities(range, range, range);

        for (Entity e : nearby) {
            if (e instanceof LivingEntity && !(e instanceof ArmorStand)) {
                Vector toEntity = e.getLocation().toVector().subtract(player.getEyeLocation().toVector());
                Vector direction = player.getLocation().getDirection();

                // Check if within ~30 degree cone of vision
                if (direction.angle(toEntity) < Math.toRadians(30)) {
                    if (target == null || player.getLocation().distance(e.getLocation()) < player.getLocation().distance(target.getLocation())) {
                        target = (LivingEntity) e;
                    }
                }
            }
        }

        if (target == null) {
            player.sendMessage(ChatColor.RED + "No target found!");
            return;
        }

        LivingEntity pulled = target;

        // Apply pull
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks > 10 || pulled.isDead() || !pulled.isValid()) {
                    cancel();
                    return;
                }
                Vector pullVec = player.getLocation().toVector().subtract(pulled.getLocation().toVector()).normalize().multiply(0.6);
                pulled.setVelocity(pullVec);
                pulled.getWorld().spawnParticle(Particle.SMOKE, pulled.getLocation().add(0, 1, 0), 5, 0.2, 0.2, 0.2, 0.01);
                pulled.getWorld().playSound(pulled.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1f, 0.8f);
                ticks++;
            }
        }.runTaskTimer(plugin, 0, 2);
    }
}
