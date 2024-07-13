package excel.instance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;

public class OATransfer {

    public static void main(String[] args) {
        cellDataRead();
    }

    public static void cellDataRead() {
        String readFile = "/Users/soleray/Downloads/平台技术中心（软通）-4月考勤.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(readFile, OARowData.class, new OARowDataListener()).extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();
    }
}
