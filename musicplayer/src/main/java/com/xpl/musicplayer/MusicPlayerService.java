package com.xpl.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class MusicPlayerService extends Service {

    public static final String TAG = "MusicPlayerService";
    private SharedPreferences sp;//轻量级储存
    MediaPlayer mediaPlayer;//媒体播放机
    public static int playingStatus;//当前播放状态
    public static final int MUSIC_STOP = 0;//播放状态为暂停
    private OnMusicLengthListener musicLengthListener;

    public MusicPlayerService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: " + "音乐播放服务开启了");
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    /**
     * 提供给外界调用的入口
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new MyBinder();
    }

    private class MyBinder extends Binder implements IMusicService {
        /**
         * 调用服务里面的播放逻辑
         *
         * @param playList 音乐资源的路径集合
         * @param position
         */
        @Override
        public void callPlay(List<String> playList, int position) {
            play(playList, position);
        }

        /**
         * 调用停止播放的方法
         */
        @Override
        public void callStop() {
            stopPlayer();
        }
    }

    /**
     * 播放音乐
     *
     * @param playList 音乐列表
     * @param position 当前位置
     */
    public void play(final List<String> playList, final int position) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();//释放资源
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.d(TAG, "play: position = " + position);
            mediaPlayer.setDataSource(playList.get(position)); //设置要使用的数据源(文件路径或http/rtsp URL)。
            mediaPlayer.prepare(); //同步准备播放器的播放。
            mediaPlayer.start();
            if (musicLengthListener!=null){
                musicLengthListener.getMaxMusicLength(mediaPlayer.getDuration());
            }
            //发消息更新进度
            handler.sendEmptyMessage(0);
            final String path = playList.get(position);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int mode = sp.getInt("mode", 0);
                    if (mode == SettingAcitity.CYCLE) {
                        play(playList, position);// 播放
                    } else if (mode == SettingAcitity.NEXT) {
                        //播放下一曲
                        int newPosition = position + 1;
                        if (newPosition >= playList.size()) {
                            newPosition = 0;
                        }
                        play(playList, position);
                    } else if (mode == SettingAcitity.STOP) {
                        playingStatus = MUSIC_STOP;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            playingStatus = MUSIC_STOP;
        }
    }

    //暂停
    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 音乐数据传递接口
     */
    interface OnMusicLengthListener {
        //音乐的长度
        void getMaxMusicLength(long length);

        //当前播放的位置
        void getMusicCurr(long curr);
    }

    public void setOnMusicLengthListener(OnMusicLengthListener m) {
        this.musicLengthListener = m;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (musicLengthListener != null) {
                musicLengthListener.getMusicCurr(mediaPlayer.getCurrentPosition());
                Log.d(TAG, "handleMessage: getMusicCurr = ");
            }
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };
}
