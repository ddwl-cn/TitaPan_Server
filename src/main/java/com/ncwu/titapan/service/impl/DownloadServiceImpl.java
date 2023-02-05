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

    /**
     * TODO 文件下载 写入OutputStream
     *
     * @param response response
     * @param res_f_name 设置下载文件名
     * @param f_name 服务端存储的文件的名称
     * @return boolean
     * @Author ddwl.
     * @Date 2023/2/5 11:10
    **/
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
            // 读取byte数据到 OutputStream 中
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
                try {
                    if (bufferedInputStream != null) bufferedInputStream.close();
                    if (fileInputStream != null) fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

}
