package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.service.DownloadService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/8 16:10
 */
@Service
public class DownloadServiceImpl implements DownloadService {

    @Override
    public boolean downloadSingle(HttpServletResponse response, String res_f_name, String f_name) {

        File file = new File(Constant.sys_storage_path, f_name);
        if(file.exists() && file.isFile()){
            response.setHeader("Content-Disposition", "attachment;fileName=" + res_f_name);// 设置文件名
            response.setContentType("application/octet-stream");
            /*
            getResponseHeader()方法只能拿到6个基本字段：
            Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma
            */
            // 暴露header 中的 Content-Disposition信息 用于前端接收数据流
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            response.setHeader("Content-Length", String.valueOf(file.length()));
            // 缓冲区
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            // 读取byte数据到 outputstream 中
            try{
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = response.getOutputStream();
                int index = bufferedInputStream.read(buffer);
                while(index != -1){
                    outputStream.write(buffer, 0, index);
                    index = bufferedInputStream.read(buffer);
                }

            }
            catch(IOException e){
                e.printStackTrace();
                return false;
            }
            finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

}
