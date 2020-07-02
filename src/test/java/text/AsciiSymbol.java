package text;

import org.junit.jupiter.api.Test;

public class AsciiSymbol {

    /**
     * detail see :
     *    io.netty.handler.codec.http.HttpConstants
     *
     */
    @Test
    public void testSymbolValue(){
        int sp = ' ';
        int ht = '\t';
        int cr = '\r';
        int equals = '=';
        int lf = '\n';
        int colon = ':';
        int semicolon = ';';
        int comma = ',';
        int doubleQuote = '"';

        System.out.println("Horizontal space " + sp );
        System.out.println("Horizontal tab " + ht );
        System.out.println("Carriage " + cr );
        System.out.println("Equals " + equals );
        System.out.println("Line feed " + lf );
        System.out.println("Colon " + colon );
        System.out.println("Semicolon " + semicolon );
        System.out.println("Comma " + comma );
        System.out.println("Double quote " + doubleQuote );
    }

    @Test
    public void testByte(){
        byte sp = 32;
        byte ht = 9;
        byte cr = 13;
        byte equals = 61;
        byte lf = 10;
        byte colon = 58;
        byte semicolon = 59;
        byte comma = 44;
        byte doubleQuote = 34;

        byte[] bytes = new byte[]{sp,ht,cr,equals,lf,colon,semicolon,comma,doubleQuote};

        String s = new String(bytes);
        char[] chars = s.toCharArray();

        System.out.println("Horizontal space " + chars[0]);
        System.out.println("Horizontal tab " + chars[1] );
        System.out.println("Carriage " + chars[2] );
        System.out.println("Equals " + chars[3] );
        System.out.println("Line feed " + chars[4] );
        System.out.println("Colon " + chars[5] );
        System.out.println("Semicolon " + chars[6] );
        System.out.println("Comma " + chars[7] );
        System.out.println("Double quote " + chars[8] );
    }
}
