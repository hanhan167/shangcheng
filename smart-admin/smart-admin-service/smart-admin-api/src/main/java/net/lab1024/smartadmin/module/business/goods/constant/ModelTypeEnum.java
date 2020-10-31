package net.lab1024.smartadmin.module.business.goods.constant;

public enum ModelTypeEnum {


    GOODS("GOODS","1","商品"),

    STYLE("STYLE","2","风格"),

    BRAND("BRAND","3","品牌");

    private String name;

    private String value;

    private String desc;

    ModelTypeEnum(String name, String value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
