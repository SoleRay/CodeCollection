package excel;

public interface ExcelConstans {

    String NEW_LINE = "\n";

    interface WorkDateType {
        String WORKDAY = "工作日";

        String WEEKEND = "休息日";

        String HOLIDAYS = "节假日";
    }

    interface RemarkType {
        String MORNING_TAKE_OFF = "请上午半天假";

        String AFTERNOON_TAKE_OFF = "请下午半天假";

        String ALL_DAY_TAKE_OFF = "请假一天";

        String ALL_DAY_EXTRA_WORK = "加班一天";
    }
}
