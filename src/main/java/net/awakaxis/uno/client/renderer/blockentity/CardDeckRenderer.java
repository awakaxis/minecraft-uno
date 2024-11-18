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
    private static final float CARD_THICKNESS = 0.007f;
    private static final float HEIGHT = .1f;
    private static final float CARD_COUNT = HEIGHT / CARD_THICKNESS;

    public CardDeckRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CardDeckBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE));
        poseStack.pushPose();
//        poseStack.rotateAround(Axis.YP.rotationDegrees(90), 0.5f, 0, 0.5f);
        Matrix4f affine = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        // top face
        this.vertex(affine, normal, vertexConsumer, 0.375f, HEIGHT, 0.312f, 0, 0, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, HEIGHT, 0.687f, 0, 1, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, HEIGHT, 0.687f, 1, 1, 0, 1, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, HEIGHT, 0.312f, 1, 0, 0, 1, 0, i);

        vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(CARD_DECK_TEXTURE_SIDE));

        // northern face
        this.vertex(affine, normal, vertexConsumer, 0.625f, HEIGHT, 0.312f, 0, 0, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0f, 0.312f, 0, CARD_COUNT, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0f, 0.312f, 1, CARD_COUNT, 0, 0, -1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, HEIGHT, 0.312f, 1, 0, 0, 0, -1, i);

        // eastern face
        this.vertex(affine, normal, vertexConsumer, 0.625f, HEIGHT, 0.687f, 0, 0, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.687f, 0, CARD_COUNT, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.312f, 1, CARD_COUNT, 1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, HEIGHT, 0.312f, 1, 0, 1, 0, 0, i);

        // southern face
        this.vertex(affine, normal, vertexConsumer, 0.375f, HEIGHT, 0.687f, 0, 0, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.687f, 0, CARD_COUNT, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, 0, 0.687f, 1, CARD_COUNT, 0, 0, 1, i);
        this.vertex(affine, normal, vertexConsumer, 0.625f, HEIGHT, 0.687f, 1, 0, 0, 0, 1, i);

        // western face
        this.vertex(affine, normal, vertexConsumer, 0.375f, HEIGHT, 0.312f, 0, 0, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.312f, 0, CARD_COUNT, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, 0, 0.687f, 1, CARD_COUNT, -1, 0, 0, i);
        this.vertex(affine, normal, vertexConsumer, 0.375f, HEIGHT, 0.687f, 1, 0, -1, 0, 0, i);

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
