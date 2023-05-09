package com.ncwu.titapan.utils;

import com.ncwu.titapan.constant.Constant;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;


import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

/**
 * TODO 图片视频预览图处理 工具类
 *
 * @author ddwl.
 * @date 2023/2/5 18:40
 */
public class PreviewImageUtil {


    public static String createPreviewURL(String path, String suffix) {
        String preview_url = null;
        try {
            if (FileUtil.isPic(suffix)) {
                // 生成图片预览地址
                preview_url = PreviewImageUtil.get_preview_pic_url(new File(path), false);
            } else if (FileUtil.isVedio(suffix)) {
                // 获取视频第一张图片
                String framePath = Constant.preview_image_path + PreviewImageUtil.createRandomName(32) + ".jpg";

                PreviewImageUtil.getVideoFirstFrame(new File(path),
                        framePath);
                // 生成图片预览地址
                File frameFile = new File(framePath);
                preview_url = PreviewImageUtil.get_preview_pic_url(frameFile, false);
                frameFile.delete();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return preview_url;
    }

    /**
     * TODO 图片预览
     *
     * @return lang.String
     * @Author ddwl.
     * @Date 2023/2/5 18:41
    **/

    public static String get_preview_pic_url(File src, boolean keepQuality){

        String randomString = createRandomName(64);
        String suffix = src.getName().substring(src.getName().lastIndexOf('.'));
        String url = Constant.host_url + randomString + suffix;
        try {
            if(keepQuality){
                Files.copy(src.toPath(),
                        new File(
                        Constant.preview_image_path
                                + randomString
                                + suffix).toPath());
            }
            else {
                Thumbnails.of(src)
                        .size(300, 300) //图片大小（长宽）保留比例 多余填充
                        .outputQuality(1)
                        .toFile(new File(
                                Constant.preview_image_path
                                        + randomString
                                        + suffix));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String createRandomName(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random=new Random();

        StringBuffer sb=new StringBuffer();

        for(int i = 0; i < length; i++){
            int number=random.nextInt(62);    //从62个字符中随机取其中一个
            sb.append(str.charAt(number));  //用取到的数当索引取字符加到length个数的字符串
        }

        return sb.toString() + '-' + System.currentTimeMillis() + "-preview";  //返回字符串
    }


    /**
     * 获取视频第一帧
     * @param videoFile 视频文件
     * @param imgPath 生成图片的名字（包含全路径）
     * @throws Exception
     */
    public static void getVideoFirstFrame(File videoFile, String imgPath) throws Exception {
        File imgFile = new File(imgPath);
        //判断保存的文件的文件夹是否存在，不存在创建。
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs();
        }
        if (videoFile.exists()) {
            //实例化“截取视频首帧”对象
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            int ftp = ff.getLengthInFrames();
            int flag = 0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = ff.grabImage();
                //过滤前1帧，避免出现全黑图片
                if ((flag > 1) && (frame != null)) {
                    break;
                }
                flag++;
            }
            ImageIO.write(frameToBufferedImage(frame), "jpg", imgFile);
            ff.close();
            ff.stop();
        }
    }

    /**
     * 帧转为流
     * @param frame
     * @return
     */
    private static RenderedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public static String getFileNameFromUrl(String url){
        return url.substring(url.lastIndexOf('/') + 1);
    }



    public static void main(String[] args){
        // System.out.println(get_preview_pic_url(new File("C:\\Users\\Lenovo\\Videos\\新建文件夹\\level (5).png")));
        String s = "d08381d94e8d1cd8ba10a0be9b348d08.jpg";
        System.out.println(s.length());
    }


}
