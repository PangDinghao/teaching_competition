package com.example.yizhi.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
public class FileDownloadUtil {


    /**
     * 下载文件到服务器
     *
     * @param filePath      要下载的文件的地址
     * @return
     */
    public static void downloadToBrowser(String filePath, String originalFileName, HttpServletResponse response) {
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                throw new FileNotFoundException("File not found: " + filePath);
            }

            bis = new BufferedInputStream(new FileInputStream(file));

            // 获取原始文件名并设置到响应头
            // // 使用ISO-8859-1编码来对文件名进行编码
            String encodedFileName = new String(originalFileName.substring(originalFileName.indexOf('_') + 1).getBytes("UTF-8"), "ISO-8859-1");
            System.out.println(encodedFileName);

            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setContentType("application/octet-stream");

            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
