package codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.net.URLCodec;

import java.util.BitSet;

public class CodecDemo {

    public void base64(byte[] needEncodeBytes,String needDecodeStr){

        /** 编码，byte -> String */
        String encodedStr = Base64.encodeBase64String(needEncodeBytes);

        /** 解码，String -> byte */
        byte[] decodedBytes = Base64.decodeBase64(needDecodeStr);
    }

    public void md5(String data){
        /** 进行md5 签名 */
        String sign = DigestUtils.md5Hex(data);
    }

    public void urlEncode(BitSet urlsafe,byte[] bytes){
        URLCodec.encodeUrl(urlsafe, bytes);
    }

    public void urlDecode(byte[] bytes) throws Exception{
        URLCodec.decodeUrl(bytes);
    }
}
