package com.xpl.dialogs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click05(View view) {
        final ProgressDialog pd =new ProgressDialog(this);
        pd.setTitle("请稍后>>>");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("加载中。。。。");
        pd.setMax(100);
        pd.setCancelable(false); //设置该对话框是否可取消
        pd.show();

        new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i=0;i<100;i++){
                    try {
                        Thread.sleep(100);
                        pd.setProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pd.dismiss();
            }
        }.start();
    }

    /**
     * 进度对话框
     *
     * @param view
     */
    public void click04(View view) {
        final ProgressDialog pd =new ProgressDialog(this);
        pd.setTitle("请稍后>>>");
        pd.setMessage("加载中。。。。");
        pd.show();
        //创建个线程，模拟加载四秒钟
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }.start();
    }

    /**
     * 多选对话框
     *
     * @param view
     */
    public void click03(View view) {
        // 工厂设计模式，得到创建对话框的工厂
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择您爱吃的水果：");
        final String[] items = {"苹果", "梨子", "香蕉", "菠萝", "哈密瓜"};
        final boolean[] checkeds = {true, false, true, false, true};
        builder.setMultiChoiceItems(items, checkeds, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Toast.makeText(MainActivity.this, "" + items[which] + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i=0;i<checkeds.length;i++){
                    if (checkeds[i]){
                        stringBuffer.append(items[i]+" , ");
                    }
                    Toast.makeText(MainActivity.this,"您喜欢吃的水果是"+stringBuffer.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();

    }

    /**
     * 单选对话框
     *
     * @param view
     */
    public void click02(View view) {
        // 工厂设计模式，得到创建对话框的工厂
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择你的性别：");
        final String[] items = {"男", "女", "中性"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "您的性别为" + items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss(); //关闭对话框
            }
        });
//        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "您的性别为确定为："+items[which], Toast.LENGTH_SHORT).show();
//            }
//        });
        builder.show();
    }

    /**
     * 弹出确定取消对话框
     *
     * @param view
     */
    public void click01(View view) {
        // 工厂设计模式，得到创建对话框的工厂
//        Toast.makeText(MainActivity.this, "啊...即便自宫，也不一定能成功", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告");
        builder.setMessage("若练此功，必先自宫，是否继续？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "啊...即便自宫，也不一定能成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "如果不自宫，一定不成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show(); //显示对话框

    }
}
