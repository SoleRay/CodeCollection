package express.enums;

public enum Kd100ExpressEnum {


    SF("SF","shunfeng","顺丰"),
    EMS("EMS","ems","EMS"),
    YTO("YTO","yuantong","圆通"),
    ZTO("ZTO","zhongtong","中通"),
    YD("YD","yunda","韵达"),
    STO("STO","shentong","申通");

    private String shortCode;

    private String code;

    private String name;


    private Kd100ExpressEnum(String shortCode, String code, String name) {
        this.shortCode = shortCode;
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code){
        for(Kd100ExpressEnum expressEnum : Kd100ExpressEnum.values()){
            if(expressEnum.getCode().equals(code)){
                return expressEnum.getName();
            }
        }
        return null;
    }


    public static String getCodeByShortCode(String shortCode){
        for(Kd100ExpressEnum expressEnum : Kd100ExpressEnum.values()){
            if(expressEnum.getShortCode().equals(shortCode)){
                return expressEnum.getCode();
            }
        }
        return null;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
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
