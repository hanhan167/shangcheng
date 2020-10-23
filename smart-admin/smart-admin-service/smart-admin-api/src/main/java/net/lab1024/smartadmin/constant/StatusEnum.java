package net.lab1024.smartadmin.constant;

import net.lab1024.smartadmin.common.domain.BaseEnum;

/**
 * 正常标识枚举类
 *
 * @author zhouchengyan
 * @date 2019年4月11日 17:34:59
 */
public enum StatusEnum implements BaseEnum {


    /**
     * dev
     */
    NORMAL(1, "正常"),

    /**
     * sit
     */
    DELETED(0, "删除");


    private Integer value;

    private String desc;

    StatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取定义枚举value值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
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
