package text;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class StringCodecText {

    @Test
    public void test(){
        String orginalText = "ABC";
//        String orginalText = "我们";

//        Charset charset = CharsetUtil.UTF_8;
        Charset charset = CharsetUtil.ISO_8859_1;


        byte[] bytes = encode(orginalText,charset);

        String decodedText = decode(bytes, charset);


        System.out.println(decodedText);
    }

    private String decode(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    private byte[] encode(String orginalText, Charset charset) {
        return orginalText.getBytes(charset);
    }
}
