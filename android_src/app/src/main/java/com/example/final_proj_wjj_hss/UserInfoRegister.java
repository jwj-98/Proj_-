package com.example.final_proj_wjj_hss;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserInfoRegister extends AsyncTask<String, Void, String> {


    String sendMsg;
    String receiveMsg;




    @Override
    protected String doInBackground(String... strings) {

        try {
            String str;

            // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
            URL url = new URL("http://1.225.83.125:8090/jsp_server/getUserInfo.jsp");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);                         // 서버에서 읽기 모드 지정
            conn.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            conn.setRequestMethod("POST");                // 전송방식 POST
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            // 전송할 데이터. GET 방식으로 작성후 post 전송
            sendMsg = "id=" + strings[0];

            osw.write(sendMsg);
            osw.flush();

            //jsp 와 통신 성공 시 수행
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // jsp 에서 보낸 값을 받는 부분
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                // 통신 실패
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //jsp 로부터 받은 리턴 값
        return receiveMsg;
    }
}
