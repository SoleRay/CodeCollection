package excel;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ExcelService {

    <T> Workbook write(String path, List<String> fields, List<T> data, Class<T> tClass, int rowStart) throws Exception;

    <T> List<T> read(byte[] bytes, String fileSuffix, List<String> fields, Class<T> tClass, int rowStart, int cellStart) throws Exception;
}
