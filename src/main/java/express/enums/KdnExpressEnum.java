package express.enums;

public enum KdnExpressEnum {


    SF("SF","顺丰"),
    EMS("EMS","EMS"),
    YTO("YTO","圆通"),
    ZTO("ZTO","中通"),
    YD("YD","韵达"),
    STO("STO","申通");

    private String code;

    private String name;


    private KdnExpressEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code){
        for(KdnExpressEnum expressEnum : KdnExpressEnum.values()){
            if(expressEnum.getCode().equals(code)){
                return expressEnum.getName();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
