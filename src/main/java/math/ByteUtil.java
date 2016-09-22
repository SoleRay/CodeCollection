package math;

public class ByteUtil {

	public static int bytesToInt(byte[] ary, int offset) {
		
		int[] fixArgs = new int[]{0xFF,0xFF00,0xFF0000,0xFF000000};
		int finalValue=0;
		
		if(ary.length <= offset){
			throw new IllegalArgumentException("起始位大于数组长度");
		}
		
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
}
