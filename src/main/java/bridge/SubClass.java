package bridge;

public class SubClass implements SuperClass<String> {

    public String method(String param) {
        return param;
    }
}