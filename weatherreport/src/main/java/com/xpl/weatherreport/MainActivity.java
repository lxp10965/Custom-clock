package com.xpl.weatherreport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //解析天气xml信息
        getWeatherInfo();
    }

    private void getWeatherInfo() {
        try {
            XmlPullParser parser = Xml.newPullParser();

            InputStream inputStream = getAssets().open("getWeatherbyCityName.xml");
            /*
             *设置解析器将要处理的输入流。
             *此调用重置解析器状态并设置事件类型
             *设置为初始值START_DOCUMENT。
             */
            parser.setInput(inputStream, "utf-8");
            ArrayList<String> infos = new ArrayList<>();
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                Log.d("type: "+type, "XmlPullParser.END_DOCUMENT: "+XmlPullParser.END_DOCUMENT);
                if ("string".equals(parser.getName())) {
                    String info = parser.nextText();
                    infos.add(info);
                }
                type = parser.next();
            }
            inputStream.close();
            String cityname = infos.get(0);
            String temp = infos.get(1);
            String weather = infos.get(2);
            String wind = infos.get(3);
            String wearinfo = infos.get(4);
            TextView tv = (TextView) findViewById(R.id.tv_info);
            tv.setText("城市名称：" + cityname + "\n温度：" + temp + "\n天气信息：" + weather + "\n风力：" + wind + "\n穿衣指数：" + wearinfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
