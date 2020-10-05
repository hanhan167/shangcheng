package net.lab1024.smartadmin.module.business.goods.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.base.BaseQuery;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * [  ]
 * 分类表
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Data
@TableName("t_style")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StyleEntity extends BaseQuery {

    Integer id;

    @NotNull(message = "styleName不能为空")
    String styleName;

    Integer createUserId;

    Date createTime;

    Integer updateUserId;

    Date updateTime;

    Integer deleted;

    List<FileEntity> fileEntityList;

}
