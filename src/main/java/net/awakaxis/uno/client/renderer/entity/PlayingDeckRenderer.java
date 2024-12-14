package net.awakaxis.uno.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.awakaxis.uno.UNOItems;
import net.awakaxis.uno.entity.PlayingDeck;
import net.awakaxis.uno.item.UnoCardItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class PlayingDeckRenderer extends EntityRenderer<PlayingDeck> {
    private final ItemRenderer itemRenderer;

    public PlayingDeckRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PlayingDeck playingDeck, float f, float g, PoseStack matrices, MultiBufferSource multiBufferSource, int i) {

        RandomSource randomSource = RandomSource.create(playingDeck.getEntityData().get(PlayingDeck.CARD_PLACEMENT_SEED));

        ItemStack itemStack = ((UnoCardItem) UNOItems.UNO_CARD).getWithIndex(18);

        matrices.pushPose();
        Matrix4f affine = matrices.last().pose();

        // transform affine matrix with rotation to make the card flat, and scale to make the card look not massive
        affine.translate(0f, 0f, 0.05f);
        affine.rotate(Axis.XP.rotationDegrees(90f));
        affine.rotate(Axis.ZN.rotationDegrees((randomSource.nextFloat() * 90f) - 45f));
        affine.scale(0.5f, 0.5f, 0.5f);

        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, i, OverlayTexture.NO_OVERLAY, matrices, multiBufferSource, playingDeck.level(), playingDeck.getId());

        matrices.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(PlayingDeck entity) {
        return null;
    }
}
