package designmode.bridge;

import designmode.bridge.gift.Gift;

public class Boy implements Sender {

    @Override
    public void send(Receiver receiver, Gift gift) {
        if(receiver.receive(gift)){
            System.out.println("The receiver receive your gift");
        }else {
            System.out.println("The receiver denied your gift");
        }
    }
}
