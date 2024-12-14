package net.awakaxis.uno.entity;

import net.awakaxis.uno.UNO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayingDeck extends Entity {

    private static final RandomSource RANDOM = RandomSource.create();
    public static final EntityDataAccessor<CompoundTag> DECK_CONTENTS_ID = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Long> CARD_PLACEMENT_SEED = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.LONG);
    public static final String DECK_STACK_TAG = "cardStack";

    public PlayingDeck(EntityType<?> entityType, Level level) {
        super(entityType, level);

    }

    public void pushCard(int cardIndex) {
        if (this.level().isClientSide) return;

        UNO.LOGGER.info("pushing card");

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(DECK_STACK_TAG, Tag.TAG_INT);
        cardStack.add(IntTag.valueOf(cardIndex));

        this.entityData.set(DECK_CONTENTS_ID, deckContents, true);
    }

    public void popCard() {
        if (this.level().isClientSide) return;

        UNO.LOGGER.info("popping card");

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(DECK_STACK_TAG, Tag.TAG_INT);
        if (cardStack.isEmpty()) return;

        cardStack.remove(cardStack.size() - 1);

        this.entityData.set(DECK_CONTENTS_ID, deckContents);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (this.level().isClientSide) {
            player.displayClientMessage(Component.nullToEmpty("CLIENT: tag is: " + this.entityData.get(DECK_CONTENTS_ID)), false);
            return InteractionResult.CONSUME;
        } else {
            this.pushCard(18);
            player.displayClientMessage(Component.nullToEmpty("SERVER: tag is: " + this.entityData.get(DECK_CONTENTS_ID)), false);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void defineSynchedData() {
        this.entityData.define(DECK_CONTENTS_ID, new CompoundTag());
        this.entityData.define(CARD_PLACEMENT_SEED, RANDOM.nextLong());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag2.put(DECK_STACK_TAG, compoundTag.getList(DECK_STACK_TAG, Tag.TAG_INT));
        this.entityData.set(DECK_CONTENTS_ID, compoundTag2);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(DECK_STACK_TAG, this.entityData.get(DECK_CONTENTS_ID).getList(DECK_STACK_TAG, Tag.TAG_INT));
    }
}
