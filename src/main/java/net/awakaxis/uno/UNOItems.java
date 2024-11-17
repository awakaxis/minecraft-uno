package net.awakaxis.uno;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

import java.awt.*;

public class UNOItems {

    public static final ResourceKey<CreativeModeTab> UNO_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), UNO.id("item_group"));
    public static final CreativeModeTab UNO_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(UNOBlocks.CARD_DECK_BLOCK.asItem()))
            .title(Component.translatable("itemGroup.uno"))
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, UNO_ITEM_GROUP_KEY, UNO_ITEM_GROUP);
        UNO.LOGGER.info("Registered Items");
    }
}
