package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;
import org.realgotqkura.itemCatalog.utilities.data.PlayerDataConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SoulScythe implements Listener {


    private HashMap<Player, Boolean> strengthMap = new HashMap<>();

    private ItemCatalog plugin;
    private static int customModelDataNum;
    private PlayerDataConfig playerDataConfig;

    public SoulScythe(ItemCatalog plugin){
        this.plugin = plugin;
        playerDataConfig = plugin.getPlayerData();
        customModelDataNum = plugin.getConfig().getInt("customModelData.soulScythe");
    }

    public static ItemStack item(Player player){
        ItemCatalog plugin = ItemCatalog.instance;
        PlayerDataConfig playerDataConfig1 = plugin.getPlayerData();
        int souls = 0;
        if(player != null)
            souls = playerDataConfig1.getConfig().getInt("players." + player.getUniqueId() + ".soulScytheSouls");

        ItemStack stack = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#58e0be", "Soul Scythe"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Store the souls of your enemies"));
        lore.add("");
        lore.add(RandomUtils.color("&7Current Souls: &c" + souls));
        lore.add(RandomUtils.color("&7Current Mode: &aHealing"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lON KILL &6Consume"));
        lore.add(RandomUtils.color("&7Consumes the souls of your opponents."));
        lore.add("");
        lore.add(RandomUtils.color("&e&lSHIFT RIGHT CLICK &6Switch"));
        lore.add(RandomUtils.color("&7Switch between modes."));
        lore.add("");
        lore.add(RandomUtils.color("&e&lRIGHT CLICK &6Consume"));
        lore.add(RandomUtils.color("&aHealth &7Mode: &a+2 &7HP per Soul"));
        lore.add(RandomUtils.color("&cDamage &7Mode: &c+10% &7Damage per Soul"));
        lore.add("");
        lore.add(RandomUtils.color("&8Has the same damage as a Diamond Sword."));
        lore.add(RandomUtils.color("&8Its faster than a Diamond Sword."));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        AttributeModifier damageModifier = new AttributeModifier(
                new NamespacedKey(plugin, "soul_scythe_damage"),
                6.0,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND.getGroup()
        );

        AttributeModifier attackSpeedModifier = new AttributeModifier(
                new NamespacedKey(plugin, "soul_scythe_speed"),
                -1,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND.getGroup()
        );

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, attackSpeedModifier);
        meta.getPersistentDataContainer().set(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING, "SoulScythe");
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void kill(EntityDeathEvent event){
        if(event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();


        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "SoulScythe"))
            return;

        int souls = playerDataConfig.getConfig().getInt("players." + player.getUniqueId() + ".soulScytheSouls");

        if(souls > 10)
            return;

        playerDataConfig.getConfig().set("players." + player.getUniqueId() + ".soulScytheSouls", souls+1);
        playerDataConfig.saveConfig();
        ItemStack scythe = player.getInventory().getItemInMainHand();
        ItemMeta meta = scythe.getItemMeta();
        List<String> lore = meta.getLore();

        for(int i = 0; i < lore.size(); i++){
            if(lore.get(i).contains("Current Souls: "))
                lore.set(i, RandomUtils.color("&7Current Souls: &c" + (souls+1)));
        }

        meta.setLore(lore);
        scythe.setItemMeta(meta);
        player.getInventory().setItemInMainHand(scythe);
    }

    @EventHandler
    public void click(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(!RandomUtils.passedItemChecks(player.getInventory().getItemInMainHand(), "SoulScythe"))
            return;

        boolean mode = playerDataConfig.getConfig().getBoolean("players." + player.getUniqueId() + ".soulScytheMode");
        int souls = playerDataConfig.getConfig().getInt("players." + player.getUniqueId() + ".soulScytheSouls");

        if(player.isSneaking()){
            if(mode){
                playerDataConfig.getConfig().set("players." + player.getUniqueId() + ".soulScytheMode", false);
            }else{
                playerDataConfig.getConfig().set("players." + player.getUniqueId() + ".soulScytheMode", true);
            }
            playerDataConfig.saveConfig();
            ItemStack scythe = player.getInventory().getItemInMainHand();
            ItemMeta meta = scythe.getItemMeta();
            List<String> lore = meta.getLore();

            for(int i = 0; i < lore.size(); i++){
                if(!lore.get(i).contains("Current Mode: "))
                    continue;

                //Inverse it false = health, true = strength
                if(mode){
                    lore.set(i, RandomUtils.color("&7Current Mode: &cStrength"));
                }else{
                    lore.set(i, RandomUtils.color("&7Current Mode: &aHealth"));
                }
            }

            meta.setLore(lore);
            scythe.setItemMeta(meta);
            player.getInventory().setItemInMainHand(scythe);
            return;
        }

        if(souls < 1)
            return;

        if(!mode){
            player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(player.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() * (1f + (souls/10f)));
            strengthMap.put(player, true);
            playerDataConfig.getConfig().set("players." + player.getUniqueId() + ".soulScytheSouls", 0);
            playerDataConfig.saveConfig();
            ItemStack scythe = player.getInventory().getItemInMainHand();
            ItemMeta meta = scythe.getItemMeta();
            List<String> lore = meta.getLore();

            for(int i = 0; i < lore.size(); i++){
                if(lore.get(i).contains("Current Souls: "))
                    lore.set(i, RandomUtils.color("&7Current Souls: &c" + (souls+1)));
            }

            meta.setLore(lore);
            scythe.setItemMeta(meta);
            player.getInventory().setItemInMainHand(scythe);
            return;
        }

        if((player.getHealth() + (2 * souls)) < player.getMaxHealth()) {
            player.setHealth(player.getHealth() + (2 * souls));
        }else{
            player.setHealth(player.getMaxHealth());
        }

        playerDataConfig.getConfig().set("players." + player.getUniqueId() + ".soulScytheSouls", 0);
        playerDataConfig.saveConfig();
        ItemStack scythe = player.getInventory().getItemInMainHand();
        ItemMeta meta = scythe.getItemMeta();
        List<String> lore = meta.getLore();

        for(int i = 0; i < lore.size(); i++){
            if(lore.get(i).contains("Current Souls: "))
                lore.set(i, RandomUtils.color("&7Current Souls: &c" + (souls+1)));
        }

        meta.setLore(lore);
        scythe.setItemMeta(meta);
        player.getInventory().setItemInMainHand(scythe);
    }


    @EventHandler
    public void hit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();

        if(!strengthMap.containsKey(player))
            return;

        if(!strengthMap.get(player))
            return;

        player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(player.getAttribute(Attribute.ATTACK_DAMAGE).getDefaultValue());
        strengthMap.put(player, false);
    }

}
