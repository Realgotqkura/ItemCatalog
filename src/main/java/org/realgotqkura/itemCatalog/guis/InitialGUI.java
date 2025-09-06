package org.realgotqkura.itemCatalog.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.items.ItemsManager;
import org.realgotqkura.itemCatalog.utilities.GuiSorting;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class InitialGUI implements Listener {

    private ItemCatalog plugin;

    public InitialGUI(ItemCatalog plugin){
        this.plugin = plugin;
    }

    private static final String guiName = RandomUtils.color("&8Initial GUI");

    public void createInv(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, guiName);
        GuiSorting sorting = new GuiSorting(plugin);

        sorting.getOuter(inv, false);
        sorting.getInner(inv);
        sorting.corners(inv, sorting.getItems().cornerItems(true));
        inv.setItem(4, itemCount());
        inv.setItem(20, combatItems());
        inv.setItem(22, miscItems());
        inv.setItem(24, allItems());
        inv.setItem(49, sorting.getItems().guiClose());
        player.openInventory(inv);
    }

    @EventHandler
    public void click(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Initial GUI"))
            return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if(event.getSlot() == 49){
            player.closeInventory();
            return;
        }

        if(event.getSlot() == 20){
            CombatItemsGUI gui = new CombatItemsGUI(plugin);
            gui.createStartInv(player);
            return;
        }

        if(event.getSlot() == 22){
            MiscItemsGUI gui = new MiscItemsGUI(plugin);
            gui.createStartInv(player);
            return;
        }

        if(event.getSlot() == 24){
            AllItemsGui gui = new AllItemsGui(plugin);
            gui.createStartInv(player);
        }

    }

    public ItemStack itemCount(){
        ItemStack stack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&e&kEE &b&lTOTAL ITEM COUNT &e&kEE"));
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(RandomUtils.color("&7Current Items: &a" + ItemsManager.ITEM_COUNT));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DEPTH_STRIDER,1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return stack;
    }


    public ItemStack combatItems(){
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&cCombat Items"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8All items related to combat!"));
        lore.add("");
        lore.add(RandomUtils.color("&bLeft Click &eto enter the gui"));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack miscItems(){
        ItemStack stack = new ItemStack(Material.IRON_HORSE_ARMOR);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&7Miscellaneous Items"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8All items outside of combat!"));
        lore.add("");
        lore.add(RandomUtils.color("&bLeft Click &eto enter the gui"));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack allItems(){
        ItemStack stack = new ItemStack(Material.EMERALD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.color("&a&lAll plugin Items"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8All items the plugin has to offer!"));
        lore.add("");
        lore.add(RandomUtils.color("&bLeft Click &eto enter the gui"));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.FIRE_PROTECTION, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return stack;
    }
}
