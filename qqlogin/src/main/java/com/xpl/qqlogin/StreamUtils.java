package com.xpl.qqlogin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
    public static String readStream(InputStream is) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer =new byte[1024];
        int len = 0;
        while ((len = is.read(buffer))!=-1){

            bos.write(buffer,0,len);
        }
        is.close();
        String sesult = bos.toString();
        if (sesult.contains("gb2312")){
            return bos.toString("gb2312");
        }

        return sesult;
    }
    public static Bitmap readBitmap(InputStream is){
        return BitmapFactory.decodeStream(is);
    }
}
