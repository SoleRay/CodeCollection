package excel.instance.write;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import excel.ExcelConstans;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填充内容包含\n换行符，重新计算行高
 */
public class CustomCellWeightWeightConfig extends AbstractColumnWidthStyleStrategy {

    private Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        boolean needSetWidth = isHead || CollectionUtils.isNotEmpty(cellDataList);
        if (needSetWidth) {
            //包含\n的内容修改行高
            String cellValue = getCellValue(cell);
            boolean contains =  StringUtils.contains(cellValue, ExcelConstans.NEW_LINE);
            if (contains) {
                int rows = cellValue.split(ExcelConstans.NEW_LINE).length;
                float height = cell.getRow().getHeightInPoints();
                cell.getRow().setHeightInPoints(rows * height);
            }
        }
    }

    public static String getCellValue(Cell cell) {
        String cellValue;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            default:
                cellValue = cell.getStringCellValue();
                break;
        }
        return cellValue;
    }

}

