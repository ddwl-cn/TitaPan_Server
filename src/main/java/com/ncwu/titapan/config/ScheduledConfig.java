package com.ncwu.titapan.config;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.mapper.FileChunkMapper;
import com.ncwu.titapan.pojo.FileChunk;
import com.ncwu.titapan.utils.DateUtil;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
/**
 * TODO 一些定时任务
 *
 * @author ddwl.
 * @date 2023/1/27 17:59
 */
@Configuration
@EnableScheduling   //开启定时任务
public class ScheduledConfig {

    @Autowired
    private FileChunkMapper fileChunkMapper;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledConfig.class);

    /**
     * TODO 定时清理垃圾块 每天23:59:59执行一次
     *
     * @return void
     * @Author ddwl.
     * @Date 2023/1/27 18:00
    **/
    @Scheduled(cron = "59 59 23 * * ?")
    public void clearRubbishChunks(){

        // 查询已经存储了一定时间的文件块(超过12小时)
        FileChunk[] fileChunks = fileChunkMapper.getRubbishChunks(DateUtil.getFormatDate());

        for (FileChunk fileChunk : fileChunks) {
            File file = new File(fileChunk.getStorage_path() + fileChunk.getTempName());
            // 删除表中信息和本地文件
            fileChunkMapper.deleteFileChunkByMD5(fileChunk.getMd5_val());
            if(!file.delete()){
                logger.error("文件" + fileChunk.getStorage_path() + fileChunk.getTempName() + "删除异常！");
            }
        }
        logger.info("垃圾文件清理完毕！");
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void deletePreviewFiles(){
        File file = new File(Constant.sys_preview_path);
        deleteFiles(file, 0);
    }

    private boolean deleteFiles(File file, int level) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            logger.info("文件未找到！");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFiles(f, level + 1);
            } else {
                //文件删除
                f.delete();
            }
        }
        //文件夹删除
        if(level != 0) file.delete();
        else{
            logger.info("垃圾文件清理完毕！");
        }
        return true;
    }
}
