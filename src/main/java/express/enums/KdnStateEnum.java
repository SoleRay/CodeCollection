package express.enums;

public enum KdnStateEnum {


    ON_WAY(2,"在途中"),
    SIGNED(3,"已签收"),
    PROBLEM(4,"疑难件");

    private int code;

    private String name;


    private KdnStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(int code){
        for(KdnStateEnum stateEnum : KdnStateEnum.values()){
            if(stateEnum.getCode()==code){
                return stateEnum.getName();
            }
        }
        return null;
    }
}
