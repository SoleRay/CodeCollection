package designmode.bridge;

import designmode.bridge.gift.Gift;
import designmode.bridge.gift.mean.WarmedGift;
import designmode.bridge.gift.real.Flower;

public class BridgeMain {

    public static void main(String[] args) {
        Boy boy = new Boy();
        Girl girl = new Girl();
        Gift gift = new WarmedGift(new Flower());
        boy.send(girl,gift);
    }
}
