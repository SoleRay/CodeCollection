package designmode.bridge.gift;

public abstract class Gift {

    protected GiftImpl gift;

    public Gift(GiftImpl gift) {
        this.gift = gift;
    }

    public abstract void meaning();
}
