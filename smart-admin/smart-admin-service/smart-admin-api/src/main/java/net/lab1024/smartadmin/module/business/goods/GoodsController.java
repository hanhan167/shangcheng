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
import net.lab1024.smartadmin.module.business.goods.domain.dto.TypeAndIdDTO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.business.notice.domain.dto.NoticeQueryDTO;
import net.lab1024.smartadmin.module.business.notice.domain.dto.NoticeVO;
import net.lab1024.smartadmin.module.system.employee.constant.EmployeeResponseCodeConst;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.SmartRequestTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseDTO<PageResultDTO<GoodsVO>> queryByPage(@RequestBody @Valid GoodsQueryDTO queryDTO) {
        return goodsService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "新增商品", notes = "@author")
    @PostMapping("goods/saveGoods")
    public ResponseDTO<GoodsEntity> saveGoods(@RequestBody @Valid GoodsEntity goodsEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return goodsService.saveGoods(goodsEntity,requestToken);
    }

    @ApiOperation(value = "修改商品", notes = "@author")
    @PostMapping("goods/updateGoods")
    public ResponseDTO<GoodsEntity> updateGoods(@RequestBody @Valid GoodsEntity goodsEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(goodsEntity.getId()==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return goodsService.updateGoods(goodsEntity,requestToken);
    }

    @ApiOperation(value = "删除商品", notes = "@author")
    @PostMapping("goods/delGoods/{id}")
    public ResponseDTO<GoodsEntity> delGoods(@PathVariable Integer id) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(id==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return goodsService.delGoods(id,requestToken);
    }

    @ApiOperation(value = "查询品牌", notes = "@author")
    @PostMapping("brand/queryBrand")
    public ResponseDTO<List<BrandEntity>> queryBrand(@RequestBody BrandEntity brandEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return brandService.queryBrand(brandEntity,requestToken);
    }

    @ApiOperation(value = "新增品牌", notes = "@author")
    @PostMapping("brand/saveBrand")
    public ResponseDTO<BrandEntity> saveBrand(@RequestBody @Valid BrandEntity brandEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return brandService.saveBrand(brandEntity,requestToken);
    }

    @ApiOperation(value = "修改品牌", notes = "@author")
    @PostMapping("brand/updateBrand")
    public ResponseDTO<BrandEntity> updateBrand(@RequestBody @Valid BrandEntity brandEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(brandEntity.getId()==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return brandService.updateBrand(brandEntity,requestToken);
    }

    @ApiOperation(value = "删除品牌", notes = "@author")
    @PostMapping("brand/delBrand/{id}")
    public ResponseDTO<BrandEntity> delBrand(@PathVariable Integer id) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(id==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return brandService.delBrand(id,requestToken);
    }

    @ApiOperation(value = "查询分类", notes = "@author")
    @PostMapping("style/queryStyle")
    public ResponseDTO<List<StyleEntity>> queryStyle(@RequestBody StyleEntity styleEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return styleService.queryStyle(styleEntity,requestToken);
    }

    @ApiOperation(value = "新增分类", notes = "@author")
    @PostMapping("style/saveStyle")
    public ResponseDTO<StyleEntity> saveStyle(@RequestBody @Valid StyleEntity styleEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return styleService.saveStyle(styleEntity,requestToken);
    }

    @ApiOperation(value = "修改分类", notes = "@author")
    @PostMapping("style/updateStyle")
    public ResponseDTO<StyleEntity> updateStyle(@RequestBody @Valid StyleEntity styleEntity) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(styleEntity.getId()==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return styleService.updateStyle(styleEntity,requestToken);
    }

    @ApiOperation(value = "删除品牌", notes = "@author")
    @PostMapping("brand/delStyle/{id}")
    public ResponseDTO<StyleEntity> delStyle(@PathVariable Integer id) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if(id==null){
            return ResponseDTO.wrap(GoodsResponseCodeConst.ID_NOT_NULL);
        }
        return styleService.delStyle(id,requestToken);
    }


    @ApiOperation(value = "查询是否有对应的商品", notes = "@author")
    @PostMapping("goods/findIsNotGoods")
    public ResponseDTO<List<GoodsEntity>>  findIsNotGoods(@RequestBody @Valid TypeAndIdDTO typeAndIdDTO) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return goodsService.findIsNotGoods(typeAndIdDTO,requestToken);
    }

}
