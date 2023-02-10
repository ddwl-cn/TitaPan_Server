package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.CustomFile;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.DownloadService;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/8 16:10
 */
@Service
public class DownloadServiceImpl implements DownloadService {
    @Autowired
    UserFileListMapper userFileListMapper;
    @Autowired
    FileMapper fileMapper;

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

    @Override
    public boolean downloadFolder(HttpServletResponse response,
                                  int uid,
                                  String folderPath,
                                  String folderName) {
        String path = createFolderStructure(uid, folderPath, folderName);
        String []src = new String[1];
        src[0] = path + folderName;
        try {
            File zipFile = new File(path + folderName + ".zip");
            zipFile.createNewFile();
            // 打包成压缩包
            FileUtil.zipDir(src, path + folderName + ".zip", true);

            File file = new File(path + folderName + ".zip");
            // 返回文件流
            if(file.exists() && file.isFile()){
                response.setHeader("Content-Disposition", "attachment;fileName=" + folderName + ".zip");// 设置文件名
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
                }
                finally {
                    try {
                        if (bufferedInputStream != null) bufferedInputStream.close();
                        if (fileInputStream != null) fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 删除本地文件
            FileUtil.deleteFiles(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //在本地创建目录结构
    private String createFolderStructure(int uid,
                               String folderPath,
                               String folderName){
        UserFileList[] folderList = userFileListMapper.getAllFolderUnderFolder(uid, folderPath, folderName);
        String randomName = PreviewImageUtil.createRandomName(64);

        String path = Constant.zip_storage_path + randomName;
        File file = new File(path + "/" + folderName);
        if(!file.exists()){
            file.mkdirs();
        }
        for (UserFileList userFileList : folderList) {
            // 创建所有的文件夹
            file = new File(path + "/"
                    + userFileList.getStorage_path()
                    .substring(userFileList.getStorage_path().indexOf(folderName))
                    + userFileList.getF_name());
            if(!file.exists()){
                file.mkdirs();
            }
        }

        UserFileList[] fileList = userFileListMapper.getAllFileUnderFolder(uid, folderPath, folderName);
        for (UserFileList userFileList : fileList) {
            try {
                file = new File(path + "/"
                        + userFileList.getStorage_path().
                        substring(userFileList.getStorage_path().indexOf(folderName))
                        + userFileList.getF_name());
                CustomFile customFile = fileMapper.getFileInfoByFid(userFileList.getFid());
                String realPath = customFile.getStorage_path() + customFile.getF_name();
                if (!file.exists()) {
                    File src = new File(realPath);
                    // 文件都复制到对应的文件夹下
                    Files.copy(src.toPath(), file.toPath());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 返回根文件夹路径
        return path + "/";
    }
    public static void main(String[] args){
        String s = "/666/6/666/666/666/666/";
        System.out.println(s.substring(s.indexOf("666")));
    }
}
