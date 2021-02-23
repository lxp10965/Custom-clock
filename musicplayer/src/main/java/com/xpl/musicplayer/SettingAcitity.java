package com.xpl.musicplayer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class SettingAcitity extends Activity {

    public static final String TAG = "SettingAcitity";
    public static final int CYCLE = 1;
    public static final int NEXT = 2;
    public static final int STOP = 3;
    private RadioGroup rb_mode;
    /**
     * 轻量级储存
     */
    private SharedPreferences sp;
    private SeekBar seekBar;
    private MyMusicPlayerService musicPlayerService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        rb_mode = (RadioGroup) findViewById(R.id.rb_mode);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        int mode = sp.getInt("mode", 0);
        RadioButton rb;
        switch (mode) {
            case CYCLE:
                rb = findViewById(R.id.rb_single_cycle);
                rb.setChecked(true);
                break;
            case NEXT:
                rb = findViewById(R.id.rb_single_cycle);
                rb.setChecked(true);
                break;
            case STOP:
                rb = findViewById(R.id.rb_single_cycle);
                rb.setChecked(true);
                break;

            default:
                break;
        }

        rb_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //轻量级储存
                SharedPreferences.Editor editor = sp.edit();
                switch (checkedId) {
                    case R.id.rb_single_cycle:
                        editor.putInt("mobe", CYCLE);
                        break;
                    case R.id.rb_next_song:
                        editor.putInt("mobe", NEXT);
                        break;
                    case R.id.rb_play_and_stop:
                        editor.putInt("mobe", STOP);
                        break;

                    default:
                        break;
                }
                editor.commit();//记忆选择的播放模式
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        musicPlayerService= new MusicPlayerService();
//        musicPlayerService.setOnMusicLengthListener(new MusicPlayerService.OnMusicLengthListener() {
//            @Override
//            public void getMaxMusicLength(long length) {
//                Log.d("TAG", "getMaxMusicLength: ");
//                seekBar.setMax((int) length);
//            }
//
//            @Override
//            public void getMusicCurr(long curr) {
//                Log.d(TAG, "getMusicCurr: ");
//                seekBar.setProgress((int) curr);
//            }
//        });

        musicPlayerService=new MyMusicPlayerService();

    }

    private class MyMusicPlayerService implements MusicPlayerService.OnMusicLengthListener {
        @Override
        public void getMaxMusicLength(long length) {
            Log.d("TAG", "getMaxMusicLength: ");
                seekBar.setMax((int) length);
        }

        @Override
        public void getMusicCurr(long curr) {
            Log.d(TAG, "getMusicCurr: ");
                seekBar.setProgress((int) curr);
        }
    }
}
