package net.lab1024.smartadmin.constant;

import net.lab1024.smartadmin.common.domain.BaseEnum;

/**
 * 操作枚举类
 *
 * @author zhouchengyan
 * @date 2019年4月11日 17:34:59
 */
public enum DealTypeEnum implements BaseEnum {


    /**
     * dev
     */
    ADD("ADD", "正常"),

    /**
     * sit
     */
    UPDATE("UPDATE", "删除");


    private String value;

    private String desc;

    DealTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取定义枚举value值
     *
     * @return Integer
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }

}
