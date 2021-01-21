package designmode.visitor.part;


import designmode.visitor.cosumer.Visitor;

public class MainBoard implements ComputerPart {

    private double price;

    public MainBoard(double price) {
        this.price = price;
    }

    @Override
    public double accept(Visitor visitor) {
        return price * visitor.visitMainBoard();
    }
}
