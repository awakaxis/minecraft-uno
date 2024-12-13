package net.awakaxis.uno;

import net.awakaxis.uno.entity.PlayingDeck;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class UNOEntities {

    public static final EntityType<PlayingDeck> PLAYING_DECK_ENTITY_TYPE = FabricEntityTypeBuilder.create(MobCategory.MISC, PlayingDeck::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build();

    public static void register() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, UNO.id("deck"), PLAYING_DECK_ENTITY_TYPE);
        UNO.LOGGER.info("Registered Entity Types");
    }
}
