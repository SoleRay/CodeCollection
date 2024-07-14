package excel.instance.read;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 * @author SoleRay
 **/
@Getter
@Setter
@EqualsAndHashCode
public class OAReadRowData {

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

    @ExcelProperty(index=10)
    private String remark;
}
