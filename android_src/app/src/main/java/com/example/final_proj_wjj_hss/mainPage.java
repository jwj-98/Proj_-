package com.example.final_proj_wjj_hss;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class mainPage extends AppCompatActivity {

    MediaPlayer bgm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        bgm = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();

        hideNavigationBar();//하단바 삭제


        SharedPreferences shareData = getSharedPreferences("shareData", MODE_PRIVATE);
        String id = shareData.getString("userId", null);




        //DB에서 내정보를 가져옵니다 (jsp통신)
        try { //user info register 통신 예외처리
            String userInfos;
            UserInfoRegister userInfoRegister = new UserInfoRegister();
            userInfos = userInfoRegister.execute(id).get();
            String[] userInfoArray = userInfos.split(";");
            UserInfo userInfo = new UserInfo(userInfoArray[0], userInfoArray[1], Long.parseLong(userInfoArray[2]));



            ImageButton gameStart = (ImageButton) findViewById(R.id.btnGameStart);
            Button btnLogout = (Button) findViewById(R.id.btnLogout);
            TextView textViewGold = (TextView) findViewById(R.id.textViewGold);
            DecimalFormat format = new DecimalFormat("###,###");
            textViewGold.setText(format.format(userInfo.getGold()) + " 원");


            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //자동 로그인 설정값 해제
                    bgm.stop();
                    SharedPreferences shareData = getSharedPreferences("shareData", MODE_PRIVATE);
                    shareData.edit().putBoolean("firstRun", true).apply();
                    shareData.edit().putString("userId", null).apply();
                    shareData.edit().putString("userPw", null).apply();
                    Intent intent = new Intent(mainPage.this, LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    finish();
                }
            });

            gameStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bgm.stop();
                    Intent intent = new Intent(mainPage.this, GameTable.class);
                    startActivity(intent);
                    finish();
                }
            });
        }catch (Exception e){}


    }



    /////////////////////////////////////activity환경설정 메소드입니다.///////////////////////////////
    //네비게이션바 삭제
    private void hideNavigationBar() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override //BGM 끊김 설정
    protected void onStop() {
        super.onStop();

        bgm.stop();
    }

    @Override //BGM 끊김 설정
    protected void onRestart() {
        super.onRestart();

        bgm = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();
    }

    @Override //액티비티 종료 애니에이션을 삭제합니다.
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }
}
