package net.lab1024.smartadmin.module.business.goods.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.domain.BaseEntity;

import java.util.Date;

/**
 * [  ]
 * 商品表
 *
 * @author zhouchengyan
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodsVO extends BaseEntity {

    @ApiModelProperty("品牌id")
    Integer brandId;

    @ApiModelProperty("品牌id")
    String title;

    @ApiModelProperty("说明")
    String explain;

    @ApiModelProperty("价格")
    String price;

    @ApiModelProperty("颜色")
    String color;

    @ApiModelProperty("规格")
    String norms;

    @ApiModelProperty("创建人id")
    Integer createUserId;

    @ApiModelProperty("修改人id")
    Integer updateUserId;

}
