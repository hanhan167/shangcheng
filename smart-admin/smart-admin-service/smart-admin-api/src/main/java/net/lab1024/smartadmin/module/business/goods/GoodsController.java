package net.lab1024.smartadmin.module.business.goods;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.common.anno.NoValidPrivilege;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.business.notice.domain.dto.NoticeQueryDTO;
import net.lab1024.smartadmin.module.business.notice.domain.dto.NoticeVO;
import net.lab1024.smartadmin.module.system.employee.constant.EmployeeResponseCodeConst;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.SmartRequestTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 新模块入口
 * @author zhouchengyan
 * @time 2020/10/2 11:11
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.GOODS_DEAL})
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StyleService styleService;

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "分页查询全部商品消息", notes = "@author")
    @PostMapping("goods/page/query")
    @NoValidPrivilege
    public ResponseDTO<PageResultDTO<GoodsVO>> queryByPage(@RequestBody @Valid GoodsQueryDTO queryDTO) {
        return goodsService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "新增商品", notes = "@author")
    @PostMapping("goods/saveGoods")
    @NoValidPrivilege
    public ResponseDTO<GoodsEntity> saveGoods(@RequestBody @Valid GoodsEntity goodsEntity) {
        return goodsService.saveGoods(goodsEntity);
    }


    @ApiOperation(value = "新增品牌", notes = "@author")
    @PostMapping("brand/saveBrand")
    @NoValidPrivilege
    public ResponseDTO<BrandEntity> saveBrand(@RequestBody @Valid BrandEntity brandEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return brandService.saveBrand(brandEntity,requestToken);
    }

    @ApiOperation(value = "修改品牌", notes = "@author")
    @PostMapping("brand/updateBrand")
    @NoValidPrivilege
    public ResponseDTO<BrandEntity> updateBrand(@RequestBody @Valid BrandEntity brandEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(brandEntity.getId()==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return brandService.updateBrand(brandEntity,requestToken);
    }

    @ApiOperation(value = "新增分类", notes = "@author")
    @PostMapping("style/saveStyle")
    @NoValidPrivilege
    public ResponseDTO<StyleEntity> saveStyle(@RequestBody @Valid StyleEntity styleEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return styleService.saveStyle(styleEntity,requestToken);
    }

    @ApiOperation(value = "修改分类", notes = "@author")
    @PostMapping("style/updateStyle")
    @NoValidPrivilege
    public ResponseDTO<StyleEntity> updateStyle(@RequestBody @Valid StyleEntity styleEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(styleEntity.getId()==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return styleService.updateStyle(styleEntity,requestToken);
    }

}
