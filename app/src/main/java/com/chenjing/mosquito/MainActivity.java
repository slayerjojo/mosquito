package com.chenjing.mosquito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer _player = new MediaPlayer();
    private Thread _thread = new Thread();
    private int _state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void initView() {
        Button btnPlay = (Button) findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == _state) {
                    _state = 1;
                } else {
                    _state = 0;
                }
            }
        });
    }

    private void init() {
        initView();

        _thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (-1 != _state) {
                    if (0 == _state) {
                        if (_player.isPlaying())
                            _player.stop();
                    } else if (1 == _state) {
                        if (_player.isPlaying())
                            continue;
                        _state = 2;

                        try {
                            _player.release();
                            _player = new MediaPlayer();
                            AssetFileDescriptor fd = getAssets().openFd("wz.mp3");
                            _player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());

                            _player.prepare();
                            _player.setLooping(false);
                            _player.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (2 == _state) {
                        if (_player.isPlaying())
                            continue;
                        _state = 3;

                        try {
                            _player.release();
                            _player = new MediaPlayer();
                            AssetFileDescriptor fd = getAssets().openFd("bf.mp3");
                            _player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());

                            _player.prepare();
                            _player.setLooping(false);
                            _player.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (3 == _state) {
                        if (_player.isPlaying())
                            continue;
                        _state = 1;

                        try {
                            _player.release();
                            _player = new MediaPlayer();
                            AssetFileDescriptor fd = getAssets().openFd("mq.mp3");
                            _player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());

                            _player.prepare();
                            _player.setLooping(false);
                            _player.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                _state = 0;
            }
        });
        _thread.start();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(this, "权限已获取！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "拒绝权限，将无法使用程序。", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (_player.isPlaying())
            _player.stop();

        _state = -1;
    }
}