package org.realgotqkura.itemCatalog.items.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.utilities.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class IronbackHelm implements Listener {

    private static int customModelDataNum;

    public IronbackHelm(ItemCatalog plugin) {
        customModelDataNum = plugin.getConfig().getInt("customModelData.ironbackHelm");
    }

    public static ItemStack item() {
        ItemStack stack = new ItemStack(Material.IRON_HELMET);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(RandomUtils.safeHexColor("#b5b5b5", "Ironback Helm"));
        List<String> lore = new ArrayList<>();
        lore.add(RandomUtils.color("&8Heavy and unyielding"));
        lore.add("");
        lore.add(RandomUtils.color("&e&lPASSIVE &6Iron Stance"));
        lore.add(RandomUtils.color("&7Reduces knockback taken by &a50%"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(customModelDataNum);
        meta.getPersistentDataContainer().set(
                RandomUtils.nskContainer.get("ItemCatalogID"),
                PersistentDataType.STRING,
                "IronbackHelm"
        );
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onKnockback(EntityKnockbackByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        ItemStack helmet = player.getInventory().getHelmet();
        if (!RandomUtils.passedItemChecks(helmet, "IronbackHelm")) return;

        // Reduce knockback by 50%
        event.setFinalKnockback(event.getKnockback().multiply(0.5f));
    }
}
