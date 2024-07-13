package excel.instance;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;

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
public class OARowData {
    /**
     * 我自定义 转换器，不管数据库传过来什么 。我给他加上“自定义：”
     */
    @ExcelProperty(index=0)
    private String name;

    @ExcelProperty(index=1)
    private int totalAttendDay;

    @ExcelProperty(index=2)
    private String attendStatus;

    @ExcelProperty(index=3)
    private int realAttendDay;

    @ExcelProperty(index=4)
    private Date date;

    @ExcelProperty(index=5)
    private String dateType;

    @ExcelProperty(index=6)
    private Date fisrtAttendDate;

    @ExcelProperty(index=7)
    @DateTimeFormat("HH:mm")
    private String fisrtAttendTime;

    @ExcelProperty(index=8)
    private Date lastAttendDate;

    @ExcelProperty(index=9)
    @DateTimeFormat("HH:mm")
    private String lastAttendTime;
}
