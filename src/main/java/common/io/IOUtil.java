package common.io;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class IOUtil {

    public void copyStream(InputStream input, OutputStream output) throws IOException {
        IOUtils.copy(input, output);
    }

    public void readLineFromStream(InputStream input) throws IOException {
        List<String> line = IOUtils.readLines(input, "UTF-8");
    }
}
