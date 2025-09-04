package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CursedCoin implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();
    private final ItemCatalog plugin;
    private static int customModelDataNum;

    public CursedCoin(ItemCatalog plugin) {
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.cursedCoin");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#ffd700", "Cursed Coin"));

        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Fortune at a cost"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Steal"));
        lore.add(RandomUtils.color("&7Has &a0.1&7% chance to steal a random item"));
        lore.add(RandomUtils.color("&7from a nearby player."));
        lore.add(RandomUtils.color("&7Cooldown: &a300&7s"));
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);

        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "CursedCoin"
        );

        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "CursedCoin"))
            return;

        int cooldown = 300;
        if (!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        // Find nearby players within 5 blocks
        for (Player target : player.getWorld().getPlayers()) {
            if (target.equals(player)) continue;
            if (target.getLocation().distance(player.getLocation()) > 5) continue;

            int random = ThreadLocalRandom.current().nextInt(0, 1000+1);

            if(random == 1){
                List<ItemStack> targetItems = new ArrayList<>();
                for (ItemStack item : target.getInventory().getContents()) {
                    if (item != null) targetItems.add(item);
                }

                if (!targetItems.isEmpty()) {
                    ItemStack stolen = targetItems.get(new Random().nextInt(targetItems.size()));
                    target.getInventory().remove(stolen);
                    player.getInventory().addItem(stolen);

                    // Feedback
                    player.sendMessage(ChatColor.RED + "You stole " + stolen.getType().name() + " from " + target.getName() + "!");
                    target.sendMessage(ChatColor.DARK_RED + "A cursed coin stole an item from you!");

                    player.getWorld().spawnParticle(Particle.LARGE_SMOKE, target.getLocation(), 10, 0.5, 1, 0.5, 0.05);
                    player.getWorld().playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 0.5f);
                }
            }
            return; // Only try 1 player
        }
    }
}
