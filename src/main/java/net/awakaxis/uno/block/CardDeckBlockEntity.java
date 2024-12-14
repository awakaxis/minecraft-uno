package net.awakaxis.uno.block;

import net.awakaxis.uno.UNO;
import net.awakaxis.uno.UNOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CardDeckBlockEntity extends BlockEntity {

    public static final String CARD_COUNT_TAG = "cardCount";
    public static final String CARD_SEED_TAG = "cardSeed";

    private int cardCount = 112;

    public CardDeckBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public CardDeckBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(UNOBlockEntities.CARD_DECK_BLOCK_ENTITY_TYPE, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CardDeckBlockEntity cardDeckBlockEntity) {
    }

    public int getCardCount() {
        return this.cardCount;
    }

    public void decrementCardCount() {
        if (cardCount > 0) {
            this.cardCount--;
            this.setChanged();
        }
    }

    public void resetCardCount() {
        this.cardCount = 112;
        this.setChanged();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        // forgor about doing this for a minute or two there ahah
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        super.setChanged();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(CARD_COUNT_TAG, cardCount);
        return compoundTag;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt(CARD_COUNT_TAG, cardCount);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(CARD_COUNT_TAG)) {
            cardCount = compoundTag.getInt(CARD_COUNT_TAG);
        }
        super.load(compoundTag);
    }
}

