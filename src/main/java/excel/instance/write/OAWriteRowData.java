package excel.instance.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础数据类.这里的排序和excel里面的排序一致
 *
 * @author Jiaju Zhuang
 **/
@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(55)
@HeadRowHeight(36)
@ColumnWidth(14)
@ContentStyle(horizontalAlignment=HorizontalAlignmentEnum.CENTER,verticalAlignment = VerticalAlignmentEnum.CENTER,wrapped = BooleanEnum.TRUE,
borderLeft = BorderStyleEnum.THIN,borderRight = BorderStyleEnum.THIN,borderTop = BorderStyleEnum.THIN,borderBottom = BorderStyleEnum.THIN)
@ContentFontStyle(fontName = "微软雅黑",fontHeightInPoints =12 )
public class OAWriteRowData {

    @ExcelProperty(index=0)
    private String name;

    @ExcelProperty(index=1)
    private int totalAttendDay;

    @ExcelProperty(index=2,converter = CellWriteDataConverter.class)
    private String day1;

    @ExcelProperty(index=3,converter = CellWriteDataConverter.class)
    private String day2;

    @ExcelProperty(index=4,converter = CellWriteDataConverter.class)
    private String day3;

    @ExcelProperty(index=5,converter = CellWriteDataConverter.class)
    private String day4;

    @ExcelProperty(index=6,converter = CellWriteDataConverter.class)
    private String day5;

    @ExcelProperty(index=7,converter = CellWriteDataConverter.class)
    private String day6;

    @ExcelProperty(index=8,converter = CellWriteDataConverter.class)
    private String day7;

    @ExcelProperty(index=9,converter = CellWriteDataConverter.class)
    private String day8;

    @ExcelProperty(index=10,converter = CellWriteDataConverter.class)
    private String day9;

    @ExcelProperty(index=11,converter = CellWriteDataConverter.class)
    private String day10;

    @ExcelProperty(index=12,converter = CellWriteDataConverter.class)
    private String day11;

    @ExcelProperty(index=13,converter = CellWriteDataConverter.class)
    private String day12;

    @ExcelProperty(index=14,converter = CellWriteDataConverter.class)
    private String day13;

    @ExcelProperty(index=15,converter = CellWriteDataConverter.class)
    private String day14;

    @ExcelProperty(index=16,converter = CellWriteDataConverter.class)
    private String day15;

    @ExcelProperty(index=17,converter = CellWriteDataConverter.class)
    private String day16;

    @ExcelProperty(index=18,converter = CellWriteDataConverter.class)
    private String day17;

    @ExcelProperty(index=19,converter = CellWriteDataConverter.class)
    private String day18;

    @ExcelProperty(index=20,converter = CellWriteDataConverter.class)
    private String day19;

    @ExcelProperty(index=21,converter = CellWriteDataConverter.class)
    private String day20;

    @ExcelProperty(index=22,converter = CellWriteDataConverter.class)
    private String day21;

    @ExcelProperty(index=23,converter = CellWriteDataConverter.class)
    private String day22;

    @ExcelProperty(index=24,converter = CellWriteDataConverter.class)
    private String day23;

    @ExcelProperty(index=25,converter = CellWriteDataConverter.class)
    private String day24;

    @ExcelProperty(index=26,converter = CellWriteDataConverter.class)
    private String day25;

    @ExcelProperty(index=27,converter = CellWriteDataConverter.class)
    private String day26;

    @ExcelProperty(index=28,converter = CellWriteDataConverter.class)
    private String day27;

    @ExcelProperty(index=29,converter = CellWriteDataConverter.class)
    private String day28;

    @ExcelProperty(index=30,converter = CellWriteDataConverter.class)
    private String day29;

    @ExcelProperty(index=31,converter = CellWriteDataConverter.class)
    private String day30;

    @ExcelProperty(index=32,converter = CellWriteDataConverter.class)
    private String day31;
}
