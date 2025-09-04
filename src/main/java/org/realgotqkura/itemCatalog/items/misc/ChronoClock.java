package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Material;
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
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChronoClock implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public ChronoClock(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.chronoClock");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.CLOCK);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&x&F&F&F&F&F&FC&x&E&E&E&E&E&Eh&x&D&D&D&D&D&Dr&x&C&C&C&C&C&Co&x&B&B&B&B&B&Bn&x&A&B&A&B&A&Bo&x&9&A&9&A&9&AC&x&8&9&8&9&8&9l&x&7&8&7&8&7&8o&x&6&7&6&7&6&7c&x&5&6&5&6&5&6k"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8The clock of the gods"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6STOP"));
        lore.add(RandomUtils.color("&7Freezes non-player entities for &a15&7s"));
        lore.add(RandomUtils.color("&7Damages players for &c5 &7damage instead."));
        lore.add(RandomUtils.color("&7Range: &a30 &7blocks"));
        lore.add(RandomUtils.color("&7Cooldown: &a1&7m"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "ChronoClock");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "ChronoClock") && !RandomUtils.passedItemChecks(player.getInventory().getItemInOffHand(), "ChronoClock"))
            return;

        int cooldown = 60;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        player.sendTitle(RandomUtils.color("&0&kEE &x&F&F&F&F&F&FT&x&E&E&E&E&E&Ei&x&D&D&D&D&D&Dm&x&C&C&C&C&C&Ce &x&A&B&A&B&A&BF&x&9&A&9&A&9&Ar&x&8&9&8&9&8&9e&x&7&8&7&8&7&8e&x&6&7&6&7&6&7z&x&5&6&5&6&5&6e &0&kEE"), "", 5, 20, 5);

        for(Entity entity : player.getNearbyEntities(30,10,30)){
            if(!(entity instanceof LivingEntity))
                continue;

            if(entity instanceof Player){
                Player plEntity = (Player) entity;
                plEntity.damage(5);
                continue;
            }

            LivingEntity lEntity = (LivingEntity) entity;
            lEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 15*20, 255, false, false, false));
        }
    }
}
