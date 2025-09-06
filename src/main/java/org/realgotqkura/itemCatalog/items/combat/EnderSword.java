package org.realgotqkura.itemCatalog.items.combat;

import jdk.jfr.Enabled;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;
import org.realgotqkura.itemCatalog.utilities.SoundAndEffectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EnderSword implements Listener {


    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public EnderSword(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.enderSword");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#fcfb95", "Ender Sword"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8A sword made from the essence of The End"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Teleport Slash"));
        lore.add(RandomUtils.color("&7Teleports to the nearest entity and"));
        lore.add(RandomUtils.color("&7deals &c2 &7damage."));
        lore.add(RandomUtils.color("&7Cooldown: &b10s"));
        lore.add(RandomUtils.color("&7Range: &a15 &7blocks"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "EnderSword");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "EnderSword"))
            return;

        int cooldown = 10;

        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        LivingEntity tpEntity = null;

        for(Entity entity : player.getNearbyEntities(15,5,15)){
            if(entity instanceof LivingEntity){
                tpEntity = (LivingEntity) entity;
            }
        }

        if(tpEntity == null){
            player.sendMessage(RandomUtils.color("&cNo Entity nearby!"));
            return;
        }

        Location tpLoc = tpEntity.getLocation().clone().add(1 * Math.sin(Math.toRadians(tpEntity.getLocation().getYaw())), 1, 1 * Math.cos(Math.toRadians(tpEntity.getLocation().getYaw())));
        player.teleport(tpLoc);
        SoundAndEffectUtils.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2);
        SoundAndEffectUtils.makeParticle(player.getLocation(), Particle.DRAGON_BREATH, 4, 4, -1);
    }
}
