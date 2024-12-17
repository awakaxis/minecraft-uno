package net.awakaxis.uno.client;

import net.awakaxis.uno.UNOBlockEntities;
import net.awakaxis.uno.UNOEntities;
import net.awakaxis.uno.UNOItems;
import net.awakaxis.uno.client.renderer.blockentity.CardDeckRenderer;
import net.awakaxis.uno.client.renderer.entity.PlayingDeckRenderer;
import net.awakaxis.uno.item.UnoCardItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class UNOClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(UNOBlockEntities.CARD_DECK_BLOCK_ENTITY_TYPE, CardDeckRenderer::new);
        EntityRendererRegistry.register(UNOEntities.PLAYING_DECK_ENTITY_TYPE, PlayingDeckRenderer::new);

        // fuck you, *unclamps your ClampedItemPropertyFunction*
        ClampedItemPropertyFunction function = new ClampedItemPropertyFunction() {
            @Override
            public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                if (livingEntity != null && !livingEntity.equals(Minecraft.getInstance().player)) {
                    return 0;
                }
                return itemStack.getOrCreateTag().getInt(UnoCardItem.CARD_INDEX_TAG);
            }

            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return 0;
            }
        };

        ItemProperties.register(UNOItems.UNO_CARD, new ResourceLocation("card_index"), function);
    }
}
