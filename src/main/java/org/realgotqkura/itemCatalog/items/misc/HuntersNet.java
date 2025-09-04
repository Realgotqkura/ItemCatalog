package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HuntersNet implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public HuntersNet(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.huntersNet");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#d1fffc", "Hunter's Net"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Hunter's asset"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Pokeball"));
        lore.add(RandomUtils.color("&7Throws a snowball and traps the entity in cobweb if hit"));
        lore.add(RandomUtils.color("&7The cobwebs despawn after &a15&7s"));
        lore.add(RandomUtils.color("&7Cooldown: &a30&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "HuntersNet");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "HuntersNet"))
            return;

        event.setCancelled(true);

        int cooldown = 30;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;

        Snowball ball = player.launchProjectile(Snowball.class);
        ball.setMetadata("HuntersNet", new FixedMetadataValue(plugin, 1));
    }

    @EventHandler
    public void hit(ProjectileHitEvent event){
        if(!(event.getEntity() instanceof Snowball))
            return;

        if(!event.getEntity().hasMetadata("HuntersNet"))
            return;

        if(event.getHitEntity() == null)
            return;

        HashMap<Location, Material> pastMats = new HashMap<>();
        Location initialLoc = event.getHitEntity().getLocation();
        pastMats.put(initialLoc, initialLoc.getBlock().getType());
        initialLoc.getBlock().setType(Material.COBWEB);
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                Location clonedLoc = initialLoc.clone().add(i, 0, j);
                pastMats.put(clonedLoc, clonedLoc.getBlock().getType());
                clonedLoc.getBlock().setType(Material.COBWEB);
            }
        }


        new BukkitRunnable(){

            @Override
            public void run() {
                pastMats.forEach((k,v) ->{
                    k.getBlock().setType(v);
                });
                cancel();
            }

        }.runTaskLater(plugin, 15*20);

    }
}
