package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

@Controller
public class FileUploadController {
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public BaseJsonResponse upload(@RequestParam("file") MultipartFile file) throws FileNotFoundException, ParseException {
        BaseJsonResponse resObj = new BaseJsonResponse();

        String saveDirectory = FileUtil.getUploadDir();
        return FileUtil.upLoad(file, resObj, saveDirectory);
    }

    @RequestMapping(value = "/upload/batch", method = RequestMethod.POST)
    @CrossOrigin
    @ResponseBody
    public BaseJsonResponse batchUpload(HttpServletRequest request) throws ParseException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file;
        BaseJsonResponse resObj = new BaseJsonResponse();
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            String saveDirectory = "/images";
            return FileUtil.upLoad(file, resObj, saveDirectory);
        }
        return resObj;
    }


}
