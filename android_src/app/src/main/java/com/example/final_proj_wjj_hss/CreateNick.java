package com.example.final_proj_wjj_hss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideNavigationBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_nick);


        Button btnOk = (Button) findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextCreateNick = (EditText) findViewById(R.id.editTextCreateNick);
                String nick = editTextCreateNick.getText().toString();
                if (nick.contains("gm") || nick.contains("GM") || nick.equals("nick")) {
                    Toast.makeText(CreateNick.this, "불가능합니다. 다시입력해주세요. ", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences shareData = getSharedPreferences("shareData", MODE_PRIVATE);
                    String id = shareData.getString("userId", null);
                    try {
                        CreateNickRegister createNickRegister = new CreateNickRegister();
                        createNickRegister.execute(id, nick);
                    } catch (Exception e) {
                    }

                    Intent intent = new Intent(CreateNick.this, mainPage.class);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
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
    @Override //액티비티 종료 애니에이션을 삭제합니다.
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }
}
