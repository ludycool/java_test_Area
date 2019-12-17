package com.topband.bluetooth.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUtil {

    public static String getImageType(InputStream io) {
        return getType(io);
    }

    public static String getImageType(File file) {
        return getType(file);
    }

    private static String getType(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(obj);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                throw new RuntimeException("No readers found!");
            }
            ImageReader reader = iter.next();
            return reader.getFormatName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片base64字符串转InputStream
     * 
     * @param base64string
     * @return InputStream
     */
    public static InputStream baseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes1 = Base64.decodeBase64(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            return null;
        }
        return stream;
    }

    public static void main(String[] args) {
        String str = getImageStr();
        System.out.println(str);
        GenerateImage(str);
        InputStream io = baseToInputStream(str);
        System.out.println(getImageType(io));
    }

    public static String getMd5ByFile(InputStream io) {
        String value = "";
        try {
            value = org.apache.commons.codec.digest.DigestUtils.md5Hex(io);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getMD5(String imagePath) throws NoSuchAlgorithmException, IOException {
        InputStream in = null;

        try {
            in = new FileInputStream(new File(imagePath));

            StringBuffer md5 = new StringBuffer();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = in.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();

            // convert the byte to hex format
            for (int i = 0; i < mdbytes.length; i++) {
                md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return md5.toString().toLowerCase();
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    public static String getImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "C://Users//Administrator//Desktop//照片//7.jpg";// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    // base64字符串转化成图片
    public static boolean GenerateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = "d://222.jpg";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getFileLength(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0L;
    }
    
    /**
     * 根据文件流判断图片类型
     * @param is
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(InputStream is) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            is.read(b, 0, b.length);
            String type = HexUtil.byteToHexStr(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return "jpg";
            } else if (type.contains("89504E47")) {
                return "png";
            } else if (type.contains("47494638")) {
                return "gif";
            } else if (type.contains("424D")) {
                return "bmp";
            } else {
                return "unknown";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
