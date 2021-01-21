package designmode.bridge;

import designmode.bridge.gift.Gift;

public interface Sender {

    void send(Receiver receiver, Gift gift);
}
