package net.awakaxis.uno.block;

import net.awakaxis.uno.UNOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CardDeckBlockEntity extends BlockEntity {

    // TODO: do something like EntityDataAccessor for this block entity somehow idk man

    public CardDeckBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public CardDeckBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(UNOBlockEntities.CARD_DECK_BLOCK_ENTITY_TYPE, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CardDeckBlockEntity cardDeckBlockEntity) {
    }
}

