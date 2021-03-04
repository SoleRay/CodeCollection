package math;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

public class TestByte {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) throws IOException {
        System.out.println(tableSizeFor(22));

    }

    static final int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void testRemainLength() throws IOException {
        BitSet bitSet = new BitSet();
//        for (int i = 0; i < 7; i++) {
//            bitSet.set(i);
//        }
        bitSet.set(4);
        bitSet.set(7);
        bitSet.set(10);
        bitSet.set(14);
        byte[] bytes = bitSet.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        long l = ByteUtil.readMBI(dataInputStream);
        System.out.println(l);
//		int num = ByteUtil.bytesToInt(bytes, 0);
        int num = ByteUtil.calRemainLength(bytes);
        System.out.println(num);

        AtomicInteger atomicInteger = new AtomicInteger();
    }
}
