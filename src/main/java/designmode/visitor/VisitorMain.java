package designmode.visitor;

import designmode.visitor.cosumer.BussinessPerson;
import designmode.visitor.cosumer.SinglePerson;

public class VisitorMain {

    public static void main(String[] args) {
        SinglePerson singlePerson = new SinglePerson();
        BussinessPerson bussinessPerson = new BussinessPerson();

        ComputerMaker cm = new ComputerMaker();
        double p1 = cm.makeSingleComputerPrice(4, singlePerson);
        double p2 = cm.makeSingleComputerPrice(4, bussinessPerson);

        System.out.println(p1);
        System.out.println(p2);
    }
}
