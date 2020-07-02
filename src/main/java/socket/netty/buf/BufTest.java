package socket.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.ByteProcessor;
import org.junit.jupiter.api.Test;

public class BufTest {


    @Test
    public void testPooledByteBuf(){
        /**
         * byteBuf1 和 byteBuf1是两个不同的对象
         * 但底层是同一块Chunk,且内部指向同一个数组
         * 且可以看到byteBuf1的offset=0，byteBuf2的offset=8192
         */
        ByteBuf byteBuf1 = PooledByteBufAllocator.DEFAULT.heapBuffer(8192);
        ByteBuf byteBuf2 = PooledByteBufAllocator.DEFAULT.heapBuffer(8192);

        System.out.println();
    }

    @Test
    public void testRelease(){
        /**
         * byteBuf1 和 byteBuf1是两个不同的对象
         * 但底层是同一块Chunk,且内部指向同一个数组
         * 且可以看到byteBuf1的offset=0，byteBuf2的offset=8192
         */
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer(8192);

        //释放后，引用计数会减少
        System.out.println(byteBuf.refCnt());
        byteBuf.release();
        System.out.println(byteBuf.refCnt());

        //释放以后不能再写入，会报错
//        byteBuf1.writeByte(1);

        System.out.println();
    }

    /**
     * duplicate方法，是共享缓冲区的，只是readIndex和writeIndex，独立维护
     * 因为共享缓冲区，所以如果duplicate出来的和原来的都往同一个索引写，则后写的那个，会覆盖先写的。
     *
     * retainedDuplicate方法，和duplicate方法基本一样，不同之处在于，retainedDuplicate方法会使ByteBuf的引用计数+1
     * 这有什么好处？
     * 如果是duplicate，那么两个ByteBuf的引用，任意一个执行了release，那么两个都释放了。都不能继续写了。
     * 而retainedDuplicate，只有两个都release，那么引用计数才会变成0，才会释放。
     */
    @Test
    public void testDuplicate(){
        ByteBuf original = PooledByteBufAllocator.DEFAULT.heapBuffer(16);
        original.writeByte(1);
        original.writeByte(2);
        original.writeByte(3);
        original.writeByte(4);
        original.writeByte(5);

        original.readByte();
        original.readByte();
        original.readByte();

        ByteBuf duplicate = original.duplicate();
        System.out.println("readerIndex:"+duplicate.readerIndex()+",writerIndex:"+duplicate.writerIndex());


        original.writeByte(16);

        //覆盖original，index=5的数据
        duplicate.writeByte(17);

        original.retainedDuplicate();
    }

    /**
     * slice方法，和原先的ByteBuf，同样共享缓冲区。
     * 但是slice方法获取出来的ByteBuf，只是原先ByteBuf的一部分。
     *
     * 以下面的例子为例，原先的ByteBuf，readerIndex = 3，writerIndex = 5
     * 而slice出来的ByteBuf，readerIndex = 0，writerIndex = 3
     *
     * slice出来的ByteBuf,不能再write，但可以修改
     *
     * 而retainedSlice，和而retainedDuplicate类似，就是计数+1
     */
    @Test
    public void testSilce(){
        ByteBuf original = PooledByteBufAllocator.DEFAULT.heapBuffer(16);
        original.writeByte(1);
        original.writeByte(2);
        original.writeByte(3);
        original.writeByte(4);
        original.writeByte(5);

        original.readByte();
        original.readByte();
        original.readByte();

        System.out.println("originalBuf readerIndex:"+original.readerIndex());
        System.out.println("originalBuf writerIndex:"+original.writerIndex());

        ByteBuf slicedBuf = original.slice(1, 3);
        System.out.println("slicedBuf readerIndex:"+slicedBuf.readerIndex());
        System.out.println("slicedBuf writerIndex:"+slicedBuf.writerIndex());
        System.out.println(slicedBuf.readByte());

        //报错：slicedBuf 越界。。
//        slicedBuf.writeByte(6);

        //不能write，但可以set，set以后，original也发生变化
        slicedBuf.setByte(1,6);
        System.out.println(original.getByte(2));


    }


    /**
     * ReadSilce和silce的区别在于，silce可以取 0 - writeIndex的任意一块，并且original的readIndex和writeIndex都不会变化
     * 而ReadSilce，只能取 以 readerIndex 为起点，最大不能超过writeIndex的 一段子缓冲区。
     * 并且执行ReadSilce以后，original也会移动ReadSilce所截取的长度。
     *
     * 以下面的为例：originalBuf 最初的readerIndex = 3，writerIndex = 5
     * ReadSilce，截取剩余可读的长度，于是originalBuf的 readerIndex = 3，writerIndex = 5
     * 也就是说，originalBuf 不可以再读取 ReadSilce出来的缓冲区。
     *
     * 这在某些场景下是有使用场景的。即slice 一个 子缓冲区，我们希望 originalBuf 不能再读取。
     *
     *
     */
    @Test
    public void testReadSilce(){
        ByteBuf original = PooledByteBufAllocator.DEFAULT.heapBuffer(16);
        original.writeByte(1);
        original.writeByte(2);
        original.writeByte(3);
        original.writeByte(4);
        original.writeByte(5);

        original.readByte();
        original.readByte();
        original.readByte();

        System.out.println("originalBuf readerIndex:"+original.readerIndex());
        System.out.println("originalBuf writerIndex:"+original.writerIndex());

        ByteBuf readSlicedBuf = original.readSlice(original.readableBytes());
        System.out.println("slicedBuf readerIndex:"+readSlicedBuf.readerIndex());
        System.out.println("slicedBuf writerIndex:"+readSlicedBuf.writerIndex());
//        System.out.println(readSlicedBuf.readByte());
        System.out.println("originalBuf readerIndex:"+original.readerIndex());
        System.out.println("originalBuf writerIndex:"+original.writerIndex());
    }


    @Test
    public void testEnsureWritable(){

        byte[] bytes = getBytes();

        ByteBuf buffer = Unpooled.buffer(4);
        System.out.println(buffer.capacity());

        buffer.ensureWritable(bytes.length);
        System.out.println(buffer.capacity());

//        buffer.writeBytes(bytes);
    }

    private byte[] getBytes() {
        String s = "abcde\r\n12345\r\n67890\r\n";
        return s.getBytes();
    }

    @Test
    public void testForEachByte(){
        StringBuilder sb = new StringBuilder();
        ByteBuf buffer = Unpooled.buffer(4);

        byte[] bytes = getBytes();
        buffer.writeBytes(bytes);

        int i = buffer.forEachByte(new ByteProcessor() {
            @Override
            public boolean process(byte value) throws Exception {

                char nextByte = (char) (value & 0xFF);
                if (nextByte == HttpConstants.CR) {
                    return true;
                }
                if (nextByte == HttpConstants.LF) {
                    return false;
                }

                sb.append(nextByte);
                return true;
            }
        });

        System.out.println(sb.toString());
    }
}
