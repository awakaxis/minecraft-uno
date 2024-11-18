package net.awakaxis.uno;

import net.awakaxis.uno.block.CardDeckBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class UNOBlocks {
    public static final Block CARD_DECK = registerWithItem(new CardDeckBlock(BlockBehaviour.Properties.copy(Blocks.STONE)), "card_deck", new Item.Properties().stacksTo(1));

    private static Block registerWithItem(Block block, String id, Item.Properties properties) {
        ResourceLocation resourceLocation = UNO.id(id);
        Block block2 = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, new BlockItem(block, properties));
        return block2;
    }

    public static void register() {
        UNO.LOGGER.info("Registered Blocks");
    }
}
