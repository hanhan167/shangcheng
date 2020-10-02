package net.lab1024.smartadmin.module.business.goods.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.anno.ApiModelPropertyEnum;
import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.domain.BaseEntity;

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

    @ApiModelProperty("id")
    Integer id;

    @ApiModelProperty("品牌名称")
    String brandName;

    @ApiModelProperty("品牌创建人")
    Integer createUserId;

    @ApiModelProperty("品牌创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    @ApiModelProperty("品牌修改人")
    Integer updateUserId;

    @ApiModelProperty("品牌修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateTime;

    @ApiModelPropertyEnum(enumDesc = "正常状态",value = JudgeEnum.class)
    Integer deleted;

}
