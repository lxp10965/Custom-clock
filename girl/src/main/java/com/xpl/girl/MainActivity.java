package com.xpl.girl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView iv;
    private int startX;//开始触摸的X
    private int startY;//开始触摸的Y
    private Bitmap newBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();//初始化视图
        initData();//初始化数据
        initEvent();//初始化点击

    }

    private void initEvent() {
        iv.setOnTouchListener(this);
    }

    Bitmap bitmap;
    private void initData() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pre19);
        // 得到原图的拷贝
        newBitmap = bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        //创建画板
        Canvas canvas = new Canvas(newBitmap);
        Paint p = new Paint();//画笔
        p.setColor(Color.BLACK);//
        canvas.drawBitmap(bitmap, new Matrix(), p); //传入图片，矩形，画笔
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
    }

    /**
     * 当一个触摸事件被分派到一个视图时调用。这允许侦听器在目标视图之前获得响应机会。
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                Log.d("startX = " + startX, "startY: " + startY);
                newBitmap.setPixel(startX, startY, Color.TRANSPARENT); //设置透明
                iv.setImageBitmap(newBitmap);
                break;
            case MotionEvent.ACTION_MOVE:
                int newX = (int) event.getX();
                int newY = (int) event.getY();
                Log.d("newX = " + newX, "newY: " + newY);
                for (int i = -3; i < 4; i++) {
                    for (int j = -3; j < 4; j++) {  //绘制 7*7=49格
                        newBitmap.setPixel(newX + i, newY + j, Color.TRANSPARENT);
                    }
                }
//                newBitmap.setPixel(newX , newY , Color.TRANSPARENT);
                iv.setImageBitmap(newBitmap);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;

        }
        return true;
    }
}
