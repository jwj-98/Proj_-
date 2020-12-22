package com.example.final_proj_wjj_hss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.in;
import static java.lang.System.out;

public class socketTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_test);

        Button btnConn = (Button) findViewById(R.id.btnConn);
        Button btnSend = (Button) findViewById(R.id.btnSend);
        EditText editTextSendMsg = (EditText) findViewById(R.id.editTextMsg);
        TextView textViewServerMsg = (TextView) findViewById(R.id.textViewServerMsg);

        btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketTestRegister so = new SocketTestRegister();
                so.execute();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
