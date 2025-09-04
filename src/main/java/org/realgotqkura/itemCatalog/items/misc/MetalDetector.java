package org.realgotqkura.itemCatalog.items.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MetalDetector implements Listener {

    private static HashMap<UUID, Double> cooldowns = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;

    public MetalDetector(ItemCatalog plugin){
        this.plugin = plugin;
        customModelDataNum = plugin.getConfig().getInt("customModelData.metalDetector");
    }


    public static ItemStack item(){
        ItemStack stack = new ItemStack(Material.IRON_HOE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#c1cfa5", "Metal Detector"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Legal X-ray"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Detect"));
        lore.add(RandomUtils.color("&7Shows the location of gold, diamonds and emeralds"));
        lore.add(RandomUtils.color("&7inside the chunk where it was used"));
        lore.add(RandomUtils.color("&7Cooldown: &a20&7s"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "MetalDetector");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        if(event.getHand() != EquipmentSlot.HAND)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "MetalDetector"))
            return;

        event.setCancelled(true);

        int cooldown = 20;
        if(!RandomUtils.checkCooldown(player, cooldown, cooldowns))
            return;


        List<Block> detectedBlocks = new ArrayList<>();
        Chunk chunk = player.getLocation().getChunk();

        int minY = player.getWorld().getMinHeight();
        int maxY = player.getWorld().getMaxHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.GOLD_ORE
                            || block.getType() == Material.DIAMOND_ORE
                            || block.getType() == Material.EMERALD_ORE) {
                        detectedBlocks.add(block);
                    }
                }
            }
        }

        List<Shulker> entities = new ArrayList<>();
        detectedBlocks.forEach(block -> {
            Shulker entity = (Shulker) player.getWorld().spawnEntity(block.getLocation(), EntityType.SHULKER);
            entity.setAI(false);
            entity.setCollidable(false);

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getMainScoreboard();

            String teamName = block.getType().name();
            Team team = board.getTeam(teamName);
            if (team == null) {
                team = board.registerNewTeam(teamName);
                switch (block.getType()) {
                    case GOLD_ORE -> team.setColor(ChatColor.GOLD);
                    case DIAMOND_ORE -> team.setColor(ChatColor.AQUA);
                    case EMERALD_ORE -> team.setColor(ChatColor.GREEN);
                }
            }

            team.addEntry(entity.getUniqueId().toString());
            entity.setGlowing(true);

            entities.add(entity); // <--- missing in your code
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                entities.forEach(Entity::remove);
                entities.clear();
                cancel();
            }
        }.runTaskLater(plugin, 10 * 20);
    }
}
