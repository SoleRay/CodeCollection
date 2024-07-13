package excel.instance;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板的读取类
 *
 * @author SoleRay
 */
@Slf4j
public class OARowDataListener implements ReadListener<OARowData> {

    private List<OARowData> cachedDataList = new ArrayList<>();

    private Map<Integer,Field> fieldMap = new HashMap<>();

    private Map<String,List<OARowData>> dataMap = new HashMap<>();

    public OARowDataListener() {
        Field[] fields = OARowData.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            fieldMap.put(i,fields[i]);
        }
    }

    @Override
    public void invoke(OARowData data, AnalysisContext context) {
        cachedDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        for (OARowData oaRowData : cachedDataList) {
            List<OARowData> personOaRowDataList = dataMap.get(oaRowData.getName());
            if(personOaRowDataList == null){
                personOaRowDataList = new ArrayList<>();
                dataMap.put(oaRowData.getName(),personOaRowDataList);
            }
            personOaRowDataList.add(oaRowData);
        }
        System.out.println(dataMap);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        switch (extra.getType()) {
            case MERGE:
                processMergeData(extra);
                   break;
            default:
        }
    }

    /**
     * 把合并单元格的数据，通过反射的方式，注入到每一行中的数据去
     * 举例：
     * 原本数据的第一列name和第二列totalAttendDay，是合并单元格，所以合并单元格的多行，只有第一行有数据，其余几行没有数据。
     * {"name":"小明","totalAttendDay":22,"attendStatus":"正常","realAttendDay":1,"date":1711900800000,...}
     * {"name":"null","totalAttendDay":null,"attendStatus":"正常","realAttendDay":1,"date":1711987200000,...}
     * {"name":"null","totalAttendDay":null,"attendStatus":"正常","realAttendDay":1,"date":1712073600000,...}
     * 所以，必须把合并单元格的数据，填充到其他几行中，最后的结果应该是这样：
     * {"name":"小明","totalAttendDay":22,"attendStatus":"正常","realAttendDay":1,"date":1711900800000,...}
     * {"name":"小明","totalAttendDay":22,"attendStatus":"正常","realAttendDay":1,"date":1711987200000,...}
     * {"name":"小明","totalAttendDay":22,"attendStatus":"正常","realAttendDay":1,"date":1712073600000,...}
     */
    private void processMergeData(CellExtra extra) {
        Field mergedColumnField = fieldMap.get(extra.getFirstColumnIndex());
        OARowData mergedRowData = cachedDataList.get(extra.getFirstRowIndex()-1);
        Object mergedColumnValue = ReflectionUtils.getField(mergedColumnField, mergedRowData);
        for (int i = extra.getFirstRowIndex(); i <extra.getLastRowIndex() ; i++) {
            OARowData oaRowData = cachedDataList.get(i);
            ReflectionUtils.setField(mergedColumnField,oaRowData,mergedColumnValue);
        }
    }

    public Map<String, List<OARowData>> getDataMap() {
        return dataMap;
    }
}