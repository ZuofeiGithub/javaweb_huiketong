package com.huiketong.cofpasgers.util;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.json.data.UpLoadFileData;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.json.response.KErrorditorResp;
import com.huiketong.cofpasgers.json.response.KSuccessditorResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Date;

public class FileUtil {

    private final static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + "/" + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static BaseJsonResponse upLoad(MultipartFile file, BaseJsonResponse response, String saveDirectory) throws ParseException {
        if (file != null && !file.isEmpty()) {
            //获取上传的文件名
            String tempfile = file.getOriginalFilename();
            String suffix = tempfile.substring(tempfile.lastIndexOf(".") + 1);
            String fileName = DateUtils.dateFormat(new Date(), DateUtils.LINE_DATETIME_PATERN);
            String holname = fileName + "." + suffix;
            try {
                FileUtil.uploadFile(file.getBytes(), saveDirectory, holname);
            } catch (Exception e) {
                e.printStackTrace();

                response.setMsg("上传失败");
                response.setCode("-1");
            }
            UpLoadFileData data = new UpLoadFileData();
            data.setFilename(holname);
            data.setUploadDate(new Date());
            response.setData(data);
            response.setMsg("上传成功");
            response.setCode("0");
        } else {
            response.setMsg("文件不能为空");
            response.setCode("-1");
        }
        return response;
    }

    public static String getUploadDir() throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");
        //如果上传目录为/static/images/uploadfile/，则可以如下获取：
//        File upload = new File(path.getAbsolutePath(), "static/images/");
//        File upload = new File("/www/");
//        if(!upload.exists()) upload.mkdirs();
//        String saveDirectory = upload.getAbsolutePath();
        String saveDirectory = "/www/static/images";
        log.info("uploadfile url:" + saveDirectory);
        System.out.println("uploadfile url:" + saveDirectory);
        return saveDirectory;
    }
}
