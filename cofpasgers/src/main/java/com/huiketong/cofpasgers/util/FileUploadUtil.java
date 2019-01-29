package com.huiketong.cofpasgers.util;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.json.data.UpLoadFileData;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传工具类
 */
public  class FileUploadUtil {

    private  final  static Logger log= LoggerFactory.getLogger(FileUploadUtil.class);


    public  static  String upload(HttpServletRequest request, MultipartFile multipartFile) throws Exception {
    /*    long time = System.currentTimeMillis();*/
        //项目服务器地址路径
        String projectServerPath = Constant.IMAGE_URL;
        BaseJsonResponse baseJsonResponse=new BaseJsonResponse();
        String saveDirectory=FileUtil.getUploadDir();
        BaseJsonResponse baseJsonResponse1=FileUtil.upLoad(multipartFile,baseJsonResponse,saveDirectory);
        UpLoadFileData upLoadFileData= (UpLoadFileData) baseJsonResponse1.getData();
        String  newFileName= upLoadFileData.getFilename();
        log.info(newFileName+"----newFileName");
        return projectServerPath + newFileName;
    }
}
