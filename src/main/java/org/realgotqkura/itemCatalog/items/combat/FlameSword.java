package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FlameSword implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public FlameSword(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.flameSword");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#f77148", "Flame Sword"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A fire hazard"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Fire Slash"));
        lore.add(RandomUtils.color("&7Sets the &a7&7 blocks on fire"));
        lore.add(RandomUtils.color("&7Cooldown: &a10&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "FlameSword");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void hit(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "FlameSword"))
            return;

        int cooldown = 10;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        Vector dir = player.getLocation().getDirection().normalize();
        Location base = player.getLocation();

        for (int i = 2; i <= 8; i++) {
            Location loc = base.clone().add(dir.clone().multiply(i));

            // Find highest block at that X/Z
            int highestY = loc.getWorld().getHighestBlockYAt(loc);
            Location fireLoc = new Location(loc.getWorld(), loc.getX(), highestY + 1, loc.getZ());

            // Only place fire if air
            if (fireLoc.getBlock().getType() == Material.AIR || !fireLoc.getBlock().getType().isBlock()) {
                fireLoc.getBlock().setType(Material.FIRE);
            }
        }
    }
}
