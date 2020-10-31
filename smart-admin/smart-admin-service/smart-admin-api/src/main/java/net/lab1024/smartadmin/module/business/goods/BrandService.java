package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.DealTypeEnum;
import net.lab1024.smartadmin.constant.StatusEnum;
import net.lab1024.smartadmin.module.business.goods.constant.ModelTypeEnum;
import net.lab1024.smartadmin.module.business.goods.dao.BrandDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.service.FileService;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.DataUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst.IS_NULL;


@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> saveBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        if(!checkBrandName(brandEntity.getBrandName(),DealTypeEnum.ADD.getValue(),brandEntity.getId())){
            return ResponseDTO.wrap(ResponseCodeConst.DATA_REPEAT);
        }
        brandEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandEntity.setCreateTime(new Date());
        brandEntity.setDeleted(StatusEnum.NORMAL.getValue());
        brandDao.save(brandEntity);
        return ResponseDTO.succData(brandEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> updateBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        if(!checkBrandName(brandEntity.getBrandName(),DealTypeEnum.UPDATE.getValue(),brandEntity.getId())){
            return ResponseDTO.wrap(ResponseCodeConst.DATA_REPEAT);
        }
        brandEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandEntity.setUpdateTime(new Date());
        brandEntity.setDeleted(StatusEnum.NORMAL.getValue());
        brandDao.updateByKey(brandEntity);
        return ResponseDTO.succData(brandEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> delBrand(Integer id, RequestTokenBO requestToken) {
        BrandEntity brandEntity = brandDao.selectByKey(id);
        if(brandEntity==null){
         return ResponseDTO.wrap(IS_NULL);
        }
        brandEntity.setDeleted(StatusEnum.DELETED.getValue());
        brandEntity.setUpdateTime(new Date());
        brandEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandDao.updateByKey(brandEntity);
        return ResponseDTO.succ();
    }

    public ResponseDTO<List<BrandEntity>> queryBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        brandEntity.setDeleted(StatusEnum.NORMAL.getValue());
        List<BrandEntity> brandEntityList = brandDao.selectBrandList(brandEntity);
        if(brandEntityList!=null && brandEntityList.size()>0){
            brandEntityList.forEach(val->{
                //匹配图片
                List<FileEntity> fileList =  fileService.selectFile(ModelTypeEnum.BRAND.getName(),val.getId());
                val.setFileEntityList(fileList);
            });
        }
        return ResponseDTO.succData(brandEntityList);
    }

    /**
     * 检查名称是否重复
     * @param brandName 品牌名称
     * @param dealType  操作类型
     * @param id  主键id
     * @return
     */
    private boolean checkBrandName(String brandName,String dealType,Integer id){
        List<BrandEntity> brandEntityList = brandDao.selectList(BrandEntity.builder().brandName(brandName.trim()).deleted(StatusEnum.NORMAL.getValue()).build());
        if(DataUtils.checkList(brandEntityList)){
            if(dealType.equals(DealTypeEnum.ADD.getValue())){
                return false;
            }else{
                List<BrandEntity> brandEntities = brandEntityList.stream().filter(val -> !val.getId().equals(id)).collect(Collectors.toList());
                if(DataUtils.checkList(brandEntities)){
                    return false;
                }
            }
        }
        return true;
    }

}
