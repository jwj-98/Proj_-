package com.example.final_proj_wjj_hss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideNavigationBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText editTextName = (EditText) findViewById(R.id.editTextName);
        final EditText editTextCreateId = (EditText) findViewById(R.id.editTextCreateId);
        editTextCreateId.setFilters(new InputFilter[]{new InputFilter() {//한글비허용 설정
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                Toast.makeText(SignupActivity.this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        }});

        final EditText editTextCreatePw = (EditText) findViewById(R.id.editTextCreatePw);
        editTextCreatePw.setFilters(new InputFilter[]{new InputFilter() {//한글비허용 설정
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                Toast.makeText(SignupActivity.this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        }});
        final EditText editTextCheckPw = (EditText) findViewById(R.id.editTextCheckPw);
        editTextCheckPw.setFilters(new InputFilter[]{new InputFilter() {//한글비허용 설정
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                Toast.makeText(SignupActivity.this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        }});
        final EditText editTextEmail = (EditText) findViewById(R.id.editTextUserEmail);
        Button btnSignup = (Button) findViewById(R.id.btnSingup);
        RadioGroup rgGender = (RadioGroup) findViewById(R.id.gender);
        RadioButton rbMan = (RadioButton) findViewById(R.id.man);
        rbMan.setChecked(true); //오류방지 라디오버튼 눌러놓기


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup rgGender = (RadioGroup) findViewById(R.id.gender);
                boolean signupAccess = true; //회원가입 양식에 맞을경우 true 설정 -> 새로운 background Ascyntask 에서 jsp로 데이터 전송

                int checkedGender = rgGender.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(checkedGender);

                String name = editTextName.getText().toString();
                String id = editTextCreateId.getText().toString();
                String pw = editTextCreatePw.getText().toString();
                String chkPw = editTextCheckPw.getText().toString();
                String email = editTextEmail.getText().toString();

                //입력되지 않은 값이 있을때 처리
                if ( (name.length() <= 0) || (id.length() <= 0) || (pw.length() <= 0) || (chkPw.length() <= 0) || (email.length() <= 0)) {
                    Toast.makeText(SignupActivity.this, "입력하지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    signupAccess = false;
                }

                else if ( id.length() < 6) {
                    Toast.makeText(SignupActivity.this, "'ID' 6자이상 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                    signupAccess = false;
                }

                else if ( pw.length() < 8) {
                    Toast.makeText(SignupActivity.this, "'비밀번호' 8자이상 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                    signupAccess = false;
                }


                else if (pw.equals(chkPw)) {

                }
                else {//비밀번호가 일치하지 않을경우 toast 출력후 데이터 전송하지않음
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    signupAccess = false;
                }

                if (signupAccess) {//signupAccess가 true일경우 jsp로 데이터를 전송합니다.
                    try {
                        String result;
                        SignupRegister signup = new SignupRegister();
                        result = signup.execute(name, id, pw, email, rb.getText().toString()).get();
                        Toast.makeText(SignupActivity.this, result, Toast.LENGTH_SHORT).show();
                        if (result.equals("회원 가입이 완료되었습니다 !")) {
                            finish();
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
