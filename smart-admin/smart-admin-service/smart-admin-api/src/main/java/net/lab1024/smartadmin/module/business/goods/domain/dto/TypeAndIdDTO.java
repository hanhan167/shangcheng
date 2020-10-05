package net.lab1024.smartadmin.module.business.goods.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.lab1024.smartadmin.common.base.BaseQuery;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeAndIdDTO extends BaseQuery {

    @NotNull(message = "id不能位空！")
    @ApiModelProperty("id")
    Integer id;

    @NotNull(message = "类型不能位空！")
    @ApiModelProperty("类型；1：品牌 2：风格")
    Integer type;

}
