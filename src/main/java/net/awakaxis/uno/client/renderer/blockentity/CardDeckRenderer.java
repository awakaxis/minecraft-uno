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
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CardDeckRenderer implements BlockEntityRenderer<CardDeckBlockEntity> {
    public static final ResourceLocation CARD_DECK_TEXTURE = UNO.id("textures/entity/card_deck.png");
    public static final ResourceLocation CARD_DECK_TEXTURE_SIDE = UNO.id("textures/entity/card_deck_side.png");
    public static int CARD_COUNT = 112;
    private static final float CARD_THICKNESS = 0.007f;
    // the model looks stupid if there are too many visible cards

    public CardDeckRenderer(BlockEntityRendererProvider.Context context) {

    }

    private static float getHeight() {
        return CARD_THICKNESS * Math.round(CARD_COUNT / 10.0f) / 2.0f;
    }

    @Override
    public void render(CardDeckBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE));
        poseStack.pushPose();

        // TODO: rotate with blockstate
        // also, translate with blockstate (i wanna make it so you can place the deck on the left or right side of the block face, rather than just the middle)
//        poseStack.rotateAround(Axis.YP.rotationDegrees(90), 0.5f, 0, 0.5f);
        Matrix4f affine = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // top face
        this.vertex(affine, normal, vertexConsumer, 0.375f, getHeight(), 0.312f, 0, 0, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, getHeight(), 0.687f, 0, 1, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, getHeight(), 0.687f, 1, 1, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, getHeight(), 0.312f, 1, 0, 0, 1, 0, i);

        vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE_SIDE));

        // round to x, x + 0.5, or x + 1 because the card side texture is actually two cards on top of each other, so the value needs to be in terms of 0.5
        // hawk tuah
        final float VALUE = Math.round(CARD_COUNT / 10.0f) / 2.0f;
        final float UV_NY = (int) VALUE < VALUE ? 0.5f : 0.0F;
        final float UV_PY = (int) VALUE < VALUE ? VALUE + 0.5f : VALUE;

        // northern face
        this.vertex(affine, normal, vertexConsumer, 0.625f, getHeight(), 0.312f, 0, UV_NY, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0f, 0.312f, 0, UV_PY, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0f, 0.312f, 1, UV_PY, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, getHeight(), 0.312f, 1, UV_NY, 0, 0, -1, i);

        // eastern face
        this.vertex(affine, normal, vertexConsumer, 0.625f, getHeight(), 0.687f, 0, UV_NY, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.687f, 0, UV_PY, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.312f, 1, UV_PY, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, getHeight(), 0.312f, 1, UV_NY, 1, 0, 0, i);

        // southern face
        this.vertex(affine, normal, vertexConsumer, 0.375f, getHeight(), 0.687f, 0, UV_NY, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.687f, 0, UV_PY, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.687f, 1, UV_PY, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, getHeight(), 0.687f, 1, UV_NY, 0, 0, 1, i);

        // western face
        this.vertex(affine, normal, vertexConsumer, 0.375f, getHeight(), 0.312f, 0, UV_NY, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.312f, 0, UV_PY, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.687f, 1, UV_PY, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, getHeight(), 0.687f, 1, UV_NY, -1, 0, 0, i);

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

    @Override
    public boolean shouldRender(CardDeckBlockEntity blockEntity, Vec3 vec3) {
        return true;
    }
}
