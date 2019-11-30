package common.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileUtil {

    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }


    public static String getFileNameUnderResources(String fileName){
        return FileUtil.class.getClassLoader().getResource(fileName).getFile();
    }

    public static InputStream getFileStreamUnderResources(String fileName){
        return FileUtil.class.getClassLoader().getResourceAsStream(fileName);
    }


    public static void copyFile(String srcFileName,String destFileName) throws IOException {
        FileUtils.copyFile(new File(srcFileName),new File(destFileName));
    }

    public static List<String> readLines(String fileName) throws IOException {
        List<String> list = FileUtils.readLines(new File(fileName), "UTF-8");
        return list;
    }

    public static void writeStringToFile(String fileName,String str) throws IOException {
        FileUtils.writeStringToFile(new File(fileName),str,"UTF-8");
    }


}
