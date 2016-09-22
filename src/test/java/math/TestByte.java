package math;

import java.util.BitSet;

public class TestByte {

	public static void main(String[] args) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < 8; i++) {
			bitSet.set(i);
		}
		byte[] bytes = bitSet.toByteArray();
		int num = ByteUtil.bytesToInt(bytes, 0);
		System.out.println(num);
	}
}
