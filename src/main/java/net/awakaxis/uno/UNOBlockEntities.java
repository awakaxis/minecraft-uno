package net.awakaxis.uno;

import net.awakaxis.uno.block.CardDeckBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class UNOBlockEntities {
    public static final BlockEntityType<CardDeckBlockEntity> CARD_DECK_BLOCK_ENTITY_TYPE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, UNO.id("card_deck"), FabricBlockEntityTypeBuilder.create(CardDeckBlockEntity::new, UNOBlocks.CARD_DECK).build());

    public static void register() {
        UNO.LOGGER.info("Registered Block Entities");
    }
}
