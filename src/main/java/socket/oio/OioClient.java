package socket.oio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Administrator on 2016-10-26.
 */
public class OioClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 18080);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String msg = reader.readLine();
                out.println(msg);
                out.flush();
                if (msg.equals("bye")) {
                    break;
                }
                System.out.println(in.readLine());
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
