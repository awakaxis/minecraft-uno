package net.awakaxis.uno.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public UnoCardItem(Item.Properties properties) {
        super(properties);
    }
}
