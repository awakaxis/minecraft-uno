package net.awakaxis.uno.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.awakaxis.uno.UNO;
import net.awakaxis.uno.block.CardDeckBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CardDeckRenderer implements BlockEntityRenderer<CardDeckBlockEntity> {
    public static final ResourceLocation CARD_DECK_TEXTURE = UNO.id("textures/entity/card_deck.png");

    public CardDeckRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CardDeckBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE));

        poseStack.pushPose();
        Matrix4f affine = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        this.vertex(affine, normal, vertexConsumer, 1, 0, 1, 0, 0, 0, 1, 0, 0);
        this.vertex(affine, normal, vertexConsumer, 1, 0, 0, 0, 1, 0, 1, 0, 0);
        this.vertex(affine, normal, vertexConsumer, 0, 0, 0, 1, 1, 0, 1, 0, 0);
        this.vertex(affine, normal, vertexConsumer, 0, 0, 1, 1, 0, 0, 1, 0, 0);
        poseStack.popPose();
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int i, int j, int k, float f, float g, int l, int m, int n, int o) {
        vertexConsumer.vertex(matrix4f, (float)i, (float)j, (float)k)
                .color(255, 255, 255, 255)
                .uv(f, g)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(o)
                .normal(matrix3f, (float)l, (float)m, (float)n)
                .endVertex();
    }
}
