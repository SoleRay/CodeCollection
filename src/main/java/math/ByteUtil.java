package math;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;

public class ByteUtil {

	public static int bytesToInt(byte[] ary, int offset) {
		
		int[] fixArgs = new int[]{0xFF,0xFF00,0xFF0000,0xFF000000};
		int finalValue=0;
		
		if(ary.length <= offset){
			throw new IllegalArgumentException("起始位大于数组长度");
		}

		//如果从起始位开始，byte的长度不足4位
		if(ary.length>=offset && ary.length<offset+4){
			for(int i=offset,j=0,e=0;i<ary.length;i++,j++,e=e+8){
				finalValue = finalValue | (ary[i]<< e & fixArgs[j]);
			}
		}else{
			for(int i=offset,j=0,e=0;i<offset+4;i++,j++,e=e+8){
				finalValue = finalValue | (ary[i]<< e & fixArgs[j]);
			}
		}
		
	    return finalValue;  
	}

	/**
	 * if only one byte,for instance:
	 * 0010 0111 => 0010 0111
	 *
	 * if two byte, for instance:
	 * 0100 1100 1010 1111 => 0010 0110 0010 1111
	 *
	 * if three byte, for instance:
	 * 0100 1100
	 */
	public static int calRemainLength(byte[] bytes){
		if(bytes.length==0){
			return 0;
		}

		BitSet bitSet = byteArray2BitSet(bytes);
		if(bitSet.get(7)){
			for(int i=7;i<=14;i++){
				bitSet.set(i,bitSet.get(i+1));
			}
			if(bitSet.get(15)){
				for(int i=15;i<=22;i++){
					bitSet.set(i,bitSet.get(i+1));
				}
				if(bitSet.get(23)){
					for(int i=23;i<=30;i++){
						bitSet.set(i,bitSet.get(i+1));
					}
				}

			}

		}

		byte[] result = bitSet.toByteArray();


		int value = bytesToInt(result, 0);


		return value;
	}


	/**
	 *
	 * @param bytes
	 * @param count
	 * @return
	 *
	 * This method comes from code below:
	 *
	 * int count =0;
	 * byte b1 = bytes[0];
	 * if((b1 & 0x80) > 0){
	 * 		count++;
	 * 		byte b2 = bytes[1];
	 * 		if((b2 & 0x80)>0){
	 * 			count++;
	 * 			byte b3 = bytes[2];
	 * 			if((b3 & 0x80)>0){
	 * 				count ++;
	 * 			}
	 * 		}
	 * }
	 *
	 * 0x80 = 1000 0000
	 * & 0x80 to check the highest bit is 1
	 * 1 means there are more byte...
	 *
	 * max byte is 4 (see mqtt)
	 */
	private static int getMoveCount(byte[] bytes,int count){
		if(count==bytes.length-1){
			return count;
		}
		byte b = bytes[count];
		if((b & 0x80) > 0){
			count = getMoveCount(bytes,++count);
		}

		return count;
	}

	public static BitSet byteArray2BitSet(byte[] bytes) {
		BitSet bitSet = new BitSet(bytes.length * 8);
		int index = 0;
		for (int i = 0; i < bytes.length; i++) {
			for (int j = 7; j >= 0; j--) {
				bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true
						: false);
			}
		}
		return bitSet;
	}

	/**
	 * another way to calculate remain length
	 *
	 */
	protected static long readMBI(DataInputStream in) throws IOException {
		byte digit;
		long msgLength = 0;
		int multiplier = 1;
		int count = 0;

		do {
			digit = in.readByte();
			count++;
			msgLength += ((digit & 0x7F) * multiplier);
			multiplier *= 128;
		} while ((digit & 0x80) != 0);

		return msgLength;
	}

}
