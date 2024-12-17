package net.awakaxis.uno.entity;

import net.awakaxis.uno.UNO;
import net.awakaxis.uno.UNOItems;
import net.awakaxis.uno.item.UnoCardItem;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PlayingDeck extends Entity {

    public static final EntityDataAccessor<CompoundTag> DECK_CONTENTS_ID = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Long> CARD_PLACEMENT_SEED = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.LONG);
    public static final String CARD_ROTS_TAG = "cardRotations";
    public static final String CARD_STACK_TAG = "cardStack";

    public PlayingDeck(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void pushCard(Player player, int cardIndex) {
        if (this.level().isClientSide) {
            UNO.LOGGER.warn("Pushing card from client");
            return;
        }

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(CARD_STACK_TAG, Tag.TAG_INT);
        ListTag cardRots = deckContents.getList(CARD_ROTS_TAG, Tag.TAG_INT);

        cardStack.add(IntTag.valueOf(cardIndex));
        cardRots.add(IntTag.valueOf((int) player.getYRot()));

        this.entityData.set(DECK_CONTENTS_ID, deckContents, true);
    }

    public void popCard() {
        if (this.level().isClientSide) return;

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(CARD_STACK_TAG, Tag.TAG_INT);
        ListTag cardRots = deckContents.getList(CARD_ROTS_TAG, Tag.TAG_INT);
        if (cardStack.isEmpty() || cardRots.isEmpty()) {
            if (!(cardStack.isEmpty() && cardRots.isEmpty())) {
                UNO.LOGGER.warn("Desync in cardStack and cardRots");
            }
            return;
        }

        cardStack.remove(cardStack.size() - 1);
        cardRots.remove(cardRots.size() - 1);

        this.entityData.set(DECK_CONTENTS_ID, deckContents, true);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (this.level().isClientSide) {
            return InteractionResult.sidedSuccess(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty());
        } else {
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (itemStack.isEmpty() || !itemStack.is(UNOItems.UNO_CARD)) return InteractionResult.SUCCESS;

            this.pushCard(player, itemStack.getOrCreateTag().getInt(UnoCardItem.CARD_INDEX_TAG));
            itemStack.shrink(1);

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void tick() {
        super.tick();

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(CARD_STACK_TAG, Tag.TAG_INT);
        if (cardStack.isEmpty()) {
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void defineSynchedData() {
        this.entityData.define(DECK_CONTENTS_ID, new CompoundTag());
        this.entityData.define(CARD_PLACEMENT_SEED, RandomSource.create().nextLong());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag2.put(CARD_STACK_TAG, compoundTag.getList(CARD_STACK_TAG, Tag.TAG_INT));
        compoundTag2.put(CARD_ROTS_TAG, compoundTag.getList(CARD_ROTS_TAG, Tag.TAG_INT));
        this.entityData.set(DECK_CONTENTS_ID, compoundTag2);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        CompoundTag deckData = this.entityData.get(DECK_CONTENTS_ID);
        compoundTag.put(CARD_STACK_TAG, deckData.getList(CARD_STACK_TAG, Tag.TAG_INT));
        compoundTag.put(CARD_ROTS_TAG, deckData.getList(CARD_ROTS_TAG, Tag.TAG_INT));
    }
}
