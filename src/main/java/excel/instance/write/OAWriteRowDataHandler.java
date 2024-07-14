package excel.instance.write;

import excel.instance.read.OAReadRowData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
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

                if(StringUtils.isNotBlank(oaReadRowData.getFisrtAttendTime())){
                    builder.append("上班：").append(oaReadRowData.getFisrtAttendTime()).append("\n");
                }
                if(StringUtils.isNotBlank(oaReadRowData.getLastAttendTime())){
                    builder.append("下班：").append(oaReadRowData.getLastAttendTime());
                }

                ReflectionUtils.setField(fieldMap.get(i+2),oaWriteRowData,builder.toString());
                builder.setLength(0);
            }
            writeRowDataList.add(oaWriteRowData);
        }
        return writeRowDataList;
    }
}
