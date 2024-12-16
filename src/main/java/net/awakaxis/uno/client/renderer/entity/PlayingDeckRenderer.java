package net.awakaxis.uno.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.awakaxis.uno.UNOItems;
import net.awakaxis.uno.entity.PlayingDeck;
import net.awakaxis.uno.item.UnoCardItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PlayingDeckRenderer extends EntityRenderer<PlayingDeck> {
    private final ItemRenderer itemRenderer;

    public PlayingDeckRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PlayingDeck playingDeck, float f, float g, PoseStack matrices, MultiBufferSource multiBufferSource, int i) {

        if (playingDeck.getEntityData().get(PlayingDeck.DECK_CONTENTS_ID).isEmpty()) return;

        RandomSource randomSource = RandomSource.create(playingDeck.getEntityData().get(PlayingDeck.CARD_PLACEMENT_SEED));

        // push for basic transforms (making the card properly sized and also lying on the ground flat)
        matrices.pushPose();

        Matrix4f affine = matrices.last().pose();
        Matrix3f normal = matrices.last().normal();

        // transform affine matrix with rotation to make the card flat, and scale to make the card look not massive
        affine.translate(0f, 0f, 0.05f);
        affine.rotate(Axis.XP.rotationDegrees(90f));
        normal.rotate(Axis.XP.rotationDegrees(90f));
        affine.scale(.4f, .4f, 0.05f);

        CompoundTag deckData = playingDeck.getEntityData().get(PlayingDeck.DECK_CONTENTS_ID);
        ListTag cards = deckData.getList(PlayingDeck.DECK_STACK_TAG, Tag.TAG_INT);
        ListTag cardRots = deckData.getList(PlayingDeck.DECK_ROTS_TAG, Tag.TAG_INT);

        for (int j = 0; j < cards.size(); j ++) {
            // push for card specifics (slight rotation, height in deck)
            matrices.pushPose();

            int k = ((IntTag)cards.get(j)).getAsInt();
            ItemStack itemStack = ((UnoCardItem) UNOItems.UNO_CARD).getWithIndex(k);
            Matrix4f affine2 = matrices.last().pose();
            Matrix3f normal2 = matrices.last().normal();

            float cardRot = ((IntTag)cardRots.get(j)).getAsInt();
            float modifier = (randomSource.nextFloat() * 30f) - 15f;
            affine2.rotate(Axis.ZP.rotationDegrees(cardRot));
            affine2.rotate(Axis.ZP.rotationDegrees(modifier));
            normal2.rotate(Axis.ZP.rotationDegrees(cardRot));
            normal2.rotate(Axis.ZP.rotationDegrees(modifier));

            affine2.translate(0, 0, -j * 0.06f);

            this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, i, OverlayTexture.NO_OVERLAY, matrices, multiBufferSource, playingDeck.level(), playingDeck.getId());

            matrices.popPose();
        }

        matrices.popPose();
    }

//    @Override
//    public boolean shouldRender(PlayingDeck entity, Frustum frustum, double d, double e, double f) {
//        return true;
//    }

    @Override
    public ResourceLocation getTextureLocation(PlayingDeck entity) {
        return null;
    }
}
