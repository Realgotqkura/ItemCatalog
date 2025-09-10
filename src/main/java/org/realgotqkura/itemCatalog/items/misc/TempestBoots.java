package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.*;

public class TempestBoots implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    // Track players who boosted, so we know to cancel fall damage
    private static Set<UUID> boostedPlayers = new HashSet<>();

    public TempestBoots(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.tempestBoots");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#00ccff", "Tempest Boots"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Ride the wind"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Windlash Jump"));
        lore.add(RandomUtils.color("&7Sprint-jump to launch higher and gain Speed II"));
        lore.add(RandomUtils.color("&7for 3 seconds."));
        lore.add(RandomUtils.color("&7No fall damage."));
        lore.add(RandomUtils.color("&7Cooldown: &a10&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "TempestBoots"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onSprintJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!RandomUtils.passedItemChecks(player.getEquipment().getBoots(), "TempestBoots"))
            return;

        // Detect a jump: Y position increases + player is sprinting + on ground before jump
        if (event.getFrom().getY() < event.getTo().getY() && player.isSprinting()) {
            int cooldown = 10;
            if (!RandomUtils.checkCooldownNoSpam(player, cooldown, cooldowns))
                return;

            // Launch upward slightly stronger than normal jump
            player.setVelocity(player.getVelocity().setY(1.0));

            // Give Speed II for 3s
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true));

            // Mark player as boosted to cancel fall damage later
            boostedPlayers.add(player.getUniqueId());

            // Feedback
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.3, 0.3, 0.3, 0.05);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1f, 1.2f);
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL &&
                boostedPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            boostedPlayers.remove(player.getUniqueId()); // Remove after one fall-cancel
        }
    }
}
