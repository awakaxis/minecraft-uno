package net.awakaxis.uno.client.renderer.entity;

import net.awakaxis.uno.entity.PlayingDeck;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PlayingDeckRenderer extends EntityRenderer<PlayingDeck> {
    public PlayingDeckRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(PlayingDeck entity) {
        return null;
    }
}
