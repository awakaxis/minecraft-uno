package net.awakaxis.uno.entity;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PlayingDeck extends Entity {

    public PlayingDeck(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void defineSynchedData() {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {}

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {}
}
