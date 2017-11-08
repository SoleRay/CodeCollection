package excel.impl;

import excel.ExcelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//@Service
public class ExcelServiceImpl implements ExcelService {

//    @Override
    public <T> Workbook write(String path, List<String> fields, List<T> data, Class<T> tClass,int rowStart) throws Exception {
        Workbook wb = null;
        InputStream inputStream = null;

        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(path);

            wb = new XSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);

            //处理行
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i+rowStart);
                T t = data.get(i);

                //处理列
                for(int j=0; j<fields.size();j++){
                    String field = fields.get(j);
                    String getMethodName = "get"+ StringUtils.capitalize(field);
                    Method getMethod = tClass.getDeclaredMethod(getMethodName);
                    Object value = getMethod.invoke(t);

                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(value));
                }
            }
            return wb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> read(byte[] bytes, String fileSuffix, List<String> fields, Class<T> tClass, int rowStart, int cellStart) throws Exception {

        List<T> list = new ArrayList<>();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        Workbook wb = null;
        if("xls".equals(fileSuffix)){
            wb = new HSSFWorkbook(inputStream);
        }else{
            wb = new XSSFWorkbook(inputStream);
        }

        Sheet sheet = wb.getSheetAt(0);
        for (int i = rowStart; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            T t = tClass.newInstance();

            //处理列
            for(int j=cellStart; j<fields.size()+cellStart;j++){
                Cell cell = row.getCell(j);
                Object cellValue = null;

                String fieldName = fields.get(j);
                Field field = tClass.getDeclaredField(fieldName);
                Class<?> type = field.getType();

                String setMethodName = "set"+ StringUtils.capitalize(fieldName);
                Method setMethod = tClass.getDeclaredMethod(setMethodName,type);

                if(String.class.isAssignableFrom(type)){
                    cellValue = cell.getStringCellValue();
                }else if(Long.class.isAssignableFrom(type)
                        || long.class.isAssignableFrom(type)){
                    cellValue = (long) cell.getNumericCellValue();
                }else if(Integer.class.isAssignableFrom(type)
                        || int.class.isAssignableFrom(type)){
                    cellValue = (int) cell.getNumericCellValue();
                }
                setMethod.invoke(t,cellValue);
            }
            list.add(t);
        }

        return list;
    }
}
