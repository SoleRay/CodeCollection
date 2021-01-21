package designmode.bridge.gift.mean;

import designmode.bridge.gift.Gift;
import designmode.bridge.gift.GiftImpl;

public class PricedGift extends Gift {

    public PricedGift(GiftImpl gift) {
        super(gift);
    }

    @Override
    public void meaning() {
        System.out.println("priced " + gift.getName());
    }
}
