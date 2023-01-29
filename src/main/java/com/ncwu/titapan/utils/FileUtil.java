package com.ncwu.titapan.utils;

import com.ncwu.titapan.constant.Constant;
import lombok.Builder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:18
 */
public class FileUtil {
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

        FileOutputStream out = new FileOutputStream(new File(outDir));
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
                    // 文件夹要递归调用
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
                        } else {
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
        return s.substring(index + 1);
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
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
            SequenceInputStream sequenceInputStream=new SequenceInputStream(vector.elements());
            //10字节的缓存
            byte[] cache = new byte[1024*10];
            int len = -1;
            while ((len=sequenceInputStream.read(cache))!=-1) {
                //分段写
                outputStream.write(cache,0,len);
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

    public static void main(String []args){

    }

}
