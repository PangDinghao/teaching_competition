package com.example.yizhi.controller;

import com.example.yizhi.common.FilePathHolder;
import com.example.yizhi.utils.FileDownloadUtil;
import com.example.yizhi.utils.FileUploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileContoller {
    //上传文件
    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file) {
        String fileExtenstion = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName =  UUID.randomUUID().toString()+"_"+ file.getOriginalFilename();
        FileUploadUtil.uploadToServer(file, FilePathHolder.getFilePath(),fileName);

        return fileName;
    }

    //下载文件
    @GetMapping("/download")
    public void download( String fileName, HttpServletResponse response) {
        FileDownloadUtil.downloadToBrowser( FilePathHolder.getFilePath() + fileName,fileName,response);
    }
}
