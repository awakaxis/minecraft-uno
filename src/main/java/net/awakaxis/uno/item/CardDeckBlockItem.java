package net.awakaxis.uno.item;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CardDeckBlockItem extends BlockItem {

    public static final String CARD_INVENTORY_TAG = "cards";

    public CardDeckBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack itemStack = super.getDefaultInstance();

        ListTag listTag = new ListTag();

        IntTag wildTag = IntTag.valueOf(9);

        listTag.add(wildTag);
        listTag.add(wildTag);
        listTag.add(wildTag);
        listTag.add(wildTag);

        IntTag drawFourTag = IntTag.valueOf(10);

        listTag.add(drawFourTag);
        listTag.add(drawFourTag);
        listTag.add(drawFourTag);
        listTag.add(drawFourTag);

        // one of each zero card
        listTag.add(IntTag.valueOf(47));
        listTag.add(IntTag.valueOf(48));
        listTag.add(IntTag.valueOf(49));
        listTag.add(IntTag.valueOf(50));

        // add two of each number card two the deck (11 - 46)
        for (int i = 1; i < 47; i++) {
            IntTag intTag = IntTag.valueOf(i);
            listTag.add(intTag);
            listTag.add(intTag);
        }

        // add two of each non-wild special card (51 - 62)
        for (int i = 51; i < 63; i++) {
            IntTag intTag = IntTag.valueOf(i);
            listTag.add(intTag);
            listTag.add(intTag);
        }

        itemStack.getOrCreateTag().put(CARD_INVENTORY_TAG, listTag);
        return itemStack;
    }
}
