package designmode.bridge;

import designmode.bridge.gift.Gift;

public class Girl implements Receiver {

    @Override
    public boolean receive(Gift gift) {
        gift.meaning();
        return false;
    }
}
