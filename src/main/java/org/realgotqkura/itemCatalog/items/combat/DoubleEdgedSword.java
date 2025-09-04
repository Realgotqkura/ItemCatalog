package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class DoubleEdgedSword implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public DoubleEdgedSword(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.doubleEdgedSword");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#940a21", "Double Edged Sword"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A risky weapon"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON HIT &6Trade off"));
        lore.add(RandomUtils.color("&7Does &c30&7% more damage but gives Weakness &aI"));
        lore.add(RandomUtils.color("&7for &a3 &7seconds"));
        lore.add(RandomUtils.color("&7Cooldown: &a4&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "DoubleEdgedSword");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof LivingEntity))
            return;

        LivingEntity lEntity = (LivingEntity) event.getDamager();

        if(!RandomUtils.passedItemChecks(lEntity.getEquipment().getItemInMainHand(), "DoubleEdgedSword"))
            return;

        event.setDamage(event.getDamage() * 1.3f);

        if(event.getEntity() instanceof LivingEntity)
            ((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3*20, 0, false, false, true));
    }
}
