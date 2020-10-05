package net.lab1024.smartadmin.module.business.goods.constant;

import net.lab1024.smartadmin.common.constant.ResponseCodeConst;

/**
 * 员工常量类
 * 3001-3999
 *
 * @author lidoudou
 * @date 2017年12月19日下午19:04:52
 */
public class GoodsResponseCodeConst extends ResponseCodeConst {

    /**
     * ID_NOT_NULL
     */
    public static final GoodsResponseCodeConst ID_NOT_NULL = new GoodsResponseCodeConst(10001, "id不能为空！");

    public static final GoodsResponseCodeConst IS_NULL = new GoodsResponseCodeConst(10002, "对象不存在！");


    public GoodsResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
