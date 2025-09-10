package org.realgotqkura.itemCatalog.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataType;
import org.realgotqkura.itemCatalog.ItemCatalog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RandomUtils {

    public static HashMap<String, NamespacedKey> nskContainer = new HashMap<>();

    public static void loadNskContainer(ItemCatalog plugin){
        nskContainer.put("ItemCatalogID", new NamespacedKey(plugin, "item_catalog_id"));
    }

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static String safeHexColor(String hex, String message) {
        if (!hex.startsWith("#") || hex.length() != 7) return message;

        StringBuilder builder = new StringBuilder("§x");
        for (char c : hex.substring(1).toCharArray()) {
            builder.append("§").append(c);
        }

        builder.append(message);
        String translatableString = builder.toString();
        return translatableString;
    }

    /**
     * Check for events if the item is the correct one
     * @param stack
     * @param itemID
     * @return
     */
    public static boolean passedItemChecks(ItemStack stack, String itemID){
        if(stack == null || !stack.hasItemMeta() ||
                !stack.getItemMeta().getPersistentDataContainer().has(RandomUtils.nskContainer.get("ItemCatalogID")) ||
        !stack.getItemMeta().getPersistentDataContainer().get(RandomUtils.nskContainer.get("ItemCatalogID"), PersistentDataType.STRING).equals(itemID))
            return false;

        return true;
    }

    public static boolean checkCooldown(Player player, double cooldownSeconds, Map<UUID, Double> cooldowns) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            double lastUse = cooldowns.get(player.getUniqueId());
            double currentTime = System.currentTimeMillis() / 1000.0; // Convert to seconds

            if (currentTime - lastUse < cooldownSeconds) {
                int timeLeft = (int) (cooldownSeconds - (currentTime - lastUse));
                player.sendMessage(RandomUtils.color("&cAbility Still has " + timeLeft + "s left of the cooldown!"));
                return false;
            }
        }

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() / 1000.0);
        return true;
    }

    public static boolean checkCooldownNoSpam(Player player, double cooldownSeconds, Map<UUID, Double> cooldowns) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            double lastUse = cooldowns.get(player.getUniqueId());
            double currentTime = System.currentTimeMillis() / 1000.0; // Convert to seconds

            if (currentTime - lastUse < cooldownSeconds) {
                return false;
            }
        }

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() / 1000.0);
        return true;
    }

    public static ItemStack getSmeltingResult(ItemStack stack) {
        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();

            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                if (furnaceRecipe.getInputChoice().test(stack)) {
                    return furnaceRecipe.getResult().clone(); // return the smelted item
                }
            }
        }
        return null; // no smelting recipe found
    }
}
