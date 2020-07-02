package socket.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

public class ByteBufTest {

    @Test
    public void test(){
        ByteBuf byteBuf = Unpooled.directBuffer(1024);
        String str = "world";
        byteBuf.writeBytes(str.getBytes());
        System.out.println(byteBuf.maxWritableBytes());


        ByteBuf cumulation = Unpooled.directBuffer(1024);
        String helloStr = "hello,";
        cumulation.writeBytes(helloStr.getBytes());

        cumulation.writeBytes(byteBuf,byteBuf.readerIndex(),byteBuf.readableBytes());

        byte[] bytes = new byte[cumulation.readableBytes()];

        cumulation.readBytes(bytes);

        System.out.println(new String(bytes));
    }
}
