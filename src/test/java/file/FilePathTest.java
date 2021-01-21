package file;

import java.io.File;

public class FilePathTest {

    public static void main(String[] args) {
        File file_1 = new File("2.txt");
        File file_2 = new File("config/2.txt");

        testFileExists(file_1);

    }

    private static void testFileExists(File file) {
        if(file.exists()){
            System.out.println(file.getAbsolutePath());
        }else {
            System.out.println("file not found!!!");
        }
    }
}
