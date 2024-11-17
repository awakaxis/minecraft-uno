package net.awakaxis.uno.client;

import net.awakaxis.uno.UNOBlockEntities;
import net.awakaxis.uno.client.renderer.blockentity.CardDeckRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class UNOClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(UNOBlockEntities.CARD_DECK_BLOCK_ENTITY_TYPE, CardDeckRenderer::new);
    }
}
