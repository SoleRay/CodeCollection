package designmode.visitor.part;

import designmode.visitor.cosumer.Visitor;

public class CPU implements ComputerPart {

    private double price;

    public CPU(double price) {
        this.price = price;
    }

    @Override
    public double accept(Visitor visitor) {
        return price * visitor.visitCpu();
    }
}
