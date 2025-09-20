package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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

public class EtherealHelm implements Listener {

    private final ItemCatalog plugin;
    private static int customModelDataNum;
    private static final HashMap<UUID, Double> cooldowns = new HashMap<>();

    public EtherealHelm(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.etherealHelm");
    }

    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#c0ffff", "Ethereal Helm"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A helmet imbued with ghostly essence"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lCROUCH &6Blink"));
        lore.add(RandomUtils.color("&7Teleport forward a short distance"));
        lore.add(RandomUtils.color("&7Cooldown: &a5&7s"));
        lore.add("");
        lore.add(RandomUtils.color("&0&kOO&b&oETHEREAL&0&kOO"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "EtherealHelm"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();

        if (!player.isSneaking()) return; // only trigger when crouching
        ItemStack helmet = player.getInventory().getHelmet();
        if (!RandomUtils.passedItemChecks(helmet, "EtherealHelm")) return;

        int cooldown = 5;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns)) return;

        Location loc = player.getLocation();
        Vector dir = loc.getDirection().normalize().multiply(6); // blink 6 blocks forward
        Location target = loc.clone().add(dir);

        // Check for safe landing spot
        target.setY(target.getWorld().getHighestBlockYAt(target) + 1);

        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0,1,0), 20, 0.5,0.5,0.5,0.1);
        player.getWorld().spawnParticle(Particle.PORTAL, target.add(0,0,0).add(0,1,0), 20, 0.5,0.5,0.5,0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.5f);
        player.getWorld().playSound(target, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.5f);

        player.teleport(target);
    }
}
