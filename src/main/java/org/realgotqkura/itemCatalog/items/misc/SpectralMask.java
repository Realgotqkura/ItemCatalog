package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.*;

public class SpectralMask implements Listener {

    private static int customModelDataNum;
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN_SECONDS = 15;
    private static final int INVIS_DURATION_TICKS = 100; // 5 seconds
    private ItemCatalog plugin;

    public SpectralMask(ItemCatalog plugin) {
        customModelDataNum = plugin.getConfig().getInt("customModelData.spectralMask");
        this.plugin = plugin;
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#5a5a5a", "Spectral Mask"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A mask shrouded in mystery"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lSNEAK &6Shadowmeld"));
        lore.add(RandomUtils.color("&7Become invisible while sneaking"));
        lore.add(RandomUtils.color("&7Duration: &a5s &7Cooldown: &a15s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "SpectralMask"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        if (!RandomUtils.passedItemChecks(helmet, "SpectralMask")) return;

        if (!event.isSneaking()) return;

        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();

        if (cooldowns.containsKey(uuid) && cooldowns.get(uuid) > now) {
            long remaining = (cooldowns.get(uuid) - now) / 1000;
            player.sendMessage(RandomUtils.color("&cSpectral Mask is on cooldown for &e" + remaining + "s"));
            return;
        }

        cooldowns.put(uuid, now + (COOLDOWN_SECONDS * 1000L));

        player.setInvisible(true);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                player.setInvisible(false);
            }
        }, INVIS_DURATION_TICKS);
    }
}
