package net.lab1024.smartadmin.module.support.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.file.FileDao;
import net.lab1024.smartadmin.module.support.file.constant.FileServiceTypeEnum;
import net.lab1024.smartadmin.module.support.file.domain.dto.FileAddDTO;
import net.lab1024.smartadmin.module.support.file.domain.dto.FileDTO;
import net.lab1024.smartadmin.module.support.file.domain.dto.FileQueryDTO;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.domain.vo.FileVO;
import net.lab1024.smartadmin.module.support.file.domain.vo.UploadVO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.BusinessException;
import net.lab1024.smartadmin.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import net.lab1024.smartadmin.util.SmartPageUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private static final ConcurrentHashMap<String, IFileService> fileServiceMap = new ConcurrentHashMap<>();

    @Resource(name = "local")
    private IFileService fileService;

    /**
     * 获取文件服务实现
     *
     * @param typeEnum
     * @return
     */
    private IFileService getFileService(FileServiceTypeEnum typeEnum) {
        /**
         * 获取文件服务
         */
        String serviceName = typeEnum.getServiceName();
        IFileService fileService = fileServiceMap.get(serviceName);
        if (null == fileService) {
            throw new RuntimeException("未找到文件服务实现类：" + serviceName);
        }
        return fileService;
    }

    /**
     * 文件上传服务
     *
     * @param file
     * @param typeEnum     文件服务类型枚举类
     * @param moduleType   文件夹类型
     * @param requestToken
     * @return
     */
    public ResponseDTO<FileEntity> fileUpload(MultipartFile file, FileServiceTypeEnum typeEnum, String moduleType, RequestTokenBO requestToken) {
        // 获取文件服务
        //IFileService fileService = this.getFileService(typeEnum);//默认阿里云服务器
        ResponseDTO<FileEntity> response = fileService.fileUpload(file, "file", moduleType, requestToken.getRequestUserId());
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFileList(List<FileEntity> fileEntities, Integer id) {
        if (fileEntities != null && fileEntities.size() > 0) {
            for (FileEntity fileEntity : fileEntities) {
                if (fileEntity.getFileIndex() == null || fileEntity.getFileIndex() == 0) {
                    throw new BusinessException(ResponseCodeConst.ERROR_PARAM.getCode(), "fileIndex不能为空!");
                }
                fileEntity.setModuleId(id.toString());
                fileDao.updateById(fileEntity);
            }
        }
    }


    /**
     * 根据文件绝对路径 获取文件URL
     *
     * @param path
     * @return
     */
    public ResponseDTO<String> getFileUrl(String path, FileServiceTypeEnum typeEnum) {
        //IFileService fileService = this.getFileService(typeEnum);
        return fileService.getFileUrl(path);
    }

    /**
     * 批量插入
     *
     * @param fileDTOList
     */
    public void insertFileBatch(List<FileDTO> fileDTOList) {
        fileDao.insertFileBatch(fileDTOList);
    }

    /**
     * 根据module 删除文件信息
     *
     * @param moduleId
     * @return
     */
    public void deleteFilesByModuleId(String moduleId) {
        fileDao.deleteFilesByModuleId(moduleId);
    }

    /**
     * 根据module 获取文件信息
     *
     * @param moduleId
     * @return
     */
    public List<FileVO> listFilesByModuleId(String moduleId) {
        return fileDao.listFilesByModuleId(moduleId);
    }

    /**
     * @param filesStr 逗号分隔文件id字符串
     * @return
     */
    public List<FileVO> getFileDTOList(String filesStr) {
        if (StringUtils.isEmpty(filesStr)) {
            return Lists.newArrayList();
        }
        String[] fileIds = filesStr.split(",");
        List<Long> fileIdList = Arrays.asList(fileIds).stream().map(e -> Long.valueOf(e)).collect(Collectors.toList());
        List<FileVO> files = fileDao.listFilesByFileIds(fileIdList);
        return files;
    }

    /**
     * 分页查询文件列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<FileVO>> queryListByPage(FileQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<FileVO> fileList = fileDao.queryListByPage(page, queryDTO);
        if (CollectionUtils.isNotEmpty(fileList)) {
            fileList.forEach(e -> {
                // 根据文件服务类 获取对应文件服务 查询 url
                FileServiceTypeEnum serviceTypeEnum = SmartBaseEnumUtil.getEnumByValue(e.getFileLocationType(), FileServiceTypeEnum.class);
                IFileService fileService = this.getFileService(serviceTypeEnum);
                e.setFileUrl(fileService.getFileUrl(e.getFilePath()).getData());
            });
        }
        PageResultDTO<FileVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, fileList);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 根据id 下载文件
     *
     * @param id
     * @param request
     * @return
     */
    public void downLoadById(Long id, HttpServletRequest request, HttpServletResponse response) {
        FileEntity entity = fileDao.selectById(id);
        if (null == entity) {
            throw new RuntimeException("文件信息不存在");
        }

        // 根据文件服务类 获取对应文件服务 查询 url
//        FileServiceTypeEnum serviceTypeEnum = SmartBaseEnumUtil.getEnumByValue(entity.getFileLocationType(), FileServiceTypeEnum.class);
//        IFileService fileService = this.getFileService(serviceTypeEnum);
         fileService.fileDownload(entity.getFilePath(),entity.getFileName(),request,response);
    }

    /**
     * 系统文件保存通用接口
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> saveFile(FileAddDTO addDTO, RequestTokenBO requestToken) {
        FileEntity entity = SmartBeanUtil.copy(addDTO, FileEntity.class);
        entity.setCreaterUser(requestToken.getRequestUserId());
        entity.setCreateTime(new Date());
        entity.setFileIndex(addDTO.getFileIndex());
        fileDao.insert(entity);
        return ResponseDTO.succ();
    }


    public List<FileEntity> selectFile(String moduleType, Integer moduleId) {
        List<FileEntity> fileEntityList = fileDao.selectFile(moduleType, moduleId);
        return fileEntityList;
    }

    public ResponseDTO<FileEntity> deleteLocalFile(Integer fileId, RequestTokenBO requestToken) {
        FileEntity fileEntity = fileDao.selectById(fileId);
        if (fileEntity == null) {
            throw new BusinessException(ResponseCodeConst.NOT_EXISTS.getCode(), "文件不存在!");
        }
        String filePath = fileEntity.getFilePath();
        boolean deleteFileFlag = deleteFile(filePath);
        if(deleteFileFlag){
            fileDao.deleteById(fileId);
            return ResponseDTO.succ();
        }
        return ResponseDTO.wrap(ResponseCodeConst.DELETE_FILE_ERROR);
    }

    /**
     * 删除单个文件
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            boolean delete = file.delete();
            return delete ? true : false;
        }
        return false;
    }

}