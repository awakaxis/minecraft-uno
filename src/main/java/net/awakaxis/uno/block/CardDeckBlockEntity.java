package net.awakaxis.uno.block;

import net.awakaxis.uno.UNOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CardDeckBlockEntity extends BlockEntity {

    public static final String CARD_COUNT_TAG = "cardCount";
    public static final String CARD_SEED_TAG = "cardSeed";
    public static final String CARD_INVENTORY_TAG = "cards";
    // used to track how many times a card has been pulled from this deck since the last seed change
    // on loading, the RandomSource can be consumed this many times so that the seeded selection wont reset between restarts / pickups and placements
    public static final String DECK_PULL_COUNT_TAG = "deckPulls";

    private RandomSource randomSource;
    private int deckPulls = 0;
    // only matters on the client
    private int cardCount = 0;
    // only matters on the server
    private ArrayList<Integer> cards = null;
    private long cardSeed;

    public CardDeckBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public CardDeckBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(UNOBlockEntities.CARD_DECK_BLOCK_ENTITY_TYPE, blockPos, blockState);
    }

    // unused for now
    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CardDeckBlockEntity cardDeckBlockEntity) {
    }

    public int pullCard() {
//        if (this.cards == null) {
//            populateDeck();
//        }
        this.deckPulls++;
        int i;
        if (this.getCardCount() == 0) {
            return -1;
        } else if (this.getCardCount() == 1) {
            randomSource.nextInt();
            i = this.cards.remove(0);
        } else {
            i = this.cards.remove(randomSource.nextInt(this.cards.size() - 1));
        }
        this.setChanged();
        return i;
    }

    public void putCard(int i) {
        this.cards.add(i);
        this.setChanged();
    }

//    public void onPlace() {
//        if (this.cards == null) {
//            populateDeck();
//        }
//    }

    public void populateDeckIfNeeded() {
        if (this.cards == null) {
            populateDeck();
        }
    }

    // can do params here later for special deck types / deck card skins maybe
    private void populateDeck() {
        ArrayList<Integer> list = new ArrayList<>();

        // four wild cards
        list.add(9);
        list.add(9);
        list.add(9);
        list.add(9);

        // four draw four cards
        list.add(10);
        list.add(10);
        list.add(10);
        list.add(10);

        // TODO: i should re-order the card predicate definitions so that this stuff can be done with only one loop, but im too lazy

        // one of each zero card
        list.add(47);
        list.add(48);
        list.add(49);
        list.add(50);

        // add two of each number card to the deck (11 - 46)
        for (int i = 11; i < 47; i++) {
            list.add(i);
            list.add(i);
        }

        // add two of each non-wild special card (51 - 62)
        for (int i = 51; i < 63; i++) {
            list.add(i);
            list.add(i);
        }

        this.cards = list;
        shuffleDeck();
        this.setChanged();
    }

    public void shuffleDeck() {
        this.cardSeed = RandomSource.create().nextLong();
        this.randomSource = RandomSource.create(this.cardSeed);
        this.deckPulls = 0;
    }

    public int getCardCount() {
        if (this.level.isClientSide) {
            return this.cardCount;
        } else {
//            if (this.cards == null) {
//                populateDeck();
//            }
            return this.cards.size();
        }
    }

    public void createAndDropItemStack() {
        ItemStack itemStack = new ItemStack(this.getBlockState().getBlock());
        this.saveToItem(itemStack);

        ItemEntity itemEntity = new ItemEntity(this.level, (double) this.getBlockPos().getX() + 0.5, (double) this.getBlockPos().getY() + 0.5, (double) this.getBlockPos().getZ() + 0.5, itemStack);
        itemEntity.setDefaultPickUpDelay();
        this.level.addFreshEntity(itemEntity);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        // forgor about doing this for a minute or two there ahah
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        super.setChanged();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(CARD_COUNT_TAG, this.getCardCount());
        return compoundTag;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
//        no need to save because it only exists on the client (i think)
//        compoundTag.putInt(CARD_COUNT_TAG, cardCount);
        // save card inventory only if it has been initialized
        if (this.cards != null) {
            ListTag listTag = new ListTag();
            this.cards.forEach(card -> listTag.add(IntTag.valueOf(card)));
            compoundTag.put(CARD_INVENTORY_TAG, listTag);
        }
        compoundTag.putLong(CARD_SEED_TAG, this.cardSeed);
        compoundTag.putInt(DECK_PULL_COUNT_TAG, this.deckPulls);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        // only the client will ever load this tag
        if (compoundTag.contains(CARD_COUNT_TAG)) {
            cardCount = compoundTag.getInt(CARD_COUNT_TAG);
        }
        // only set this.cards if the data has been initialized
        if (compoundTag.contains(CARD_INVENTORY_TAG)) {
            ListTag listTag = compoundTag.getList(CARD_INVENTORY_TAG, Tag.TAG_INT);
            this.cards = new ArrayList<>();
            listTag.forEach(intTag -> this.cards.add(((IntTag) intTag).getAsInt()));
        }
        if (compoundTag.contains(CARD_SEED_TAG)) {
            this.cardSeed = compoundTag.getLong(CARD_SEED_TAG);
        } else {
            this.cardSeed = RandomSource.create().nextLong();
        }
        if (compoundTag.contains(DECK_PULL_COUNT_TAG)) {
            this.deckPulls = compoundTag.getInt(DECK_PULL_COUNT_TAG);
        }

        this.randomSource = RandomSource.create(this.cardSeed);
        this.randomSource.consumeCount(this.deckPulls);

        super.load(compoundTag);
    }
}

