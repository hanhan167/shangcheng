package net.lab1024.smartadmin.module.support.file.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.file.domain.entity.FileEntity;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件服务接口
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 下午 16:42
 * @since JDK1.8
 */
public interface IFileService {

     String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
     String HEADER_CONTENT_LENGTH = "Content-Length";
     String CONTENT_TYPE = "application/octet-stream";
    /**
     * 默认缓冲数组字节大小.
     */
    int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param path
     * @param moduleType
     * @param dealUserId
     * @return
     */
    ResponseDTO<FileEntity> fileUpload(MultipartFile multipartFile, String path, String moduleType, Long dealUserId);

    /**
     * 获取文件url
     *
     * @param path
     * @return
     */
    ResponseDTO<String> getFileUrl(String path);

    /**
     * 文件下载
     *
     * @param key
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    void fileDownload(String key, String fileName, HttpServletRequest request, HttpServletResponse response);

    /**
     * 生成文件名字
     * 当前年月日时分秒 +32位 uuid + 文件格式后缀
     *
     * @param originalFileName
     * @return String
     */
    default String generateFileName(String originalFileName) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmms"));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        return time + uuid + fileType;
    }

    /**
     * 获取文件类型
     *
     * @param fileExt
     * @return
     */
    default String getContentType(String fileExt) {
        // 文件的后缀名
        if ("bmp".equalsIgnoreCase(fileExt)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExt)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt) || ".png".equalsIgnoreCase(fileExt)) {
            return "image/jpeg";
        }
        if ("png".equalsIgnoreCase(fileExt)) {
            return "image/png";
        }
        if ("html".equalsIgnoreCase(fileExt)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExt)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExt)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(fileExt)) {
            return "text/xml";
        }
        return "";
    }

    default void downloadMethod(File file, HttpServletRequest request, HttpServletResponse response) {
        String fileName = file.getName();
        ByteArrayInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                // firefox浏览器
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                // IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("EDGE") > 0) {
                // WIN10浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
                // 谷歌
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else {
                //万能乱码问题解决
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            // log.error("", e);
        }
        try {
            byte[] byteArray = file2byte(file);
            response.reset();
            response.addHeader(HEADER_CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
            response.addHeader(HEADER_CONTENT_LENGTH, String.valueOf(byteArray.length));
            response.setContentType(CONTENT_TYPE);
            bis = new ByteArrayInputStream(byteArray);
            bos = new BufferedOutputStream(response.getOutputStream());  //file.delete();
            int bytesRead;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            // 将文件发送到客户端
            bos.flush();
        } catch (Exception e) {
            // log.error("", e);
        }finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
        }
    }


    /**
     * 文件转byte数组
     * @param tradeFile
     * @return
     */
    static byte[] file2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

}
