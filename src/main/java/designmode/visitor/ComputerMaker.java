package designmode.visitor;

import designmode.visitor.cosumer.Visitor;
import designmode.visitor.part.CPU;
import designmode.visitor.part.ComputerPart;
import designmode.visitor.part.MainBoard;
import designmode.visitor.part.Memory;

public class ComputerMaker {

    private ComputerPart cpu = new CPU(2500.0);
    private ComputerPart memory = new Memory(200);
    private ComputerPart mainBoard = new MainBoard(1000.0);

    public double makeSingleComputerPrice(int memoryNum,Visitor visitor){
        double cpuPrice = cpu.accept(visitor);
        double memoryPrice = memory.accept(visitor);
        double mainBoardPrice = this.mainBoard.accept(visitor);
        return cpuPrice + memoryPrice * memoryNum + mainBoardPrice;
    }

}
