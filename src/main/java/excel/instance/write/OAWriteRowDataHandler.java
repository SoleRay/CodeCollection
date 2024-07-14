package excel.instance.write;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import excel.instance.read.OAReadRowData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.util.ReflectionUtils;
import util.JsonUtils;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAWriteRowDataHandler {

    private String[] persons = new String[]{"程潜","范伟锋","李瑞华","廖子寅","王灿佩","王平康","姚轶睿"};

    private Map<Integer, Field> fieldMap = new HashMap<>();

    public OAWriteRowDataHandler() {
        Field[] fields = OAWriteRowData.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            fieldMap.put(i,fields[i]);
        }
    }

    public List<OAWriteRowData> convertToWriteData(Map<String, List<OAReadRowData>> dataMap){
        List<OAWriteRowData> writeRowDataList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (String person : persons) {
            List<OAReadRowData> personOaReadRowDataList = dataMap.get(person);
            OAWriteRowData oaWriteRowData = new OAWriteRowData();
            for (int i = 0; i < personOaReadRowDataList.size(); i++) {
                OAReadRowData oaReadRowData = personOaReadRowDataList.get(i);

                if(i==0){
                    oaWriteRowData.setName(oaReadRowData.getName());
                    oaWriteRowData.setTotalAttendDay(oaReadRowData.getTotalAttendDay());
                }

                ReflectionUtils.setField(fieldMap.get(i+2),oaWriteRowData, JsonUtils.toJsonString(oaReadRowData));
                builder.setLength(0);
            }
            writeRowDataList.add(oaWriteRowData);
        }
        return writeRowDataList;
    }
}
