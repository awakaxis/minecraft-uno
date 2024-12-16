package net.awakaxis.uno;

import net.awakaxis.uno.item.CardDeckBlockItem;
import net.awakaxis.uno.item.UnoCardItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UNOItems {

    public static final ResourceKey<CreativeModeTab> UNO_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), UNO.id("item_group"));
    public static final CreativeModeTab UNO_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(UNOBlocks.CARD_DECK.asItem()))
            .title(Component.translatable("itemGroup.uno"))
            .build();

    // I should split this up into multiple lines, but in the words of Terry A. Davis, "I am the smartest programmer that has ever lived"
    // yummy edible uno cards!
    public static final Item UNO_CARD = Registry.register(BuiltInRegistries.ITEM, UNO.id("card"), new UnoCardItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().fast().alwaysEat().effect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 0.25f).effect(new MobEffectInstance(MobEffects.POISON, 100, 1), 0.75f).build())));
    public static final Item CARD_DECK_BLOCK_ITEM = Registry.register(BuiltInRegistries.ITEM, UNO.id("card_deck"), new CardDeckBlockItem(UNOBlocks.CARD_DECK, new Item.Properties().stacksTo(1)));

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, UNO_ITEM_GROUP_KEY, UNO_ITEM_GROUP);
        UNO.LOGGER.info("Registered Items");
    }
}
