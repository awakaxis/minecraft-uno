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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayingDeck extends Entity {

    public static final EntityDataAccessor<CompoundTag> DECK_CONTENTS_ID = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Long> CARD_PLACEMENT_SEED = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.LONG);
    public static final String CARD_ROTS_TAG = "cardRotations";
    public static final String CARD_STACK_TAG = "cardStack";

    private long lastHit;

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

    public int popCard() {
        if (this.level().isClientSide) return -1;

        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(CARD_STACK_TAG, Tag.TAG_INT);
        ListTag cardRots = deckContents.getList(CARD_ROTS_TAG, Tag.TAG_INT);
        if (cardStack.isEmpty() || cardRots.isEmpty()) {
            if (!(cardStack.isEmpty() && cardRots.isEmpty())) {
                UNO.LOGGER.warn("Desync in cardStack and cardRots");
            }
            return -1;
        }

        int i = ((IntTag)cardStack.remove(cardStack.size() - 1)).getAsInt();
        cardRots.remove(cardRots.size() - 1);

        this.entityData.set(DECK_CONTENTS_ID, deckContents, true);
        return i;
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (this.level().isClientSide) {
            return InteractionResult.sidedSuccess(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty());
        } else {
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (!itemStack.is(UNOItems.UNO_CARD)) {
                if (itemStack.isEmpty()) {
                    int i = popCard();
                    ItemStack itemStack2 = ((UnoCardItem)UNOItems.UNO_CARD).getWithIndex(i);
                    if (!player.getInventory().add(player.getInventory().selected, itemStack2)) {
                        player.drop(itemStack2, false);
                    }
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }

            this.pushCard(player, itemStack.getOrCreateTag().getInt(UnoCardItem.CARD_INDEX_TAG));
            itemStack.shrink(1);

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        boolean bl = damageSource.getDirectEntity() instanceof AbstractArrow;
        if (damageSource.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
            return false;
        }
        if (damageSource.isCreativePlayer()) {
            this.kill();
            return true;
        } else {
            long l = this.level().getGameTime();
            if (l - this.lastHit > 5L && !bl) {
                this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
                this.lastHit = l;
            } else {
                this.kill();
            }
            return true;
        }

    }

    @Override
    public void kill() {
        if (!this.level().isClientSide) {
            CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
            ListTag cardStack = deckContents.getList(CARD_STACK_TAG, Tag.TAG_INT);
            cardStack.forEach(tag -> {
                IntTag intTag = (IntTag) tag;
                ItemEntity itemEntity = new ItemEntity(this.level(), (double) this.blockPosition().getX() + 0.5, (double) this.blockPosition().getY() + 0.5, (double) this.blockPosition().getZ() + 0.5, ((UnoCardItem)UNOItems.UNO_CARD).getWithIndex(intTag.getAsInt()));
                itemEntity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itemEntity);
            });
        }
        super.kill();
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
    public @Nullable ItemStack getPickResult() {
        CompoundTag deckContents = this.entityData.get(DECK_CONTENTS_ID);
        ListTag cardStack = deckContents.getList(CARD_STACK_TAG, Tag.TAG_INT);
        return ((UnoCardItem)UNOItems.UNO_CARD).getWithIndex(((IntTag)cardStack.get(cardStack.size() - 1)).getAsInt());
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
