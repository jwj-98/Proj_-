package com.example.final_proj_wjj_hss;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class SocketTestRegister extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;




    @Override
    protected String doInBackground(String... strings) {

        final String SERVER_IP = "1.225.83.125";
        final int SERVER_PORT = 9000;
        Socket socket = null;

        try {
            // 2. 서버와 연결을 위한 소켓을 생성
            socket = new Socket();

            // 3. 생성한 소켓을 서버의 소켓과 연결(connect)
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }



}
