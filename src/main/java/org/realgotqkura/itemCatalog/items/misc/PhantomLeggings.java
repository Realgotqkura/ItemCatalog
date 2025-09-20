package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PhantomLeggings implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;
    private static final HashSet<UUID> phasing = new HashSet<>();

    public PhantomLeggings(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.phantomLeggings");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#7f00ff", "Phantom Leggings"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Light as the night air"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &5Phantom Stride"));
        lore.add(RandomUtils.color("&7While sprinting, you phase through mobs"));
        lore.add(RandomUtils.color("&7for as long as you keep moving."));
        lore.add("");
        lore.add(RandomUtils.color("&0&kXX&5&oPHASE&0&kXX"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "PhantomLeggings"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onSprintToggle(PlayerToggleSprintEvent event){
        Player player = event.getPlayer();
        ItemStack legs = player.getInventory().getLeggings();
        if (!RandomUtils.passedItemChecks(legs, "PhantomLeggings")) return;

        if (event.isSprinting()){
            enablePhasing(player);
        } else {
            disablePhasing(player);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (!phasing.contains(player.getUniqueId())) return;
        if (!player.isSprinting()){
            disablePhasing(player);
            return;
        }

        // visuals
        player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation().add(0,1,0), 3, 0.2,0.3,0.2,0.01);
    }

    private void enablePhasing(Player player){
        if (phasing.contains(player.getUniqueId())) return;
        phasing.add(player.getUniqueId());

        player.setCollidable(false);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 0.6f, 1.2f);

        // safety failsafe – disable after 15s of sprinting
        new BukkitRunnable(){
            @Override
            public void run(){
                if (phasing.contains(player.getUniqueId())){
                    disablePhasing(player);
                }
            }
        }.runTaskLater(plugin, 20 * 15);
    }

    private void disablePhasing(Player player){
        phasing.remove(player.getUniqueId());
        player.setCollidable(true);
    }
}
