package express.util;

import http.HttpClientUtil;

import java.text.MessageFormat;

public class ExpressKd100Utils {

    private static String URL_TEMPLATE = "http://www.kuaidi100.com/query?type={0}&postid={1}";


    public static String getTraces(String shipperCode, String logisticCode) throws Exception{

        String queryUrl = MessageFormat.format(URL_TEMPLATE, shipperCode, logisticCode);
        String s = HttpClientUtil.doGet(queryUrl);
        return s;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getTraces("shunfeng","139246905772"));
    }
}
