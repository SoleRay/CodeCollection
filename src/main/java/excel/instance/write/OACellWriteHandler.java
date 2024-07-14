package excel.instance.write;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.time.Duration;
import java.time.LocalTime;

public class OACellWriteHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        WriteCellData<?> cellData = context.getFirstCellData();
        if(cellData.getType().equals(CellDataTypeEnum.STRING)){
            String cellValue = cellData.getStringValue();
            if(StringUtils.isNotBlank(cellValue) && cellValue.contains("\n")){
                String[] splitCellValues = cellValue.split("\n");
                String[] clockOnStr = StringUtils.split(splitCellValues[0],"：");
                String[] clockOffStr = StringUtils.split(splitCellValues[1],"：");
                String clockOnTimeStr = "";
                String clockOffTimeStr = "";
                if(clockOnStr.length>1){
                    clockOnTimeStr = clockOnStr[1];
                }
                if(clockOffStr.length>1){
                    clockOffTimeStr = clockOffStr[1];
                }

                LocalTime clockOnTime = LocalTime.parse(clockOnTimeStr);
                LocalTime clockOffTime = LocalTime.parse(clockOffTimeStr);
                Duration duration = Duration.between(clockOnTime, clockOffTime);
                if(duration.toHours()< 9){
                    WriteCellStyle writeCellStyle = cellData.getWriteCellStyle();
                    WriteFont writeFont = writeCellStyle.getWriteFont();
                    writeFont.setColor(IndexedColors.PLUM.getIndex());
                    writeFont.setBold(Boolean.TRUE);
//                    System.out.println("clockOnTimeStr="+clockOnTimeStr+",clockOffTimeStr="+clockOffTimeStr);
                }

            }
        }
    }
}
