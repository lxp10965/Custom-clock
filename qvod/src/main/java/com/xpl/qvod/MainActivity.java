package com.xpl.qvod;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pb;
    private SurfaceView sv;
    private SeekBar seekBar1;
    private SharedPreferences sp;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("SurfaceView被创建了");
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource("http://vfx.mtime.cn/Video/2016/07/19/mp4/160719095812990469.mp4");

                    // 播放视频必须设置播放的容器，通过sv得到他的holder
                    mediaPlayer.setDisplay(sv.getHolder());
                    mediaPlayer.prepareAsync();// 异步的准备，开启子线程
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        //在播放过程中到达媒体源的末尾时调用
                        @Override
                        public void onCompletion(MediaPlayer mp) {  //mp 到达文件末尾的MediaPlayer
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("position", 0);
                            editor.commit();
                        }
                    });
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        /**
                         *在媒体文件准备好播放时调用。
                         *
                         * @param mp 准备好播放的MediaPlayer
                         */
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            pb.setVisibility(View.INVISIBLE);
                            mediaPlayer.start();
                            int position = sp.getInt("position", 0);
                            mediaPlayer.seekTo(position);
                            timer = new Timer();
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    int max = mediaPlayer.getDuration();
                                    int progress = mediaPlayer.getCurrentPosition();
                                    seekBar1.setMax(max);
                                    seekBar1.setProgress(progress);
                                }
                            };
                            timer.schedule(task, 0, 500);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("SurfaceView被销毁了");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int position = mediaPlayer.getCurrentPosition();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("position", position); //记忆播放位置
                    editor.commit();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                }
            }
        });
    }

    private void initData() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        pb = (ProgressBar) findViewById(R.id.pb);
        sv = (SurfaceView) findViewById(R.id.sv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("----------------------------");
            seekBar1.setVisibility(View.VISIBLE);
            new Thread(){
                @Override
                public void run() {
                    // 3秒隐藏进度条
                    SystemClock.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar1.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }.start();
        }
        return super.onTouchEvent(event);
    }
}
