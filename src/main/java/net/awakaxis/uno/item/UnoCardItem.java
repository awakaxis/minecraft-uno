package net.awakaxis.uno.item;

import net.awakaxis.uno.UNOEntities;
import net.awakaxis.uno.entity.PlayingDeck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UnoCardItem extends Item {

    public static final String CARD_INDEX_TAG = "cardIndex";

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = super.getDefaultInstance();
        itemStack.getOrCreateTag().putInt(CARD_INDEX_TAG, 0);
        return itemStack;
    }

    public ItemStack getWithIndex(int index) {
        ItemStack itemStack = getDefaultInstance();
        itemStack.getOrCreateTag().putInt(CARD_INDEX_TAG, index);
        return itemStack;
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        int i = itemStack.getOrCreateTag().getInt(CARD_INDEX_TAG);
        return super.getDescriptionId() + "." + i;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        } else {
            BlockPos hitPos = useOnContext.getClickedPos();
            BlockState state = serverLevel.getBlockState(hitPos);

            if (useOnContext.getClickedFace() != Direction.UP || state.getCollisionShape(serverLevel, hitPos).isEmpty()) return InteractionResult.SUCCESS;

            ItemStack itemStack = useOnContext.getItemInHand();
            Vec3 hitLocation = useOnContext.getClickLocation();

            EntityType<PlayingDeck> entityType = UNOEntities.PLAYING_DECK_ENTITY_TYPE;
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("id", EntityType.getKey(entityType).toString());

            // why is creating entities so cancer, this shit took like an hour to figure out
            PlayingDeck playingDeck = (PlayingDeck) EntityType.create(compoundTag, serverLevel).orElseThrow();
            playingDeck.moveTo(hitLocation);
            playingDeck.pushCard(useOnContext.getPlayer(), itemStack.getOrCreateTag().getInt(CARD_INDEX_TAG));
            serverLevel.addFreshEntityWithPassengers(playingDeck);

            itemStack.shrink(1);

            return InteractionResult.CONSUME;
        }
    }

    public UnoCardItem(Item.Properties properties) {
        super(properties);
    }
}
