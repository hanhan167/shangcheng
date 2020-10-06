package net.lab1024.smartadmin.module.business.goods.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.common.base.BaseQuery;
import net.lab1024.smartadmin.common.domain.PageParamDTO;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Data
public class GoodsQueryDTO extends BaseQuery {

    @ApiModelProperty("风格id")
    private String styleId;

    @ApiModelProperty("品牌id")
    private String brandId;

    @ApiModelProperty("价格区间")
    private String price;

    @ApiModelProperty("排序规则")
    private String sortFields;

}
