package designmode.visitor.cosumer;

public interface Visitor {

    double visitCpu();

    double visitMemory();

    double visitMainBoard();
}
