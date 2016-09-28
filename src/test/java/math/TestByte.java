package math;

import java.util.BitSet;

public class TestByte {

	public static void main(String[] args) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < 23; i++) {
			bitSet.set(i);
		}
		byte[] bytes = bitSet.toByteArray();
//		int num = ByteUtil.bytesToInt(bytes, 0);

		int num = ByteUtil.calRemainLength(bytes);
		System.out.println(num);
	}
}
