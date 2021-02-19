package com.xpl.htmlviewer.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 流的一个工具类
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class StreamUtils {

    /**
     * 读取流的数据返回一个字符串
     * @param is
     *          获取的流
     * @return  字符串 如果解析失败返回null
     */
    public static String readStream(InputStream is) {
        try {
            //字节流 ， 输出流 ， 缓冲流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            //一次性读1024个字节
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            String result = baos.toString();
            if (result.contains("gb2312")) {
                return baos.toString("gb2312");
            } else if (result.contains("gbk")) {
                return baos.toString("gbk");
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
