package file;

import java.io.InputStream;

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
}
