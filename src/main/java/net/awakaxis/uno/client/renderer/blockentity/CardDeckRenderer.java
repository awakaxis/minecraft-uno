package net.awakaxis.uno.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.awakaxis.uno.UNO;
import net.awakaxis.uno.block.CardDeckBlock;
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
    public static final ResourceLocation CARD_DECK_TEXTURE_SIDE = UNO.id("textures/entity/card_deck_side.png");
    private static final float CARD_THICKNESS = 0.007f;
    private static final float CARD_FACTOR = 5.0f;

    public CardDeckRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CardDeckBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (blockEntity.getCardCount() <= 0) return;

        float height = CARD_THICKNESS * Math.max(Math.round((blockEntity.getCardCount() / CARD_FACTOR)) / 2.0f, 0.5f);

        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE));
        poseStack.pushPose();

        // TODO someday: translate with blockstate (i wanna make it so you can place the deck on the left or right side of the block face, rather than just the middle)
        switch (blockEntity.getBlockState().getValue(CardDeckBlock.FACING)) {
            case EAST -> poseStack.rotateAround(Axis.YN.rotationDegrees(90f), 0.5f, 0, 0.5f);
            case SOUTH -> poseStack.rotateAround(Axis.YN.rotationDegrees(180f), 0.5f, 0, 0.5f);
            case WEST -> poseStack.rotateAround(Axis.YN.rotationDegrees(270f), 0.5f, 0, 0.5f);
        }
        Matrix4f affine = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        //TODO: replace hardcoded vertex coordinates with variables so that implementing blockstates is easier

        // top face
        this.vertex(affine, normal, vertexConsumer, 0.375f, height, 0.312f, 0, 0, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, height, 0.687f, 0, 1, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, height, 0.687f, 1, 1, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, height, 0.312f, 1, 0, 0, 1, 0, i);

        vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE_SIDE));

        // round to x, x + 0.5, or x + 1 because the card side texture is actually two cards on top of each other, so the value needs to be in terms of 0.5
        // hawk tuah
        final float VALUE = Math.max(Math.round(blockEntity.getCardCount() / CARD_FACTOR) / 2.0f, 0.5f);
        final float UV_NY = (int) VALUE < VALUE ? 0.5f : 0.0F;
        final float UV_PY = (int) VALUE < VALUE ? VALUE + 0.5f : VALUE;

        // northern face
        this.vertex(affine, normal, vertexConsumer, 0.625f, height, 0.312f, 0, UV_NY, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0f, 0.312f, 0, UV_PY, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0f, 0.312f, 1, UV_PY, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, height, 0.312f, 1, UV_NY, 0, 0, -1, i);

        // eastern face
        this.vertex(affine, normal, vertexConsumer, 0.625f, height, 0.687f, 0, UV_NY, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.687f, 0, UV_PY, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.312f, 1, UV_PY, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, height, 0.312f, 1, UV_NY, 1, 0, 0, i);

        // southern face
        this.vertex(affine, normal, vertexConsumer, 0.375f, height, 0.687f, 0, UV_NY, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.687f, 0, UV_PY, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.687f, 1, UV_PY, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, height, 0.687f, 1, UV_NY, 0, 0, 1, i);

        // western face
        this.vertex(affine, normal, vertexConsumer, 0.375f, height, 0.312f, 0, UV_NY, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.312f, 0, UV_PY, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.687f, 1, UV_PY, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, height, 0.687f, 1, UV_NY, -1, 0, 0, i);

        // TODO: bottom face
        poseStack.popPose();
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float i, float j, float k, float f, float g, int l, int m, int n, int o) {
        vertexConsumer.vertex(matrix4f, i, j, k)
                .color(255, 255, 255, 255)
                .uv(f, g)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(o)
                .normal(matrix3f, (float)l, (float)m, (float)n)
                .endVertex();
    }
}
