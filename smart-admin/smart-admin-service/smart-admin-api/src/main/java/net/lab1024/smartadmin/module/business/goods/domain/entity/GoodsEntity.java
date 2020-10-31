package net.lab1024.smartadmin.module.business.goods.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.base.BaseQuery;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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

    @NotEmpty(message = "title不能为空")
    String title;

    @NotEmpty(message = "explain不能为空")
    String explain;

    @NotEmpty(message = "price不能为空")
    String price;

    @NotEmpty(message = "color不能为空")
    String color;

    @NotEmpty(message = "norms不能为空")
    String norms;

    Integer createUserId;

    Date createTime;

    Integer updateUserId;

    Date updateTime;

    Integer deleted;

    @Size(min = 1,message = "stypeId不能为空")
    List<Integer> stypeList;

    List<FileEntity> fileList;

}
