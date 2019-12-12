package top.quhailong.database.utils;


/**
 * wheteher枚举，全局真/假枚举
 */
public enum TrueFalseEnum {
    TRUE(1,"是"),
    FALSE(0,"否");

    private Integer code;

    private String name;

    TrueFalseEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public static String getName(int code) {
        for (TrueFalseEnum c : TrueFalseEnum.values()) {
            if (c.getCode()== code) {
                return c.name;
            }
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }
}
