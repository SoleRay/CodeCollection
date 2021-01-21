package designmode.visitor.cosumer;

public class BussinessPerson implements Visitor {

    @Override
    public double visitCpu() {
        return 0.7;
    }

    @Override
    public double visitMemory() {
        return 0.4;
    }

    @Override
    public double visitMainBoard() {
        return 0.5;
    }
}
