package excel.impl;

import excel.ExcelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
}
