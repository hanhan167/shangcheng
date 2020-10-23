package net.lab1024.smartadmin.module.business.goods.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.anno.ApiModelPropertyEnum;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import net.lab1024.smartadmin.constant.StatusEnum;

import java.util.Date;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandVO extends BaseEntity {

    @ApiModelProperty("品牌名称")
    String brandName;

    @ApiModelProperty("品牌创建人")
    Integer createUserId;

    @ApiModelProperty("品牌修改人")
    Integer updateUserId;

    @ApiModelPropertyEnum(enumDesc = "正常状态",value = StatusEnum.class)
    Integer deleted;

}
