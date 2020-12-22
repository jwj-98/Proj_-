package com.example.final_proj_wjj_hss;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class Bgm extends Service {

    MediaPlayer bgm;

    public Bgm() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        bgm  = MediaPlayer.create(this, R.raw.bgm);
//        bgm.setLooping(true);
//        bgm.start();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        bgm  = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        bgm.stop();

    }





}
