package math;

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

	public static int calRemainLength(byte[] bytes){
		if(bytes.length==0){
			return 0;
		}

		int count = getMoveCount(bytes,0);

		int value = bytesToInt(bytes, 0);

		return (value>>>count);
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
}
