package excel.instance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import excel.instance.read.OAReadRowData;
import excel.instance.read.OAReadRowDataListener;
import excel.instance.write.OACellWriteHandler;
import excel.instance.write.OAWriteRowData;
import excel.instance.write.OAWriteRowDataHandler;

public class OATransfer {

    public static void main(String[] args) {
        cellDataRead();
    }

    public static void cellDataRead() {
        String readFile = "/Users/soleray/Downloads/4月考勤.xlsx";
        String writeFile = "/Users/soleray/Downloads/aaa.xlsx";
        String templateFile = "/Users/soleray/Downloads/考勤核对表.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet

        OAReadRowDataListener readListener = new OAReadRowDataListener();
        OAWriteRowDataHandler oaWriteRowDataHandler = new OAWriteRowDataHandler();
        EasyExcel.read(readFile, OAReadRowData.class, readListener).extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();
        EasyExcel.write(writeFile, OAWriteRowData.class).withTemplate(templateFile).inMemory(true)
                .sheet("4月-new").needHead(false).doWrite(()->oaWriteRowDataHandler.convertToWriteData(readListener.getDataMap()));


//        try (ExcelWriter excelWriter = EasyExcel.write(writeFile, OAWriteRowData.class)
//                     .registerWriteHandler(new CustomCellWeightWeightConfig()).withTemplate(templateFile).needHead(false)
//                     .build()) {
//
//            // 设置自动换行，前提内容中需要加「\n」才有效
//            WriteCellStyle writeCellStyle = new WriteCellStyle();
//            writeCellStyle.setWrapped(true);
//
//            HorizontalCellStyleStrategy cellStyleStrategy = new HorizontalCellStyleStrategy(null, writeCellStyle);
//            WriteSheet writeSheet = EasyExcel.writerSheet("4月-new").registerWriteHandler(cellStyleStrategy).build();
//
//            excelWriter.write(oaWriteRowDataHandler.convertToWriteData(readListener.getDataMap()), writeSheet);
////            excelWriter.fill(oaWriteRowDataHandler.convertToWriteData(readListener.getDataMap()), writeSheet);
////            excelWriter.finish();
//        }
    }


}
