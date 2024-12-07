package net.awakaxis.uno.entity;

import net.awakaxis.uno.UNO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PlayingDeck extends Entity {

    private static final EntityDataAccessor<CompoundTag> DECK_CONTENTS_ID = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.COMPOUND_TAG);
    public static final String DECK_STACK_TAG = "cardStack";

    public PlayingDeck(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Environment(EnvType.SERVER)
    public void pushCard(int cardIndex) {
        if (this.level().isClientSide) return;

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(DECK_STACK_TAG, Tag.TAG_INT);
        cardStack.set(cardStack.size(), IntTag.valueOf(cardIndex));

        this.entityData.set(DECK_CONTENTS_ID, deckContents);
    }

    @Environment(EnvType.SERVER)
    public void popCard() {
        if (this.level().isClientSide) return;

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(DECK_STACK_TAG, Tag.TAG_INT);
        if (cardStack.isEmpty()) return;

        cardStack.remove(cardStack.size() - 1);

        this.entityData.set(DECK_CONTENTS_ID, deckContents);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        UNO.LOGGER.info("Testing");
        if (this.level().isClientSide) {
            player.displayClientMessage(Component.nullToEmpty("CLIENT: tag is: " + this.entityData.get(DECK_CONTENTS_ID)), false);
            return InteractionResult.SUCCESS;
        } else {
            this.pushCard(18);
            player.displayClientMessage(Component.nullToEmpty("SERVER: tag is: " + this.entityData.get(DECK_CONTENTS_ID)), false);
            return InteractionResult.SUCCESS;
        }
    }


    @Override
    public void defineSynchedData() {
        this.entityData.define(DECK_CONTENTS_ID, new CompoundTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {}

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {}
}
