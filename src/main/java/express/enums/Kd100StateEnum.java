package express.enums;

public enum  Kd100StateEnum {


    ON_WAY(0,"在途中"),
    PICKED(1,"已揽件"),
    PUZZLED(2,"疑难件"),
    SIGNED(3,"已签收"),
    DENY(4,"退签"),
    SENDING(5,"派件中"),
    RETURN(6,"退回");

    private int code;

    private String name;


    private Kd100StateEnum(int code, String name) {
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
        for(Kd100StateEnum stateEnum : Kd100StateEnum.values()){
            if(stateEnum.getCode()==code){
                return stateEnum.getName();
            }
        }
        return null;
    }
}
