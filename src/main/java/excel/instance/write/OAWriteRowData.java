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

    @ExcelProperty(index=2)
    private String day1;

    @ExcelProperty(index=3)
    private String day2;

    @ExcelProperty(index=4)
    private String day3;

    @ExcelProperty(index=5)
    private String day4;

    @ExcelProperty(index=6)
    private String day5;

    @ExcelProperty(index=7)
    private String day6;

    @ExcelProperty(index=8)
    private String day7;

    @ExcelProperty(index=9)
    private String day8;

    @ExcelProperty(index=10)
    private String day9;

    @ExcelProperty(index=11)
    private String day10;

    @ExcelProperty(index=12)
    private String day11;

    @ExcelProperty(index=13)
    private String day12;

    @ExcelProperty(index=14)
    private String day13;

    @ExcelProperty(index=15)
    private String day14;

    @ExcelProperty(index=16)
    private String day15;

    @ExcelProperty(index=17)
    private String day16;

    @ExcelProperty(index=18)
    private String day17;

    @ExcelProperty(index=19)
    private String day18;

    @ExcelProperty(index=20)
    private String day19;

    @ExcelProperty(index=21)
    private String day20;

    @ExcelProperty(index=22)
    private String day21;

    @ExcelProperty(index=23)
    private String day22;

    @ExcelProperty(index=24)
    private String day23;

    @ExcelProperty(index=25)
    private String day24;

    @ExcelProperty(index=26)
    private String day25;

    @ExcelProperty(index=27)
    private String day26;

    @ExcelProperty(index=28)
    private String day27;

    @ExcelProperty(index=29)
    private String day28;

    @ExcelProperty(index=30)
    private String day29;

    @ExcelProperty(index=31)
    private String day30;

    @ExcelProperty(index=32)
    private String day31;
}
