package excel.instance.write;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import excel.ExcelConstans;
import excel.instance.read.OAReadRowData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import util.JsonUtils;

import java.time.Duration;
import java.time.LocalTime;

public class CellWriteDataConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadConverterContext<?> context) {
        return context.getReadCellData().getStringValue();
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) {


        WriteCellData<String> writeCellData = new WriteCellData<>();
        writeCellData.setType(CellDataTypeEnum.STRING);

        if(StringUtils.isBlank(context.getValue())){
            return writeCellData;
        }

        try {
            OAReadRowData oaReadRowData = JsonUtils.toJavaObject(context.getValue(), OAReadRowData.class);

            writeCellData.setStringValue(buildCellValue(oaReadRowData));

            initWriteCellStyle(writeCellData);
            switch (oaReadRowData.getDateType()) {
                case ExcelConstans.WorkDateType.WORKDAY:
                    checkOnWorkday(oaReadRowData,writeCellData);
                    break;
                case ExcelConstans.WorkDateType.WEEKEND:
                case ExcelConstans.WorkDateType.HOLIDAYS:
                    checkOnWeekendAndHolidays(oaReadRowData,writeCellData);
                    break;
                default:
                    break;
            }

            return writeCellData;
        }catch (Exception e){
            e.printStackTrace();
            return writeCellData;
        }

    }

    private void checkOnWorkday(OAReadRowData oaReadRowData, WriteCellData<String> writeCellData) {
        WriteCellStyle writeCellStyle = writeCellData.getWriteCellStyle();
        boolean isCheckOn = StringUtils.isNotBlank(oaReadRowData.getFisrtAttendTime());
        boolean isCheckOff = StringUtils.isNotBlank(oaReadRowData.getLastAttendTime());

        //两个都没有，设置为旷工
        if(!isCheckOn && !isCheckOff){
            if(ExcelConstans.RemarkType.ALL_DAY_TAKE_OFF.equals(oaReadRowData.getRemark())){
                writeCellData.setStringValue("上班：请假\n下班：请假");
                markCommonDiffColor(writeCellStyle);
            }else {
                writeCellData.setStringValue("旷工");
                markSeriousDiffColor(writeCellStyle);
            }
            return;
        }

        //有一个，字体标红
        if(!isCheckOn || !isCheckOff){
            markSeriousDiffColor(writeCellStyle);
            return;
        }

        //都有，检查工时是否充足
        checkWorkHourEnough(oaReadRowData, writeCellData);
    }



    private void checkOnWeekendAndHolidays(OAReadRowData oaReadRowData, WriteCellData<String> writeCellData) {
        boolean isCheckOn = StringUtils.isNotBlank(oaReadRowData.getFisrtAttendTime());
        boolean isCheckOff = StringUtils.isNotBlank(oaReadRowData.getLastAttendTime());

        //非工作日，如果有工时，则额外处理
        if(isCheckOn || isCheckOff){

            //只要有工时，就底色打上黄色
            writeCellData.getWriteCellStyle().setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            writeCellData.getWriteCellStyle().setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            if(isCheckOn && isCheckOff){
                //上下班工时都有，检查工时是否充足
                checkWorkHourEnough(oaReadRowData, writeCellData);
            }else {
                //上下班工时缺少，颜色标红
                markSeriousDiffColor(writeCellData.getWriteCellStyle());
            }
        }
    }


    private void initWriteCellStyle(WriteCellData<String> writeCellData) {
        WriteCellStyle writeCellStyle = writeCellData.getWriteCellStyle();
        if(writeCellStyle == null){
            writeCellStyle = new WriteCellStyle();
            writeCellStyle.setWriteFont(new WriteFont());
            writeCellData.setWriteCellStyle(writeCellStyle);
        }
    }

    private void checkWorkHourEnough(OAReadRowData oaReadRowData, WriteCellData<String> writeCellData) {
        LocalTime clockOnTime = LocalTime.parse(oaReadRowData.getFisrtAttendTime());
        LocalTime clockOffTime = LocalTime.parse(oaReadRowData.getLastAttendTime());
        Duration duration = Duration.between(clockOnTime, clockOffTime);
        if(duration.toHours()< 9){
            markCommonDiffColor(writeCellData.getWriteCellStyle());
        }
    }

    private String buildCellValue(OAReadRowData oaReadRowData) {
        String cellValue = "";


        if(StringUtils.isNotBlank(oaReadRowData.getFisrtAttendTime())){
            cellValue = cellValue + "上班：" + oaReadRowData.getFisrtAttendTime() + "\n";
        }

        if(StringUtils.isNotBlank(oaReadRowData.getLastAttendTime())){
            cellValue = cellValue + "下班：" + oaReadRowData.getLastAttendTime();
        }
        return cellValue;
    }

    private void markCommonDiffColor(WriteCellStyle writeCellStyle) {
        writeCellStyle.getWriteFont().setBold(true);
        writeCellStyle.getWriteFont().setColor(IndexedColors.PLUM.getIndex());
    }

    private void markSeriousDiffColor(WriteCellStyle writeCellStyle) {
        writeCellStyle.getWriteFont().setBold(true);
        writeCellStyle.getWriteFont().setColor(IndexedColors.RED.getIndex());
    }

}