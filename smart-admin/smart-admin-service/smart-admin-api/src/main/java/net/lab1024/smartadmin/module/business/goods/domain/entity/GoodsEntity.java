package net.lab1024.smartadmin.module.business.goods.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.base.BaseQuery;
import net.lab1024.smartadmin.common.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@TableName("t_goods" )
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsEntity extends BaseQuery {

    Integer id;

    @NotNull(message = "brandId不能为空")
    Integer brandId;

    @NotBlank(message = "titile不能为空")
    String titile;

    @NotBlank(message = "explain不能为空")
    String explain;

    @NotBlank(message = "price不能为空")
    String price;

    @NotBlank(message = "color不能为空")
    String color;

    @NotBlank(message = "norms不能为空")
    String norms;

    Integer createUserId;

    Date createTime;

    Integer updateUserId;

    Date updateTime;

    Integer deleted;

    @NotBlank(message = "stypeId不能为空")
    Integer stypeId;
}
