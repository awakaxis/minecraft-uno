package net.awakaxis.uno.mixin;

import net.awakaxis.uno.UNOBlocks;
import net.awakaxis.uno.UNOItems;
import net.awakaxis.uno.item.UnoCardItem;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {

    @Inject(method="updateTrades", at=@At("RETURN"))
    private void appendUnoCard(CallbackInfo ci) {
        WanderingTrader wanderingTrader = (WanderingTrader) (Object) this;

        MerchantOffers merchantOffers = wanderingTrader.getOffers();

        MerchantOffer unoCardOffer = new MerchantOffer(
                new ItemStack(Items.EMERALD, 15),
                ItemStack.EMPTY,
                UNOBlocks.CARD_DECK.asItem().getDefaultInstance(),
                1, 5, 0.05f
        );

        merchantOffers.add(unoCardOffer);
    }
}
