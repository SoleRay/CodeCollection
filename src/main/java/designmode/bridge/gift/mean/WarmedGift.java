package designmode.bridge.gift.mean;

import designmode.bridge.gift.Gift;
import designmode.bridge.gift.GiftImpl;

public class WarmedGift extends Gift {

    public WarmedGift(GiftImpl gift) {
        super(gift);
    }

    @Override
    public void meaning() {
        System.out.println("warmed " + gift.getName());
    }
}
