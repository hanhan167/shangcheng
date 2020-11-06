package net.lab1024.smartadmin.module.support.file.service;

import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.file.FileDao;
import net.lab1024.smartadmin.module.support.file.constant.FileResponseCodeConst;
import net.lab1024.smartadmin.module.support.file.constant.FileServiceNameConst;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import net.lab1024.smartadmin.module.support.file.domain.vo.UploadVO;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigDao;
import net.lab1024.smartadmin.module.system.systemconfig.constant.SystemConfigEnum;
import net.lab1024.smartadmin.module.system.systemconfig.domain.entity.SystemConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 下午 16:15
 * @since JDK1.8
 */
@Slf4j
@Service(FileServiceNameConst.LOCAL)
public class FileServiceLocal implements IFileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${file-upload-service.path}")
    private String fileParentPath;

    @Value("${file-upload-service.urlParent}")
    private String urlParent;

    @Value("${file.location}")
    private String fileLocation;

    private static final Long DEFAULT_SIZE = 10 * 1024 * 1024L;

    @Override
    public ResponseDTO<FileEntity> fileUpload(MultipartFile multipartFile, String path, String moduleType, Long dealUserId) {
        if (null == multipartFile) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_EMPTY);
        }
        Long maxSize = DEFAULT_SIZE;
        if (StringUtils.isNotEmpty(maxFileSize)) {
            String maxSizeStr = maxFileSize.toLowerCase().replace("mb", "");
            maxSize = Integer.valueOf(maxSizeStr) * 1024 * 1024L;
        }
        if (multipartFile.getSize() > maxSize) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_SIZE_ERROR, String.format(FileResponseCodeConst.FILE_SIZE_ERROR.getMsg(), maxFileSize));
        }
        String filePath = fileParentPath;
        if (urlParent == null) {
            return ResponseDTO.wrap(FileResponseCodeConst.LOCAL_UPDATE_PREFIX_ERROR);
        }
        if (StringUtils.isNotEmpty(path)) {
            filePath = filePath + path + "/";
        }
        File directory = new File(filePath);
        if (!directory.exists()) {
            // 目录不存在，新建
            directory.mkdirs();
        }
        UploadVO localUploadVO = new UploadVO();
        String newFileName;
        File fileTemp;
        String originalFileName;
        originalFileName = multipartFile.getOriginalFilename();
//        newFileName = this.generateFileName(originalFileName);
        newFileName = originalFileName;
        fileTemp = new File(new File(filePath + newFileName).getAbsolutePath());
        try {
            multipartFile.transferTo(fileTemp);
            localUploadVO.setUrl(urlParent + newFileName);
            localUploadVO.setFileName(newFileName);
            localUploadVO.setFilePath(path + "/" + newFileName);
            localUploadVO.setFileSize(multipartFile.getSize());
        } catch (IOException e) {
            if (fileTemp.exists() && fileTemp.isFile()) {
                fileTemp.delete();
            }
            log.error("", e);
            return ResponseDTO.wrap(FileResponseCodeConst.UPLOAD_ERROR);
        }
        //插入对应的文件表
        FileEntity fileEntity = FileEntity.builder()
                .moduleType(moduleType)
                .fileName(localUploadVO.getFileName())
                .fileSize(String.valueOf(localUploadVO.getFileSize()))
                .filePath(localUploadVO.getUrl())
                .createrUser(dealUserId)
                .build();
        fileEntity.setCreateTime(new Date());
        fileDao.insert(fileEntity);

        return ResponseDTO.succData(fileEntity);
    }

    @Override
    public ResponseDTO<String> getFileUrl(String path) {
        String urlParent = this.localUrlPrefix();
        if (urlParent == null) {
            return ResponseDTO.wrap(FileResponseCodeConst.LOCAL_UPDATE_PREFIX_ERROR);
        }
        String url = urlParent + path;
        return ResponseDTO.succData(url);
    }

    private String localUrlPrefix() {
        SystemConfigEntity configEntity = systemConfigDao.getByKey(SystemConfigEnum.Key.LOCAL_UPLOAD_URL_PREFIX.name());
        if (configEntity == null) {
            return null;
        }
        return configEntity.getConfigValue();
    }

    @Override
    public void fileDownload(String key, String fileName, HttpServletRequest request, HttpServletResponse response) {
        //截取
        int index = key.indexOf("/");
        String url;
        if(index!=-1){
            String subIndex = key.substring(index + 1);
            url = fileLocation+subIndex;
        }else{
            url = key;
        }
        File file = new File(url);
        this.downloadMethod(file, request,response);
    }
}
