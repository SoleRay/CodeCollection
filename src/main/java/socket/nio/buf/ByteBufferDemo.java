package socket.nio.buf;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class ByteBufferDemo {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putInt(536724);
    }
}
