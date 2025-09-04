package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;
import org.realgotqkura.itemCatalog.utilities.SoundAndEffectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GiantHammer implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public GiantHammer(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.giantHammer");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.IRON_AXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#6e8574", "Giant Hammer"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Slam slam slam"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK BLOCK &6SLAM"));
        lore.add(RandomUtils.color("&7Slams the blocks on the ground and launches"));
        lore.add(RandomUtils.color("&7nearby entities in the air."));
        lore.add(RandomUtils.color("&7Cooldown: &a20&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "GiantHammer");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(event.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "GiantHammer"))
            return;

        int cooldown = 20;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        if(event.getClickedBlock() == null)
            return;

        List<Block> blocks = new ArrayList<>();
        Location initialLoc = event.getClickedBlock().getLocation();
        for(int i = -2; i <= 2; i++){
            for(int j = -2; j <= 2; j++){
                int random = ThreadLocalRandom.current().nextInt(0, 10+1);
                //~59%
                if(random <= 5){
                    blocks.add(initialLoc.clone().add(i, 0, j).getBlock());
                    for(int k = 0; k < 5; k++){
                        player.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, initialLoc.clone().add(i, 0, j), 5, initialLoc.clone().add(i, 0, j).getBlock().getBlockData());
                    }
                }
            }
        }

        for(Block block : blocks){
            if(block.getType() == Material.AIR)
                continue;

            FallingBlock fBlock = player.getWorld().spawnFallingBlock(block.getLocation().clone().add(0, 2.5,0), block.getBlockData());
            fBlock.setDropItem(false);
            fBlock.setMetadata("gravityHammerBlock", new FixedMetadataValue(plugin, 1));

        }

        for(Entity entity : player.getNearbyEntities(4,4,4)){
            if(!(entity instanceof LivingEntity))
                continue;

            LivingEntity livingEntity = (LivingEntity) entity;
            Vector dir = initialLoc.toVector().subtract(livingEntity.getLocation().toVector()).multiply(1.1).setY(1.5);
            livingEntity.setVelocity(dir);
        }
    }

    @EventHandler
    public void blockChange(EntityChangeBlockEvent event){
        if ((event.getEntity() instanceof FallingBlock) && event.getEntity().hasMetadata("gravityHammerBlock")) {
            event.setCancelled(true); // stops it from becoming a block
        }
    }
}
