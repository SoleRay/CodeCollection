package designmode.visitor.part;

import designmode.visitor.cosumer.Visitor;

public class Memory implements ComputerPart {

    private double price;

    public Memory(double price) {
        this.price = price;
    }

    @Override
    public double accept(Visitor visitor) {
        return price * visitor.visitMemory();
    }
}
