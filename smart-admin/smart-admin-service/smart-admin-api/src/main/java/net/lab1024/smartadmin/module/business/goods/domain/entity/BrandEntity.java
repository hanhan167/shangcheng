package net.lab1024.smartadmin.module.business.goods.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.base.BaseQuery;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_brand")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandEntity extends BaseQuery {

    Integer id;

    @NotEmpty(message = "商品名称不能为空!")
    String brandName;

    Integer createUserId;

    Date createTime;

    Integer updateUserId;

    Date updateTime;

    //'1：正常状态；0：删除状态'
    Integer deleted;

    List<FileEntity> fileEntityList;

}
