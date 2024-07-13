package excel.instance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import excel.instance.read.OAReadRowData;
import excel.instance.read.OAReadRowDataListener;
import excel.instance.write.CustomCellWeightWeightConfig;
import excel.instance.write.OAWriteRowData;
import excel.instance.write.OAWriteRowDataHandler;

import java.util.Map;

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
//        EasyExcel.write(writeFile, OAWriteRowData.class)
//                .registerWriteHandler(new CustomCellWeightWeightConfig())
//                .sheet("4月-new").doWrite(()->oaWriteRowDataHandler.convertToWriteData(readListener.getDataMap()));


        try (ExcelWriter excelWriter = EasyExcel.write(writeFile, OAWriteRowData.class)
                     .registerWriteHandler(new CustomCellWeightWeightConfig()).withTemplate(templateFile)
                     .build()) {

            // 设置自动换行，前提内容中需要加「\n」才有效
            WriteCellStyle writeCellStyle = new WriteCellStyle();
            writeCellStyle.setWrapped(true);

            HorizontalCellStyleStrategy cellStyleStrategy = new HorizontalCellStyleStrategy(null, writeCellStyle);
            WriteSheet writeSheet = EasyExcel.writerSheet("7月").registerWriteHandler(cellStyleStrategy).build();

            excelWriter.fill(oaWriteRowDataHandler.convertToWriteData(readListener.getDataMap()), writeSheet);

            excelWriter.finish();
        }
    }


}
