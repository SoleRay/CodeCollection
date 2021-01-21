package designmode.visitor.part;

import designmode.visitor.cosumer.Visitor;

public interface ComputerPart {

    double accept(Visitor visitor);
}
