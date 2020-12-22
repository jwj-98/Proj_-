package com.example.final_proj_wjj_hss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    TextView textViewFindid; // 아이디/비밀번호/계정생성 버튼
    TextView textViewFindpw;
    TextView textViewCreateAccount;
    Button btnLogin; // 로그인 버튼
    EditText editTextId;
    EditText editTextPw;
    MediaPlayer bgm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideNavigationBar();//하단바 숨김


        bgm = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();

        //로그인 이력이 있는지 확인합니다. 있다면 자동 로그인 합니다.
        SharedPreferences shareData = getSharedPreferences("shareData", MODE_PRIVATE);
        if(shareData.getBoolean("firstRun", true) == false) {
            bgm.stop();

            String id = shareData.getString("userId", null);
            String pw = shareData.getString("userPw", null);


            try {
                String result;
                LoginRegister login = new LoginRegister();
                result = login.execute(id, pw).get();

                if (result.equals("connected")) {//계정이 일치합니다.
                    String userInfos;
                    UserInfoRegister userInfoRegister = new UserInfoRegister();
                    userInfos = userInfoRegister.execute(id).get();
                    String[] userInfoArray = userInfos.split(";");
                    UserInfo userInfo = new UserInfo(userInfoArray[0],userInfoArray[1],Long.parseLong(userInfoArray[2]) );
                    if (userInfo.nick.equals("nick")){ //회원가입후 최초로그인시 닉네임 입력창으로 이동
                        Intent intent = new Intent(LoginActivity.this, CreateNick.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, userInfo.getNick() + "님의 계정으로 로그인합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, mainPage.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this, "자동 로그인에 실패했습니다. ID또는 비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                }


            }
            catch (Exception e) {
                Log.i("DB", ".......ERROR.......!");
            }
        }






        textViewFindid = (TextView) findViewById(R.id.textViewFindid); // 아이디/비밀번호/계정생성 버튼 id 연결
        textViewFindpw = (TextView) findViewById(R.id.textViewFindpw);
        textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        btnLogin = (Button) findViewById(R.id.btnLogin); // 로그인 버튼 id 연결
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextId.setFilters(new InputFilter[]{new InputFilter() {//한글비허용 설정
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                Toast.makeText(LoginActivity.this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        }});
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        editTextPw.setFilters(new InputFilter[]{new InputFilter() {//한글비허용 설정
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                Toast.makeText(LoginActivity.this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        }});




        textViewFindid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //ID 찾기버튼 이벤트
                Intent intent = new Intent(LoginActivity.this, FindAccount.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        textViewFindpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //PW 찾기버튼 이벤트
                Intent intent = new Intent(LoginActivity.this, FindAccount.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //회원가입버튼 이벤트
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //로그인버튼 이벤트

                String id = editTextId.getText().toString();
                String pw = editTextPw.getText().toString();

                if(id.length() == 0 || pw.length() == 0) {
                    //아이디와 비밀번호는 필수 입력사항입니다.
                    Toast toast = Toast.makeText(LoginActivity.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                // 아이디 pw를 체크합니다
                else {
                    try {// login 레지스터 객체 생성
                        String result;
                        LoginRegister login = new LoginRegister();
                        result = login.execute(id, pw).get();

                        if (result.equals("connected")) {//계정이 일치합니다.
                            bgm.stop();
                            SharedPreferences shareData = getSharedPreferences("shareData", MODE_PRIVATE);//최초에만 로그인하기때문에 최초실행 확인용 데이터 저장
                            shareData.edit().putBoolean("firstRun", false).apply();
                            shareData.edit().putString("userId", id).apply();
                            shareData.edit().putString("userPw", pw).apply();
                            String userInfos;
                            UserInfoRegister userInfoRegister = new UserInfoRegister();
                            userInfos = userInfoRegister.execute(id).get();
                            String[] userInfoArray = userInfos.split(";");
                            UserInfo userInfo = new UserInfo(userInfoArray[0],userInfoArray[1],Long.parseLong(userInfoArray[2]) );
                            if (userInfo.nick.equals("nick")){ //회원가입후 최초로그인시 닉네임 입력창으로 이동
                                Intent intent = new Intent(LoginActivity.this, CreateNick.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, userInfo.getNick() + "님의 계정으로 로그인합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, mainPage.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }


                        }
                        else {
                            Toast.makeText(LoginActivity.this, "로그인에 실패했습니다. ID또는 비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e) {
                        Log.i("DB", ".......ERROR.......!");
                    }
                }



            }
        });

    }


    /////////////////////////////////////activity환경설정 메소드입니다.///////////////////////////////
    //하단바 삭제 메소드
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
