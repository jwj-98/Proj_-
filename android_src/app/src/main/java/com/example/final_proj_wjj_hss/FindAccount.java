package com.example.final_proj_wjj_hss;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindAccount extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideNavigationBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        Button btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextFindAccountName = (EditText) findViewById(R.id.editTextFindAccountName);
                EditText editTextFindAccountEmail = (EditText) findViewById(R.id.editTextFindAccountEmail);
                String name = editTextFindAccountName.getText().toString();
                String email = editTextFindAccountEmail.getText().toString();


                try {
                    String result;
                    FindAccountRegister findAccount = new FindAccountRegister();
                    result = findAccount.execute(name, email).get();

                    if (result.contains("failed")) {//계정이 불일치합니다.

                        Toast.makeText(FindAccount.this, "계정찾기에 실패했습니다. 입력정보를 확인해주세요.", Toast.LENGTH_LONG).show();


                    }
                    else {

                        Toast.makeText(FindAccount.this, "계정정보가 email로 발송됩니다. email을 확인해주세요", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FindAccount.this, LoginActivity.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }

                }
                catch (Exception e) {
                    Log.i("DB", ".......ERROR.......!");
                }

            }
        });
    }



    /////////////////////////////////////activity환경설정 메소드입니다.///////////////////////////////
    @Override //액티비티 종료 애니에이션을 삭제합니다.
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }

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
}
