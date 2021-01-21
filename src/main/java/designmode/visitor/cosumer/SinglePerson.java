package designmode.visitor.cosumer;

public class SinglePerson implements Visitor {

    @Override
    public double visitCpu() {
        return 1.0;
    }

    @Override
    public double visitMemory() {
        return 1.0;
    }

    @Override
    public double visitMainBoard() {
        return 1.0;
    }
}
