package org.realgotqkura.itemCatalog.items;

import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.realgotqkura.itemCatalog.ItemCatalog;
import org.realgotqkura.itemCatalog.items.combat.*;
import org.realgotqkura.itemCatalog.items.misc.*;
import org.realgotqkura.itemCatalog.utilities.ItemStackComparators;

import java.util.ArrayList;
import java.util.List;

public class ItemsManager {

    public static int ITEM_COUNT;

    public static List<ItemStack> combatItems = new ArrayList<>();
    public static List<ItemStack> miscItems = new ArrayList<>();
    public static List<ItemStack> allItems = new ArrayList<>();

    public static void loadItems(){
        loadCombatItems();
        loadMiscItems();
        allItems.addAll(combatItems);
        allItems.addAll(miscItems);

        allItems.sort(ItemStackComparators.BY_DISPLAY_NAME);
        ITEM_COUNT = allItems.size();
    }

    public static void loadRunnables(ItemCatalog plugin){
        ItemMagnet.runnable(plugin);
        ChameleonCloak.runnable(plugin);
    }

    private static void loadCombatItems(){
        combatItems.add(HeavySword.item());
        combatItems.add(EnderSword.item());
        combatItems.add(VampireDagger.item());
        combatItems.add(SnowWand.item());
        combatItems.add(EndermanSword.item());
        combatItems.add(GuardianShield.item());
        combatItems.add(ZephyrBow.item());
        combatItems.add(InfernoStaff.item());
        combatItems.add(PoisonFang.item());
        combatItems.add(Stormbringer.item());
        combatItems.add(GolemUnderwear.item());
        combatItems.add(FlameSword.item());
        combatItems.add(SoulScythe.item(null));
        combatItems.add(EchoBow.item());
        combatItems.add(DoubleEdgedSword.item());
        combatItems.add(SandstormDagger.item());
        combatItems.add(EchoBlade.item());
        combatItems.add(GlacialBow.item());
        combatItems.add(FrostHammer.item());
        combatItems.add(ChainLightningStaff.item());
    }

    private static void loadMiscItems(){
        miscItems.add(RabbitBoots.item());
        miscItems.add(HastyPickaxe.item());
        miscItems.add(LumberjackAxe.item());
        miscItems.add(TorchOfLight.item());
        miscItems.add(SpeedyBoots.item());
        miscItems.add(HarvesterHoe.item());
        miscItems.add(TurtHelmet.item());
        miscItems.add(ChickenWings.item());
        miscItems.add(PrimitiveShadowWand.item());
        miscItems.add(ChronoClock.item());
        miscItems.add(ItemMagnet.item());
        miscItems.add(GiantHammer.item());
        miscItems.add(TempestTrident.item());
        miscItems.add(HuntersNet.item());
        miscItems.add(VolcanicPickaxe.item());
        miscItems.add(ChameleonCloak.item());
        miscItems.add(DruidStaff.item());
        miscItems.add(MetalDetector.item());
        miscItems.add(WindlashWhip.item());
        miscItems.add(CursedCoin.item());
        miscItems.add(SpiritLantern.item());
        miscItems.add(SiphonGauntlet.item());
        miscItems.add(PhoenixFeather.item());
    }


    public static void loadItemEvents(ItemCatalog plugin){
        plugin.getServer().getPluginManager().registerEvents(new PhoenixFeather(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CursedCoin(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EchoBlade(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TempestTrident(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SandstormDagger(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HeavySword(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EnderSword(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VampireDagger(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new RabbitBoots(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HastyPickaxe(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SnowWand(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new LumberjackAxe(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TorchOfLight(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EndermanSword(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GuardianShield(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ZephyrBow(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SpeedyBoots(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InfernoStaff(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HarvesterHoe(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TurtHelmet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PoisonFang(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new Stormbringer(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChickenWings(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PrimitiveShadowWand(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GolemUnderwear(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FlameSword(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChronoClock(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SoulScythe(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemMagnet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EchoBow(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DoubleEdgedSword(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GiantHammer(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HuntersNet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VolcanicPickaxe(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChameleonCloak(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DruidStaff(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MetalDetector(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GlacialBow(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FrostHammer(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChainLightningStaff(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SiphonGauntlet(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new WindlashWhip(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new SpiritLantern(plugin), plugin);
    }

}
