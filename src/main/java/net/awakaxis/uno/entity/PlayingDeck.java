package net.awakaxis.uno.entity;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PlayingDeck extends Entity {

    private static final EntityDataAccessor<CompoundTag> DECK_CONTENTS_ID = SynchedEntityData.defineId(PlayingDeck.class, EntityDataSerializers.COMPOUND_TAG);

    public PlayingDeck(EntityType<?> entityType, Level level) {
        super(entityType, level);
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
