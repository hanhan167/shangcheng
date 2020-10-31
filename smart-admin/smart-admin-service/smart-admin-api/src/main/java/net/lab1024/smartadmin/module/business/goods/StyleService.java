package net.lab1024.smartadmin.module.business.goods;

import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.StatusEnum;
import net.lab1024.smartadmin.module.business.goods.constant.ModelTypeEnum;
import net.lab1024.smartadmin.module.business.goods.dao.StyleDao;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsQueryDTO;
import net.lab1024.smartadmin.module.business.goods.domain.dto.GoodsVO;
import net.lab1024.smartadmin.module.business.goods.domain.entity.StyleEntity;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.service.FileService;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static net.lab1024.smartadmin.module.business.goods.constant.GoodsResponseCodeConst.IS_NULL;


@Service
public class StyleService {

    @Autowired
    private StyleDao styleDao;

    @Autowired
    private FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<StyleEntity> saveStyle(StyleEntity styleEntity, RequestTokenBO requestToken) {
        styleEntity.setCreateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        styleEntity.setCreateTime(new Date());
        styleEntity.setDeleted(StatusEnum.NORMAL.getValue());
        styleDao.save(styleEntity);
        return ResponseDTO.succData(styleEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<StyleEntity> updateStyle(StyleEntity styleEntity, RequestTokenBO requestToken) {
        styleEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        styleEntity.setUpdateTime(new Date());
        styleEntity.setDeleted(StatusEnum.NORMAL.getValue());
        styleDao.updateByKey(styleEntity);
        return ResponseDTO.succData(styleEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<StyleEntity> delStyle(Integer id, RequestTokenBO requestToken) {
        StyleEntity styleEntity = styleDao.selectByKey(id);
        if(styleEntity==null){
            return ResponseDTO.wrap(IS_NULL);
        }
        styleEntity.setDeleted(StatusEnum.DELETED.getValue());
        styleEntity.setUpdateTime(new Date());
        styleEntity.setUpdateUserId(Integer.valueOf(requestToken.getRequestUserId().toString()));
        styleDao.updateByKey(styleEntity);
        return ResponseDTO.succ();
    }

    public ResponseDTO<List<StyleEntity>> queryStyle(StyleEntity styleEntity, RequestTokenBO requestToken) {
        styleEntity.setDeleted(StatusEnum.NORMAL.getValue());
        List<StyleEntity> styleEntityList = styleDao.selectStyleList(styleEntity);
        if(!CollectionUtils.isEmpty(styleEntityList)){
            styleEntityList.forEach(val->{
                //匹配图片
                List<FileEntity> fileList =  fileService.selectFile(ModelTypeEnum.STYLE.getName(),val.getId());
                val.setFileEntityList(fileList);
            });
        }
        return ResponseDTO.succData(styleEntityList);
    }
}
