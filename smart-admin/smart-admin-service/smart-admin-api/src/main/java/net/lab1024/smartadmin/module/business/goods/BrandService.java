package net.lab1024.smartadmin.module.business.goods;

import com.google.common.eventbus.DeadEvent;
import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.goods.constant.ModelTypeEnum;
import net.lab1024.smartadmin.module.business.goods.dao.BrandDao;
import net.lab1024.smartadmin.module.business.goods.dao.GoodsDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.BrandEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.service.FileService;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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

    public ResponseDTO<PageResultDTO<GoodsVO>> queryByPage(GoodsQueryDTO queryDTO) {
            return  null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> saveBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        brandEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandEntity.setCreateTime(new Date());
        brandDao.save(brandEntity);
        return ResponseDTO.succData(brandEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> updateBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
        brandEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandEntity.setUpdateTime(new Date());
        brandDao.updateByKey(brandEntity);
        return ResponseDTO.succData(brandEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<BrandEntity> delBrand(Integer id, RequestTokenBO requestToken) {
        BrandEntity brandEntity = brandDao.selectByKey(id);
        if(brandEntity==null){
         return ResponseDTO.wrap(IS_NULL);
        }
        brandEntity.setDeleted(JudgeEnum.NO.getValue());
        brandEntity.setUpdateTime(new Date());
        brandEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        brandDao.updateByKey(brandEntity);
        return ResponseDTO.succ();
    }

    public ResponseDTO<List<BrandEntity>> queryBrand(BrandEntity brandEntity, RequestTokenBO requestToken) {
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

}
