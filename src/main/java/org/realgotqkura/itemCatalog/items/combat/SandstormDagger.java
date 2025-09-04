package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
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

public class SandstormDagger implements Listener {

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public SandstormDagger(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.sandstormDagger");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#fffd8a", "Sandstorm Dagger"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Is that a Baxter reference?"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON HIT &6Sandy Eyes"));
        lore.add(RandomUtils.color("&7Gives Blindness &aI &7to the hit enemy"));
        lore.add(RandomUtils.color("&7for &a5 &7seconds. If already blinded,"));
        lore.add(RandomUtils.color("&7deals &c+20&7% damage instead."));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "SandstormDagger");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof LivingEntity))
            return;

        LivingEntity damager = (LivingEntity) event.getDamager();

        if(!RandomUtils.passedItemChecks(damager.getEquipment().getItemInMainHand(), "SandstormDagger"))
            return;

        if(!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity hitEntity = (LivingEntity) event.getEntity();

        if(hitEntity.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            event.setDamage(event.getDamage() * 1.2f);
            return;
        }

        hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 0, false, false, false));
    }
}
