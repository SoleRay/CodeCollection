package concurrent.map;

import java.util.concurrent.ConcurrentHashMap;

public class PutIfAbsentVsComputeIfAbsent {

    private TestParam param = new TestParam(new ConcurrentHashMap<>(), "1", "box");

    public static void main(String[] args) {
        PutIfAbsentVsComputeIfAbsent obj = new PutIfAbsentVsComputeIfAbsent();
        obj.testNokey();
        obj.testNullKey();
        obj.testHasKey();
    }


    public void testNokey() {
        test(false);
    }

    public void testNullKey(){
        param.value=null;
        test(false);
    }

    public void testHasKey(){
        String conditionText =  "when exist key:";
        prepareData();
        putIfAbsent(param, "putIfAbsent " + conditionText);
//        prepareData();
//        computeIfAbsent(param, "computeIfAbsent " + conditionText);
    }

    private void prepareData() {
        param.map.put("1","one");
    }


    private void test(boolean hasKey) {
        String conditionText = hasKey ? "when exist key:" : "when no key:";

        putIfAbsent(param, "putIfAbsent " + conditionText);
        computeIfAbsent(param, "computeIfAbsent " + conditionText);
    }

    private void putIfAbsent(TestParam param, String testName) {

        //key不存在的时候，可以插入，但方法返回值是null
        Flow flow = (k, v) -> param.map.putIfAbsent(k, v);
        flow.flow(param, testName);
    }

    private void computeIfAbsent(TestParam param, String testName) {

       /* Flow flow = new Flow(){
            @Override
            public String doWithMethod(String key, String value) {
                return map.computeIfAbsent(key, (k) -> value);
            }
        };*/
        Flow flow = (k, v) -> param.map.computeIfAbsent(k, (kk) -> v);
        flow.flow(param, testName);
    }


    @FunctionalInterface
    interface Flow {

        default void flow(TestParam param, String testName) {
            String result = doWithMethod(param.key, param.value);
            System.out.println(testName + result);

            //实际上元素已经插入进去了
            System.out.println(testName + param.map.get(param.key));

            param.map.clear();
        }

        String doWithMethod(String key, String value);
    }

    class TestParam {
        private ConcurrentHashMap<String, String> map;
        private String key;
        private String value;

        public TestParam(ConcurrentHashMap<String, String> map, String key, String value) {
            this.map = map;
            this.key = key;
            this.value = value;
        }
    }
}
