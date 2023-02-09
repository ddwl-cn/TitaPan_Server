package com.ncwu.titapan.utils;

import com.ncwu.titapan.config.ScheduledConfig;
import com.ncwu.titapan.constant.Constant;
import lombok.Builder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:18
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /**
     *MD5计算工具
     */
    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String MD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 求一个字符串的md5值
     * @param target 字符串
     * @return md5 value
     */
    public static String MD5(String target) {
        return DigestUtils.md5Hex(target);
    }

    // 文件夹或文件列表的·压缩

    private static final int  BUFFER_SIZE = 2 * 1024;

    /**
     * TODO 打包指定的文件包括文件夹
     *
     * @param srcDir srcDir 是一个文件路径集合 也就是这个集合中的文件和文件夹将会被压缩打包
     * @param outDir outDir 压缩包输出路径
     * @param KeepDirStructure KeepDirStructure
     *                         为true时保留文件夹结构 不建议为false(必须确保不重名)
     *
     * @Author ddwl.
     * @Date 2022/5/28 19:19
     **/
    public static void zipDir(String[] srcDir, String outDir,
                              boolean KeepDirStructure) throws RuntimeException, Exception {

        FileOutputStream out = new FileOutputStream(outDir);
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            List<File> sourceFileList = new ArrayList<>();
            for (String dir : srcDir) {


                File sourceFile = new File(dir);

                sourceFileList.add(sourceFile);
            }
            compress(sourceFileList, zos, KeepDirStructure);

        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * TODO 压缩重载1 针对文件
     *
     * @param sourceFile sourceFile
     * @param zos zos
     * @param name name
     * @param KeepDirStructure KeepDirStructure
     *
     * @Author ddwl.
     * @Date 2022/5/28 19:22
     **/
    private static void compress(File sourceFile, ZipOutputStream zos,
                                 String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }

            zos.closeEntry();
            in.close();
        }
        else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }

            }
            else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(),
                                KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }

                }
            }
        }
    }

    /**
     * TODO 压缩重载2 针对子文件夹
     *
     * @param sourceFileList sourceFileList
     * @param zos zos
     * @param KeepDirStructure KeepDirStructure
     *
     * @Author ddwl.
     * @Date 2022/5/28 19:25
     **/
    private static void compress(List<File> sourceFileList,
                                 ZipOutputStream zos, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        for (File sourceFile : sourceFileList) {
            String name = sourceFile.getName();
            if (sourceFile.isFile()) {
                zos.putNextEntry(new ZipEntry(name));
                int len;
                FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            } else {
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    if (KeepDirStructure) {
                        zos.putNextEntry(new ZipEntry(name + "/"));
                        zos.closeEntry();
                    }

                } else {
                    for (File file : listFiles) {
                        if (KeepDirStructure) {
                            compress(file, zos, name + "/" + file.getName(),
                                    KeepDirStructure);
                        }
                        else {
                            compress(file, zos, file.getName(),
                                    KeepDirStructure);
                        }

                    }
                }
            }
        }
    }

    public static String getFileSuffix(String s){
        int index = s.lastIndexOf('.');
        if(index == -1) return "";
        return s.substring(index);
    }

    public static String getFileNameFromPath(String s){
        int index = s.lastIndexOf('/');
        if(index == -1) return "";
        return s.substring(index + 1);
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /**
     * TODO 当前文件夹下的文件(不包括子文件夹)
     * @param folderPath folderPath
     * @return List<java.io.File>
     * @Author ddwl.
     * @Date 2023/1/6 8:45
    **/
    public static List<File> getAllFileUnderFolder(String folderPath){
        List<File> fileList = new ArrayList<>();
        File folder = new File(folderPath);
        if(folder.isFile() || !folder.exists()){
            return fileList;
        }
        File[] files = folder.listFiles();
        for (File file : files) {
            // 是文件才添加
            if(file.isFile()){
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     *
     * @Description:   合并文件
     * @param:         @param chunkFileList 文件块列表
     * @param:         @param mergeFileDir 合并的文件存储的文件夹
     * @param:         @param mergeFileName 合并的文件新名
     * @return:        String 合并的文件路径
     */
    public static String merge(List<String> chunkFileList,String mergeFileDir,String mergeFileName) {

        try {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(mergeFileDir).append(mergeFileName);
            String newMergeFile=stringBuilder.toString();
            //输出流，写文件,true表示追加写而不覆盖
            OutputStream outputStream=new BufferedOutputStream(new FileOutputStream(newMergeFile,true));
            //输入流，读文件
            Vector<InputStream> vector=new Vector<>();
            for (int i = 0; i < chunkFileList.size(); i++) {
                vector.add(new BufferedInputStream(new FileInputStream(chunkFileList.get(i))));
            }
            //SequenceInputStream，实现批量输入流的按序列读
            SequenceInputStream sequenceInputStream = new SequenceInputStream(vector.elements());
            //10字节的缓存
            byte[] cache = new byte[1024*10];
            int len = -1;
            while ((len=sequenceInputStream.read(cache))!=-1) {
                outputStream.write(cache, 0, len);
            }
            //强制将所有缓冲的输出字节被写入磁盘，更可靠
            outputStream.flush();
            outputStream.close();
            sequenceInputStream.close();
            //返回新合成的文件路径
            return newMergeFile;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件未找到",e);
        } catch (IOException e) {
            throw new RuntimeException("文件传输异常",e);
        }

    }

    // 删除传入的文件列表
    public static void deleteTempFile(List<String> tempFileList){
        for (String s : tempFileList) {
            File file = new File(s);
            if(file != null){
                file.delete();
            }
        }
    }

    public static boolean deleteFiles(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFiles(f);
            }
        }
        //文件夹删除
        file.delete();

        return true;
    }

    /**
     * TODO 解决mo4编码格式导致的在线播放只有声音没有音频的问题 (修改mp4编码格式为h264)
     *
     * @param source source
     * @param target target
     * @Author ddwl.
     * @Date 2023/2/5 15:51
    **/
    public static void convertMP4EncodeType(File source, File target){
        MultimediaObject multimediaObject=new MultimediaObject(source);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");//音频编码格式
        //audio.setBitRate(new Integer(56000));//设置比特率，比特率越大，转出来的音频越大（默认是128000，最好默认就行，有特殊要求再设置）
        audio.setChannels(1);
        audio.setSamplingRate(22050);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("libx264");//视屏编码格式
        //video.setBitRate(new Integer(56000));//设置比特率，比特率越大，转出来的视频越大（默认是128000,最好默认就行，有特殊要求再设置）
        try {
            video.setFrameRate((int) multimediaObject.getInfo().getVideo().getFrameRate());//设置视频帧率 设置为与原视频一样的帧率
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();

        try {
            encoder.encode(multimediaObject,target,attrs);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPic(String f_suffix){
        String []suffix = {".jpg", ".jpeg", ".png", ".bmp"};
        return Arrays.asList(suffix).contains(f_suffix.toLowerCase());
    }
    public static boolean isVedio(String f_suffix){
        // 视频格式暂时只支持mp4
        String []suffix = {".mp4"};
        return Arrays.asList(suffix).contains(f_suffix.toLowerCase());
    }

    // 文件加密后存储
    public static void encodeFile(File srcFile, File desFile) {
        int fileData;
        if (!srcFile.exists()) {
            logger.error("创建目标文件失败");
            return;
        }
        try (InputStream inputStream = new FileInputStream(srcFile);
             OutputStream outputStream = new FileOutputStream(desFile)){
            while ((fileData = inputStream.read()) > -1) {
                // 利用异或两次数据不发生变化的特点
                outputStream.write(fileData ^ Constant.CRYPTO_SECRET_KEY);
            }
            outputStream.flush();
        }catch (Exception e){
            logger.error("加密上传文件失败",e);
        }
    }


    public static void main(String []args){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println(MD5(new File("D:\\ys\\ff7\\3DMGAME_Final_Fantasy_VII_Remake.CHS.Green\\Final Fantasy VII Remake Intergrade\\End\\Content\\Paks\\pakchunk0_s1-WindowsNoEditor.pak")));
        stopWatch.stop();
        System.out.println("计算耗时：" + stopWatch.getTotalTimeSeconds());
    }

}
