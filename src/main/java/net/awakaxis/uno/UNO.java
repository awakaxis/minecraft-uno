package net.awakaxis.uno;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNO implements ModInitializer {
	public static final String MOD_ID = "uno";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		UNOBlocks.register();
		UNOBlockEntities.register();
		UNOItems.register();

		ItemGroupEvents.modifyEntriesEvent(UNOItems.UNO_ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.accept(UNOBlocks.CARD_DECK_BLOCK.asItem());
		});

		LOGGER.info("Hello Queers!");
	}
}