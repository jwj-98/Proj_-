package com.example.final_proj_wjj_hss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class GameTable extends AppCompatActivity {

    String myid;

    private static final String SERVER_IP = "1.225.83.125";
    private static final int SERVER_PORT = 9001;
    Socket socket;
    Handler mHandler;

    PrintWriter networkWriter;
    BufferedReader networkReader;

    String inputLine;

    int me=0;
    int[] user = {0, 0, 0, 0};

    FrameLayout screen;
    //프로필 이미지
    ImageView myProfile;
    ImageView user1Profile;
    ImageView user2Profile;
    ImageView user3Profile;
    ImageView user4Profile;
    //유저 닉네임
    TextView myNick;
    TextView user1Nick;
    TextView user2Nick;
    TextView user3Nick ;
    TextView user4Nick ;
    //유저 머니
    TextView myGold;
    TextView user1Gold ;
    TextView user2Gold ;
    TextView user3Gold ;
    TextView user4Gold ;
    //유저 첫번째 카드
    ImageView myFirstCard;
    ImageView user1FirstCard ;
    ImageView user2FirstCard ;
    ImageView user3FirstCard ;
    ImageView user4FirstCard;
    //유저 두번째 카드
    ImageView mySecondCard;
    ImageView user1SecondCard ;
    ImageView user2SecondCard ;
    ImageView user3SecondCard ;
    ImageView user4SecondCard ;
    //테이블 머니
    TextView tableMoney ;
    //나가기 버튼
    Button exit ;
    //배팅 버튼
    ImageButton die;
    ImageButton check;
    ImageButton call;
    ImageButton half;
    //나눠줄 카드
    ImageView throwCard;
    //유저정보 객체
    UserInfo user1Info;
    UserInfo user2Info;
    UserInfo user3Info;
    UserInfo user4Info;
    UserInfo myInfo;
    //배경음악
    MediaPlayer bgm;
    //유저 메세지
    TextView myMsg;
    TextView user1Msg;
    TextView user2Msg;
    TextView user3Msg;
    TextView user4Msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_table);

        //연결할 서버
        socket = new Socket();
        mHandler = new Handler();


        hideNavigationBar();
        bgm = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();
        SharedPreferences shareData = getSharedPreferences("shareData", MODE_PRIVATE);
        myid = shareData.getString("userId", null);
        //DB에서 내정보를 가져옵니다 (jsp통신)
        try { //user info register 통신 예외처리
            String userInfos;
            UserInfoRegister userInfoRegister = new UserInfoRegister();
            userInfos = userInfoRegister.execute(myid).get();
            String[] userInfoArray = userInfos.split(";");
            myInfo = new UserInfo(userInfoArray[0], userInfoArray[1], Long.parseLong(userInfoArray[2]));
        }catch (Exception e){
        }

        screen = (FrameLayout) findViewById(R.id.screen0);
        //프로필 이미지
         myProfile = (ImageView) findViewById(R.id.imgViewMyProfile);
         user1Profile = (ImageView) findViewById(R.id.imgViewUser1Profile);
         user2Profile = (ImageView) findViewById(R.id.imgViewUser2Profile);
         user3Profile = (ImageView) findViewById(R.id.imgViewUser3Profile);
         user4Profile = (ImageView) findViewById(R.id.imgViewUser4Profile);
        //유저 닉네임
         myNick = (TextView) findViewById(R.id.textViewMyNick);
         user1Nick = (TextView) findViewById(R.id.textViewUser1Nick);
         user2Nick = (TextView) findViewById(R.id.textViewUser2Nick);
         user3Nick = (TextView) findViewById(R.id.textViewUser3Nick);
         user4Nick = (TextView) findViewById(R.id.textViewUser4Nick);
        //유저 머니
         myGold = (TextView) findViewById(R.id.textViewMyGold);
         user1Gold = (TextView) findViewById(R.id.textViewUser1Gold);
         user2Gold = (TextView) findViewById(R.id.textViewUser2Gold);
         user3Gold = (TextView) findViewById(R.id.textViewUser3Gold);
         user4Gold = (TextView) findViewById(R.id.textViewUser4Gold);
        //유저 첫번째 카드
         myFirstCard = (ImageView) findViewById(R.id.imgViewMyFirstCard);
         user1FirstCard = (ImageView) findViewById(R.id.imgViewUser1FirstCard);
         user2FirstCard = (ImageView) findViewById(R.id.imgViewUser2FirstCard);
         user3FirstCard = (ImageView) findViewById(R.id.imgViewUser3FirstCard);
         user4FirstCard = (ImageView) findViewById(R.id.imgViewUser4FirstCard);
        //유저 두번째 카드
         mySecondCard = (ImageView) findViewById(R.id.imgViewMySecondCard);
         user1SecondCard = (ImageView) findViewById(R.id.imgViewUser1SecondCard);
         user2SecondCard = (ImageView) findViewById(R.id.imgViewUser2SecondCard);
         user3SecondCard = (ImageView) findViewById(R.id.imgViewUser3SecondCard);
         user4SecondCard = (ImageView) findViewById(R.id.imgViewUser4SecondCard);
        //테이블 머니
         tableMoney = (TextView) findViewById(R.id.textViewTableMoney);
        //나가기 버튼
         exit = (Button) findViewById(R.id.btnExit);
        //배팅 버튼
         die = (ImageButton) findViewById(R.id.btnDie);
         check = (ImageButton) findViewById(R.id.btnCheck);
         call = (ImageButton) findViewById(R.id.btnCall);
         half = (ImageButton) findViewById(R.id.btnHalf);
        //나눠줄 카드
         throwCard = (ImageView) findViewById(R.id.ImgViewThrowCard);
        //유저 메세지
         myMsg = (TextView) findViewById(R.id.myMsg);
         user1Msg = (TextView) findViewById(R.id.user1Msg);
         user2Msg = (TextView) findViewById(R.id.user2Msg);
         user3Msg = (TextView) findViewById(R.id.user3Msg);
         user4Msg = (TextView) findViewById(R.id.user4Msg);

         clearRoom();//입장시 방초기화

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String line = msg.getData().getString("data");
                Log.d("handler가 전달받은메세지", line);
                String[] array = line.split(";");

                if(array[0].equals("ok")){
                    me = Integer.parseInt(array[1]);

                    sendJoin.start();

                }
                if(array[0].equals("join")) {
                    int intData = Integer.parseInt(array[1]); //유저 순번
                    if( (intData != user[0]) && (intData != user[1]) && (intData != user[2]) && (intData != user[3]))
                        switch (me) {
                            case 1:{
                                if(intData == 2){
                                    user[2] = intData;
                                    setUser3Info(array[2]);
                                    setUser3();
                                }
                                else if(intData == 3){
                                    user[0] = intData;
                                    setUser1Info(array[2]);
                                    setUser1();
                                }
                                else if(intData == 4){
                                    user[1] = intData;
                                    setUser2Info(array[2]);
                                    setUser2();
                                }
                                else if(intData == 5){
                                    user[3] = intData;
                                    setUser4Info(array[2]);
                                    setUser4();
                                }
                                break;
                            }
                            case 2:{
                                if(intData == 1){
                                    user[2] = intData;
                                    setUser4Info(array[2]);
                                    setUser4();
                                }
                                else if(intData == 3){
                                    user[0] = intData;
                                    setUser3Info(array[2]);
                                    setUser3();
                                }
                                else if(intData == 4){
                                    user[1] = intData;
                                    setUser1Info(array[2]);
                                    setUser1();
                                }
                                else if(intData == 5){
                                    user[3] = intData;
                                    setUser2Info(array[2]);
                                    setUser2();
                                }
                                break;
                            }
                            case 3:{
                                if(intData == 2){
                                    user[2] = intData;
                                    setUser4Info(array[2]);
                                    setUser4();
                                }
                                else if(intData == 1){
                                    user[0] = intData;
                                    setUser2Info(array[2]);
                                    setUser2();
                                }
                                else if(intData == 4){
                                    user[1] = intData;
                                    setUser3Info(array[2]);
                                    setUser3();
                                }
                                else if(intData == 5){
                                    user[3] = intData;
                                    setUser1Info(array[2]);
                                    setUser1();
                                }
                                break;
                            }
                            case 4:{
                                if(intData == 2){
                                    user[2] = intData;
                                    setUser2Info(array[2]);
                                    setUser2();
                                }
                                else if(intData == 3){
                                    user[0] = intData;
                                    setUser4Info(array[2]);
                                    setUser4();
                                }
                                else if(intData == 1){
                                    user[1] = intData;
                                    setUser1Info(array[2]);
                                    setUser1();
                                }
                                else if(intData == 5){
                                    user[3] = intData;
                                    setUser3Info(array[2]);
                                    setUser3();
                                }
                                break;
                            }
                            case 5:{
                                if(intData == 2){
                                    user[2] = intData;
                                    setUser1Info(array[2]);
                                    setUser1();
                                }
                                else if(intData == 3){
                                    user[0] = intData;
                                    setUser2Info(array[2]);
                                    setUser2();
                                }
                                else if(intData == 4){
                                    user[1] = intData;
                                    setUser4Info(array[2]);
                                    setUser4();
                                }
                                else if(intData == 1){
                                    user[3] = intData;
                                    setUser3Info(array[2]);
                                    setUser3();
                                }
                                break;
                            }
                        }
                }
                if(array[0].equals("exit")) {
                    int intData = Integer.parseInt(array[1]); //유저 순번
                    switch (me) {
                        case 1: {
                            if(intData == 2) user3Exit();
                            else if(intData == 3) user1Exit();
                            else if(intData == 4) user2Exit();
                            else if(intData == 5) user4Exit();
                            break;
                        }
                        case 2: {
                            if(intData == 1) user4Exit();
                            else if(intData == 3) user3Exit();
                            else if(intData == 4) user1Exit();
                            else if(intData == 5) user2Exit();
                            break;
                        }
                        case 3: {
                            if(intData == 1) user2Exit();
                            else if(intData == 2) user4Exit();
                            else if(intData == 4) user3Exit();
                            else if(intData == 5) user1Exit();
                            break;
                        }
                        case 4: {
                            if(intData == 1) user1Exit();
                            else if(intData == 2) user2Exit();
                            else if(intData == 3) user4Exit();
                            else if(intData == 5) user3Exit();
                            break;
                        }
                        case 5: {
                            if(intData == 1) user3Exit();
                            else if(intData == 2) user1Exit();
                            else if(intData == 3) user2Exit();
                            else if(intData == 5) user4Exit();
                            break;
                        }
                    }

                }
                if(array[0].equals("ready")) {
                    if(Integer.parseInt(array[1]) == me){
                        callOn();
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myMsg("콜");
                                sendData("call" + ";" + me + ";" + 1);
                            }
                        });
                    }
                }
                if(array[0].equals("throw")){
                    int array1 = Integer.parseInt(array[1]);
                    int array2 = Integer.parseInt(array[2]);

                    switch (me) {
                        case 1:{
                            if(array1 == me) iGetFirstCard(array2);
                            else if(array1 == 2) user3GetFirstCard();
                            else if(array1 == 3) user1GetFirstCard();
                            else if(array1 == 4) user2GetFirstCard();
                            else if(array1 == 5) user4GetFirstCard();
                            break;
                        }
                        case 2:{
                            if(array1 == me) iGetFirstCard(array2);
                            else if(array1 == 3) user3GetFirstCard();
                            else if(array1 == 4) user1GetFirstCard();
                            else if(array1 == 5) user2GetFirstCard();
                            else if(array1 == 1) user4GetFirstCard();
                            break;
                        }
                        case 3:{
                            if(array1 == me) iGetFirstCard(array2);
                            else if(array1 == 4) user3GetFirstCard();
                            else if(array1 == 5) user1GetFirstCard();
                            else if(array1 == 1) user2GetFirstCard();
                            else if(array1 == 2) user4GetFirstCard();
                            break;
                        }
                        case 4:{
                            if(array1 == me) iGetFirstCard(array2);
                            else if(array1 == 5) user3GetFirstCard();
                            else if(array1 == 1) user1GetFirstCard();
                            else if(array1 == 2) user2GetFirstCard();
                            else if(array1 == 3) user4GetFirstCard();
                            break;
                        }
                        case 5:{
                            if(array1 == me) iGetFirstCard(array2);
                            else if(array1 == 1) user3GetFirstCard();
                            else if(array1 == 2) user1GetFirstCard();
                            else if(array1 == 3) user2GetFirstCard();
                            else if(array1 == 4) user4GetFirstCard();
                            break;
                        }
                    }
                }
                if(array[0].equals("play")){

                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myMsg("콜");
                            sendData("call" + ";" + me + ";" + 2);
                        }
                    });
                }
                if(array[0].equals("throw2")){
                    int array1 = Integer.parseInt(array[1]);
                    int array2 = Integer.parseInt(array[2]);

                    switch (me) {
                        case 1:{
                            if(array1 == me) iGetSecondCard(array2);
                            else if(array1 == 2) user3GetSecondCard();
                            else if(array1 == 3) user1GetSecondCard();
                            else if(array1 == 4) user2GetSecondCard();
                            else if(array1 == 5) user4GetSecondCard();
                            break;
                        }
                        case 2:{
                            if(array1 == me) iGetSecondCard(array2);
                            else if(array1 == 3) user3GetSecondCard();
                            else if(array1 == 4) user1GetSecondCard();
                            else if(array1 == 5) user2GetSecondCard();
                            else if(array1 == 1) user4GetSecondCard();
                            break;
                        }
                        case 3:{
                            if(array1 == me) iGetSecondCard(array2);
                            else if(array1 == 4) user3GetSecondCard();
                            else if(array1 == 5) user1GetSecondCard();
                            else if(array1 == 1) user2GetSecondCard();
                            else if(array1 == 2) user4GetSecondCard();
                            break;
                        }
                        case 4:{
                            if(array1 == me) iGetSecondCard(array2);
                            else if(array1 == 5) user3GetSecondCard();
                            else if(array1 == 1) user1GetSecondCard();
                            else if(array1 == 2) user2GetSecondCard();
                            else if(array1 == 3) user4GetSecondCard();
                            break;
                        }
                        case 5:{
                            if(array1 == me) iGetSecondCard(array2);
                            else if(array1 == 1) user3GetSecondCard();
                            else if(array1 == 2) user1GetSecondCard();
                            else if(array1 == 3) user2GetSecondCard();
                            else if(array1 == 4) user4GetSecondCard();
                            break;
                        }
                    }
                }
                if(array[0].equals("play2")){
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myMsg("콜");
                            sendData("call" + ";" + me + ";" + 3);
                        }
                    });
                }
                if(array[0].equals("flip")){
                    int array1 = Integer.parseInt(array[1]);
                    int array2 = Integer.parseInt(array[2]);
                    int array3 = Integer.parseInt(array[3]);

                    if(array1 != me) {
                        switch (me) {
                            case 1:{
                                 if(array1 == 2) user3FlipCard(array2,array3);
                                else if(array1 == 3) user1FlipCard(array2,array3);
                                else if(array1 == 4) user2FlipCard(array2,array3);
                                else if(array1 == 5) user4FlipCard(array2,array3);
                                break;
                            }
                            case 2:{
                                 if(array1 == 3) user3FlipCard(array2,array3);
                                else if(array1 == 4) user1FlipCard(array2,array3);
                                else if(array1 == 5) user2FlipCard(array2,array3);
                                else if(array1 == 1) user4FlipCard(array2,array3);
                                break;
                            }
                            case 3:{
                                 if(array1 == 4) user3FlipCard(array2,array3);
                                else if(array1 == 5) user1FlipCard(array2,array3);
                                else if(array1 == 1) user2FlipCard(array2,array3);
                                else if(array1 == 2) user4FlipCard(array2,array3);
                                break;
                            }
                            case 4:{
                                 if(array1 == 5) user3FlipCard(array2,array3);
                                else if(array1 == 1) user1FlipCard(array2,array3);
                                else if(array1 == 2) user2FlipCard(array2,array3);
                                else if(array1 == 3) user4FlipCard(array2,array3);
                                break;
                            }
                            case 5:{
                                 if(array1 == 1) user3FlipCard(array2,array3);
                                else if(array1 == 2) user1FlipCard(array2,array3);
                                else if(array1 == 3) user2FlipCard(array2,array3);
                                else if(array1 == 4) user4FlipCard(array2,array3);
                                break;
                            }
                        }
                    }
                }

                if(array[0].equals("call")){
                    int array1 = Integer.parseInt(array[1]);
                    switch (me) {
                        case 1:{
                            if(array1 == 2) user3Msg("콜");
                            else if(array1 == 3) user1Msg("콜");
                            else if(array1 == 4) user2Msg("콜");
                            else if(array1 == 5) user4Msg("콜");
                            break;
                        }
                        case 2:{
                            if(array1 == 3) user3Msg("콜");
                            else if(array1 == 4) user1Msg("콜");
                            else if(array1 == 5) user2Msg("콜");
                            else if(array1 == 1) user4Msg("콜");
                            break;
                        }
                        case 3:{
                            if(array1 == 4) user3Msg("콜");
                            else if(array1 == 5) user1Msg("콜");
                            else if(array1 == 1) user2Msg("콜");
                            else if(array1 == 2) user4Msg("콜");
                            break;
                        }
                        case 4:{
                            if(array1 == 5) user3Msg("콜");
                            else if(array1 == 1) user1Msg("콜");
                            else if(array1 == 2) user2Msg("콜");
                            else if(array1 == 3) user4Msg("콜");
                            break;
                        }
                        case 5:{
                            if(array1 == 1) user3Msg("콜");
                            else if(array1 == 2) user1Msg("콜");
                            else if(array1 == 3) user2Msg("콜");
                            else if(array1 == 4) user4Msg("콜");
                            break;
                        }
                    }
                }

                if(array[0].equals("play3")){
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            myMsg("콜");
                            sendData("call" + ";" + me + ";" + 4);
                        }
                    });
                }

                if(array[0].equals("clear")){
                    clearCard();
                }

                if(array[0].equals("result")) {
                    int array1 = Integer.parseInt(array[1]);

                    if( array1 == me){
                        myMsgOn(array[2]);
                    }

                    switch (me) {
                        case 1:{
                            if(array1 == 2) user3MsgOn(array[2]);
                            else if(array1 == 3) user1MsgOn(array[2]);
                            else if(array1 == 4) user2MsgOn(array[2]);
                            else if(array1 == 5) user4MsgOn(array[2]);
                            break;
                        }
                        case 2:{
                            if(array1 == 3) user3MsgOn(array[2]);
                            else if(array1 == 4) user1MsgOn(array[2]);
                            else if(array1 == 5) user2MsgOn(array[2]);
                            else if(array1 == 1) user4MsgOn(array[2]);
                            break;
                        }
                        case 3:{
                            if(array1 == 4) user3MsgOn(array[2]);
                            else if(array1 == 5) user1MsgOn(array[2]);
                            else if(array1 == 1) user2MsgOn(array[2]);
                            else if(array1 == 2) user4MsgOn(array[2]);
                            break;
                        }
                        case 4:{
                            if(array1 == 5) user3MsgOn(array[2]);
                            else if(array1 == 1) user1MsgOn(array[2]);
                            else if(array1 == 2) user2MsgOn(array[2]);
                            else if(array1 == 3) user4MsgOn(array[2]);
                            break;
                        }
                        case 5:{
                            if(array1 == 1) user3MsgOn(array[2]);
                            else if(array1 == 2) user1MsgOn(array[2]);
                            else if(array1 == 3) user2MsgOn(array[2]);
                            else if(array1 == 4) user4MsgOn(array[2]);
                            break;
                        }
                    }
                }
            }
        };


        run.start();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user1Exit();
                user2Exit();
                user3Exit();
                user4Exit();

                Intent intent = new Intent(GameTable.this, mainPage.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }































    Thread run = new Thread() {
        @Override
        public void run() {
            super.run();

            try{
                socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    while(true) {
                        String line = br.readLine();
                        Log.d("받음",line);
                        Bundle data = new Bundle();
                        data.putString("data",line);
                        Message msg = new Message();
                        msg.setData(data);

                        mHandler.sendMessage(msg);
                    }

            }catch (Exception e){}
        }
    };


    void clearRoom() { //입장시 이미지들을 clear 하고 내정보를 보이게하는 메소드입니다.
        user1Profile.setVisibility(View.INVISIBLE);
        user2Profile.setVisibility(View.INVISIBLE);
        user3Profile.setVisibility(View.INVISIBLE);
        user4Profile.setVisibility(View.INVISIBLE);
        throwCard.setVisibility(View.INVISIBLE);
        myFirstCard.setVisibility(View.INVISIBLE);
        user1FirstCard.setVisibility(View.INVISIBLE);
        user2FirstCard.setVisibility(View.INVISIBLE);
        user3FirstCard.setVisibility(View.INVISIBLE);
        user4FirstCard.setVisibility(View.INVISIBLE);
        mySecondCard.setVisibility(View.INVISIBLE);
        user1SecondCard.setVisibility(View.INVISIBLE);
        user2SecondCard.setVisibility(View.INVISIBLE);
        user3SecondCard.setVisibility(View.INVISIBLE);
        user4SecondCard.setVisibility(View.INVISIBLE);
        myNick.setText(myInfo.getNick());
        user1Nick.setVisibility(View.INVISIBLE);
        user2Nick.setVisibility(View.INVISIBLE);
        user3Nick.setVisibility(View.INVISIBLE);
        user4Nick.setVisibility(View.INVISIBLE);
        DecimalFormat format = new DecimalFormat("###,###");
        myGold.setText(format.format(myInfo.getGold()) + " 원");
        dieOff();
        checkOff();
        callOff();
        halfOff();
        user1Msg.setVisibility(View.INVISIBLE);
        user2Msg.setVisibility(View.INVISIBLE);
        user3Msg.setVisibility(View.INVISIBLE);
        user4Msg.setVisibility(View.INVISIBLE);
        myMsg.setVisibility(View.INVISIBLE);
    }

    void setUser1Info(String user1Id){//유저db에서 유저 정보를 가져온후 userinfo 객체에 저장합니다.
        try { //user info register 통신 예외처리
            String userInfos;
            UserInfoRegister userInfoRegister = new UserInfoRegister();
            userInfos = userInfoRegister.execute(user1Id).get();
            String[] userInfoArray = userInfos.split(";");
            user1Info = new UserInfo(userInfoArray[0], userInfoArray[1], Long.parseLong(userInfoArray[2]));
        }catch (Exception e){
        }
    }

    void setUser2Info(String user2Id){//유저db에서 유저 정보를 가져온후 userinfo 객체에 저장합니다.
        try { //user info register 통신 예외처리
            String userInfos;
            UserInfoRegister userInfoRegister = new UserInfoRegister();
            userInfos = userInfoRegister.execute(user2Id).get();
            String[] userInfoArray = userInfos.split(";");
            user2Info = new UserInfo(userInfoArray[0], userInfoArray[1], Long.parseLong(userInfoArray[2]));
        }catch (Exception e){
        }
    }

    void setUser3Info(String user3Id){//유저db에서 유저 정보를 가져온후 userinfo 객체에 저장합니다.
        try { //user info register 통신 예외처리
            String userInfos;
            UserInfoRegister userInfoRegister = new UserInfoRegister();
            userInfos = userInfoRegister.execute(user3Id).get();
            String[] userInfoArray = userInfos.split(";");
            user3Info = new UserInfo(userInfoArray[0], userInfoArray[1], Long.parseLong(userInfoArray[2]));
        }catch (Exception e){
        }
    }

    void setUser4Info(String user4Id){//유저db에서 유저 정보를 가져온후 userinfo 객체에 저장합니다.
        try { //user info register 통신 예외처리
            String userInfos;
            UserInfoRegister userInfoRegister = new UserInfoRegister();
            userInfos = userInfoRegister.execute(user4Id).get();
            String[] userInfoArray = userInfos.split(";");
            user4Info = new UserInfo(userInfoArray[0], userInfoArray[1], Long.parseLong(userInfoArray[2]));
        }catch (Exception e){
        }
    }

    void setUser1(){ //유저정보를 게임 테이블에 보여줍니다.
        user1Profile.setVisibility(View.VISIBLE);
        user1Nick.setText(user1Info.getNick());
        user1Nick.setVisibility(View.VISIBLE);
        DecimalFormat format = new DecimalFormat("###,###");
        user1Gold.setText(format.format(user1Info.getGold()) + " 원");
    }

    void setUser2(){ //유저정보를 게임 테이블에 보여줍니다.
        user2Profile.setVisibility(View.VISIBLE);
        user2Nick.setText(user2Info.getNick());
        user2Nick.setVisibility(View.VISIBLE);
        DecimalFormat format = new DecimalFormat("###,###");
        user2Gold.setText(format.format(user2Info.getGold()) + " 원");
    }

    void setUser3(){ //유저정보를 게임 테이블에 보여줍니다.
        user3Profile.setVisibility(View.VISIBLE);
        user3Nick.setText(user3Info.getNick());
        user3Nick.setVisibility(View.VISIBLE);
        DecimalFormat format = new DecimalFormat("###,###");
        user3Gold.setText(format.format(user3Info.getGold()) + " 원");
    }

    void setUser4(){ //유저정보를 게임 테이블에 보여줍니다.
        user4Profile.setVisibility(View.VISIBLE);
        user4Nick.setText(user4Info.getNick());
        user4Nick.setVisibility(View.VISIBLE);
        DecimalFormat format = new DecimalFormat("###,###");
        user4Gold.setText(format.format(user4Info.getGold()) + " 원");
    }

    void exitUser1(){//유저가 나갔을때 정보를 비웁니다.
        user1Profile.setVisibility(View.INVISIBLE);
        user1Nick.setVisibility(View.INVISIBLE);
        user1Gold.setText("");
        user1FirstCard.setVisibility(View.INVISIBLE);
        user1SecondCard.setVisibility(View.INVISIBLE);
    }

    void exitUser2(){//유저가 나갔을때 정보를 비웁니다.
        user2Profile.setVisibility(View.INVISIBLE);
        user2Nick.setVisibility(View.INVISIBLE);
        user2Gold.setText("");
        user2FirstCard.setVisibility(View.INVISIBLE);
        user2SecondCard.setVisibility(View.INVISIBLE);
    }

    void exitUser3(){//유저가 나갔을때 정보를 비웁니다.
        user3Profile.setVisibility(View.INVISIBLE);
        user3Nick.setVisibility(View.INVISIBLE);
        user3Gold.setText("");
        user3FirstCard.setVisibility(View.INVISIBLE);
        user3SecondCard.setVisibility(View.INVISIBLE);
    }

    void exitUser4(){//유저가 나갔을때 정보를 비웁니다.
        user4Profile.setVisibility(View.INVISIBLE);
        user4Nick.setVisibility(View.INVISIBLE);
        user4Gold.setText("");
        user4FirstCard.setVisibility(View.INVISIBLE);
        user4SecondCard.setVisibility(View.INVISIBLE);
    }

    void callOn(){ //콜버튼 동작on
        call.setEnabled(true);
        call.setImageResource(R.drawable.callon);
        call.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    void callOff(){ //콜버튼 동작off
        call.setEnabled(false);
        call.setImageResource(R.drawable.calloff);
        call.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    void dieOn(){ //다이버튼 동작on
        die.setEnabled(true);
        die.setImageResource(R.drawable.dieon);
        die.setScaleType(ImageView.ScaleType.FIT_XY);

    }
    void dieOff(){ //다이버튼 동작off
        die.setEnabled(false);
        die.setImageResource(R.drawable.dieoff);
        die.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    void checkOn(){ //체크버튼 동작on
        check.setEnabled(true);
        check.setImageResource(R.drawable.checkon);
        check.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    void checkOff(){ //체크버튼 동작off
        check.setEnabled(false);
        check.setImageResource(R.drawable.checkoff);
        check.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    void halfOn(){ //하프버튼 동작on
        half.setEnabled(true);
        half.setImageResource(R.drawable.halfon);
        half.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    void halfOff(){ //다이버튼 동작off
        half.setEnabled(false);
        half.setImageResource(R.drawable.halfoff);
        half.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    void iGetFirstCard(int card){//내가 첫번째 카드를 받습니다(애니메이션추가)
        final int card1 = card;
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , myFirstCard.getX()-(screen.getWidth()/2)+(myFirstCard.getWidth()/2)
                , throwCard.getY()
                , myFirstCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                switch (card1) {
                    case 1:    myFirstCard.setImageResource(R.drawable.card01);
                        break;
                    case 2:    myFirstCard.setImageResource(R.drawable.card02);
                        break;
                    case 3:    myFirstCard.setImageResource(R.drawable.card03);
                        break;
                    case 4:    myFirstCard.setImageResource(R.drawable.card04);
                        break;
                    case 5:    myFirstCard.setImageResource(R.drawable.card05);
                        break;
                    case 6:    myFirstCard.setImageResource(R.drawable.card06);
                        break;
                    case 7:    myFirstCard.setImageResource(R.drawable.card07);
                        break;
                    case 8:    myFirstCard.setImageResource(R.drawable.card08);
                        break;
                    case 9:    myFirstCard.setImageResource(R.drawable.card09);
                        break;
                    case 10:    myFirstCard.setImageResource(R.drawable.card10);
                        break;
                    case 11:    myFirstCard.setImageResource(R.drawable.card11);
                        break;
                    case 12:    myFirstCard.setImageResource(R.drawable.card12);
                        break;
                    case 13:    myFirstCard.setImageResource(R.drawable.card13);
                        break;
                    case 14:    myFirstCard.setImageResource(R.drawable.card14);
                        break;
                    case 15:    myFirstCard.setImageResource(R.drawable.card15);
                        break;
                    case 16:    myFirstCard.setImageResource(R.drawable.card16);
                        break;
                    case 17:    myFirstCard.setImageResource(R.drawable.card17);
                        break;
                    case 18:    myFirstCard.setImageResource(R.drawable.card18);
                        break;
                    case 19:    myFirstCard.setImageResource(R.drawable.card19);
                        break;
                    case 20:    myFirstCard.setImageResource(R.drawable.card20);
                        break;
                }
                myFirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myFirstCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }
    void iGetSecondCard(int card){//내가 두번째 카드를 받습니다(애니메이션추가)
        final int card1 = card;
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , mySecondCard.getX()-(screen.getWidth()/2)+(mySecondCard.getWidth()/2)
                , throwCard.getY()
                , mySecondCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                switch (card1) {
                    case 1:    mySecondCard.setImageResource(R.drawable.card01);
                        break;
                    case 2:    mySecondCard.setImageResource(R.drawable.card02);
                        break;
                    case 3:    mySecondCard.setImageResource(R.drawable.card03);
                        break;
                    case 4:    mySecondCard.setImageResource(R.drawable.card04);
                        break;
                    case 5:    mySecondCard.setImageResource(R.drawable.card05);
                        break;
                    case 6:    mySecondCard.setImageResource(R.drawable.card06);
                        break;
                    case 7:    mySecondCard.setImageResource(R.drawable.card07);
                        break;
                    case 8:    mySecondCard.setImageResource(R.drawable.card08);
                        break;
                    case 9:    mySecondCard.setImageResource(R.drawable.card09);
                        break;
                    case 10:    mySecondCard.setImageResource(R.drawable.card10);
                        break;
                    case 11:    mySecondCard.setImageResource(R.drawable.card11);
                        break;
                    case 12:    mySecondCard.setImageResource(R.drawable.card12);
                        break;
                    case 13:    mySecondCard.setImageResource(R.drawable.card13);
                        break;
                    case 14:    mySecondCard.setImageResource(R.drawable.card14);
                        break;
                    case 15:    mySecondCard.setImageResource(R.drawable.card15);
                        break;
                    case 16:    mySecondCard.setImageResource(R.drawable.card16);
                        break;
                    case 17:    mySecondCard.setImageResource(R.drawable.card17);
                        break;
                    case 18:    mySecondCard.setImageResource(R.drawable.card18);
                        break;
                    case 19:    mySecondCard.setImageResource(R.drawable.card19);
                        break;
                    case 20:    mySecondCard.setImageResource(R.drawable.card20);
                        break;
                }
                mySecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mySecondCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }

    void user1GetFirstCard(){//유저가 첫번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user1FirstCard.getX()-(screen.getWidth()/2)+(user1FirstCard.getWidth()/2)
                , throwCard.getY()
                , user1FirstCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user1FirstCard.setImageResource(R.drawable.cardback);
                user1FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user1FirstCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }
    void user1GetSecondCard(){//유저가 두번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user1SecondCard.getX()-(screen.getWidth()/2)+(user1SecondCard.getWidth()/2)
                , throwCard.getY()
                , user1SecondCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user1SecondCard.setImageResource(R.drawable.cardback);
                user1SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user1SecondCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }

    void user2GetFirstCard(){//유저가 첫번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user2FirstCard.getX()-(screen.getWidth()/2)+(user2FirstCard.getWidth()/2)
                , throwCard.getY()
                , user2FirstCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user2FirstCard.setImageResource(R.drawable.cardback);
                user2FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user2FirstCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }
    void user2GetSecondCard(){//유저가 두번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user2SecondCard.getX()-(screen.getWidth()/2)+(user2SecondCard.getWidth()/2)
                , throwCard.getY()
                , user2SecondCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user2SecondCard.setImageResource(R.drawable.cardback);
                user2SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user2SecondCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }

    void user3GetFirstCard(){//유저가 첫번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user3FirstCard.getX()-(screen.getWidth()/2)+(user3FirstCard.getWidth()/2)
                , throwCard.getY()
                , user3FirstCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user3FirstCard.setImageResource(R.drawable.cardback);
                user3FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user3FirstCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }
    void user3GetSecondCard(){//유저가 두번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user3SecondCard.getX()-(screen.getWidth()/2)+(user3SecondCard.getWidth()/2)
                , throwCard.getY()
                , user3SecondCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user3SecondCard.setImageResource(R.drawable.cardback);
                user3SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user3SecondCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }

    void user4GetFirstCard(){//유저가 첫번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user4FirstCard.getX()-(screen.getWidth()/2)+(user4FirstCard.getWidth()/2)
                , throwCard.getY()
                , user4FirstCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user4FirstCard.setImageResource(R.drawable.cardback);
                user4FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user4FirstCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }
    void user4GetSecondCard(){//유저가 두번째 카드를 받습니다(애니메이션추가)
        throwCard.setVisibility(View.VISIBLE);

        TranslateAnimation anim = new TranslateAnimation(
                throwCard.getX()-(screen.getWidth()/2)+(throwCard.getWidth()/2)
                , user4SecondCard.getX()-(screen.getWidth()/2)+(user4SecondCard.getWidth()/2)
                , throwCard.getY()
                , user4SecondCard.getY() + throwCard.getHeight()
        );
        anim.setDuration(250);
        throwCard.startAnimation(anim);
        throwCard.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user4SecondCard.setImageResource(R.drawable.cardback);
                user4SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                user4SecondCard.setVisibility(View.VISIBLE);
            }
        }, 250);


    }


    void user1FlipCard(int card1, int card2){//유저의 카드를 뒤집습니다.
        final int card01 = card1;
        final int card02 = card2;
        ObjectAnimator anim = ObjectAnimator.ofFloat(user1FirstCard, "rotationY", 0, 360);
        anim.setDuration(150);
        anim.start();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                switch (card01) {
                    case 1:    user1FirstCard.setImageResource(R.drawable.card01);
                        break;
                    case 2:    user1FirstCard.setImageResource(R.drawable.card02);
                        break;
                    case 3:    user1FirstCard.setImageResource(R.drawable.card03);
                        break;
                    case 4:    user1FirstCard.setImageResource(R.drawable.card04);
                        break;
                    case 5:    user1FirstCard.setImageResource(R.drawable.card05);
                        break;
                    case 6:    user1FirstCard.setImageResource(R.drawable.card06);
                        break;
                    case 7:    user1FirstCard.setImageResource(R.drawable.card07);
                        break;
                    case 8:    user1FirstCard.setImageResource(R.drawable.card08);
                        break;
                    case 9:    user1FirstCard.setImageResource(R.drawable.card09);
                        break;
                    case 10:    user1FirstCard.setImageResource(R.drawable.card10);
                        break;
                    case 11:    user1FirstCard.setImageResource(R.drawable.card11);
                        break;
                    case 12:    user1FirstCard.setImageResource(R.drawable.card12);
                        break;
                    case 13:    user1FirstCard.setImageResource(R.drawable.card13);
                        break;
                    case 14:    user1FirstCard.setImageResource(R.drawable.card14);
                        break;
                    case 15:    user1FirstCard.setImageResource(R.drawable.card15);
                        break;
                    case 16:    user1FirstCard.setImageResource(R.drawable.card16);
                        break;
                    case 17:    user1FirstCard.setImageResource(R.drawable.card17);
                        break;
                    case 18:    user1FirstCard.setImageResource(R.drawable.card18);
                        break;
                    case 19:    user1FirstCard.setImageResource(R.drawable.card19);
                        break;
                    case 20:    user1FirstCard.setImageResource(R.drawable.card20);
                        break;
                }
                user1FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);//여기에 딜레이 후 시작할 작업들을 입력
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(user1SecondCard, "rotationY", 0, 360);
                        anim2.setDuration(150);
                        anim2.start();
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                switch (card02) {
                                    case 1:    user1SecondCard.setImageResource(R.drawable.card01);
                                        break;
                                    case 2:    user1SecondCard.setImageResource(R.drawable.card02);
                                        break;
                                    case 3:    user1SecondCard.setImageResource(R.drawable.card03);
                                        break;
                                    case 4:    user1SecondCard.setImageResource(R.drawable.card04);
                                        break;
                                    case 5:    user1SecondCard.setImageResource(R.drawable.card05);
                                        break;
                                    case 6:    user1SecondCard.setImageResource(R.drawable.card06);
                                        break;
                                    case 7:    user1SecondCard.setImageResource(R.drawable.card07);
                                        break;
                                    case 8:    user1SecondCard.setImageResource(R.drawable.card08);
                                        break;
                                    case 9:    user1SecondCard.setImageResource(R.drawable.card09);
                                        break;
                                    case 10:    user1SecondCard.setImageResource(R.drawable.card10);
                                        break;
                                    case 11:    user1SecondCard.setImageResource(R.drawable.card11);
                                        break;
                                    case 12:    user1SecondCard.setImageResource(R.drawable.card12);
                                        break;
                                    case 13:    user1SecondCard.setImageResource(R.drawable.card13);
                                        break;
                                    case 14:    user1SecondCard.setImageResource(R.drawable.card14);
                                        break;
                                    case 15:    user1SecondCard.setImageResource(R.drawable.card15);
                                        break;
                                    case 16:    user1SecondCard.setImageResource(R.drawable.card16);
                                        break;
                                    case 17:    user1SecondCard.setImageResource(R.drawable.card17);
                                        break;
                                    case 18:    user1SecondCard.setImageResource(R.drawable.card18);
                                        break;
                                    case 19:    user1SecondCard.setImageResource(R.drawable.card19);
                                        break;
                                    case 20:    user1SecondCard.setImageResource(R.drawable.card20);
                                        break;
                                }
                                user1SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        }, 150);// 0.5초 정도 딜레이를 준 후 시작//여기에 딜레이 후 시작할 작업들을 입력
                    }
                }, 150);
            }
        }, 150);// 0.5초 정도 딜레이를 준 후 시작


    }
    void user2FlipCard(int card1, int card2){//유저의 카드를 뒤집습니다.
        final int card01 = card1;
        final int card02 = card2;
        ObjectAnimator anim = ObjectAnimator.ofFloat(user2FirstCard, "rotationY", 0, 360);
        anim.setDuration(150);
        anim.start();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                switch (card01) {
                    case 1:    user2FirstCard.setImageResource(R.drawable.card01);
                        break;
                    case 2:    user2FirstCard.setImageResource(R.drawable.card02);
                        break;
                    case 3:    user2FirstCard.setImageResource(R.drawable.card03);
                        break;
                    case 4:    user2FirstCard.setImageResource(R.drawable.card04);
                        break;
                    case 5:    user2FirstCard.setImageResource(R.drawable.card05);
                        break;
                    case 6:    user2FirstCard.setImageResource(R.drawable.card06);
                        break;
                    case 7:    user2FirstCard.setImageResource(R.drawable.card07);
                        break;
                    case 8:    user2FirstCard.setImageResource(R.drawable.card08);
                        break;
                    case 9:    user2FirstCard.setImageResource(R.drawable.card09);
                        break;
                    case 10:    user2FirstCard.setImageResource(R.drawable.card10);
                        break;
                    case 11:    user2FirstCard.setImageResource(R.drawable.card11);
                        break;
                    case 12:    user2FirstCard.setImageResource(R.drawable.card12);
                        break;
                    case 13:    user2FirstCard.setImageResource(R.drawable.card13);
                        break;
                    case 14:    user2FirstCard.setImageResource(R.drawable.card14);
                        break;
                    case 15:    user2FirstCard.setImageResource(R.drawable.card15);
                        break;
                    case 16:    user2FirstCard.setImageResource(R.drawable.card16);
                        break;
                    case 17:    user2FirstCard.setImageResource(R.drawable.card17);
                        break;
                    case 18:    user2FirstCard.setImageResource(R.drawable.card18);
                        break;
                    case 19:    user2FirstCard.setImageResource(R.drawable.card19);
                        break;
                    case 20:    user2FirstCard.setImageResource(R.drawable.card20);
                        break;
                }
                user2FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(user2SecondCard, "rotationY", 0, 360);
                        anim2.setDuration(150);
                        anim2.start();
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                switch (card02) {
                                    case 1:    user2SecondCard.setImageResource(R.drawable.card01);
                                        break;
                                    case 2:    user2SecondCard.setImageResource(R.drawable.card02);
                                        break;
                                    case 3:    user2SecondCard.setImageResource(R.drawable.card03);
                                        break;
                                    case 4:    user2SecondCard.setImageResource(R.drawable.card04);
                                        break;
                                    case 5:    user2SecondCard.setImageResource(R.drawable.card05);
                                        break;
                                    case 6:    user2SecondCard.setImageResource(R.drawable.card06);
                                        break;
                                    case 7:    user2SecondCard.setImageResource(R.drawable.card07);
                                        break;
                                    case 8:    user2SecondCard.setImageResource(R.drawable.card08);
                                        break;
                                    case 9:    user2SecondCard.setImageResource(R.drawable.card09);
                                        break;
                                    case 10:    user2SecondCard.setImageResource(R.drawable.card10);
                                        break;
                                    case 11:    user2SecondCard.setImageResource(R.drawable.card11);
                                        break;
                                    case 12:    user2SecondCard.setImageResource(R.drawable.card12);
                                        break;
                                    case 13:    user2SecondCard.setImageResource(R.drawable.card13);
                                        break;
                                    case 14:    user2SecondCard.setImageResource(R.drawable.card14);
                                        break;
                                    case 15:    user2SecondCard.setImageResource(R.drawable.card15);
                                        break;
                                    case 16:    user2SecondCard.setImageResource(R.drawable.card16);
                                        break;
                                    case 17:    user2SecondCard.setImageResource(R.drawable.card17);
                                        break;
                                    case 18:    user2SecondCard.setImageResource(R.drawable.card18);
                                        break;
                                    case 19:    user2SecondCard.setImageResource(R.drawable.card19);
                                        break;
                                    case 20:    user2SecondCard.setImageResource(R.drawable.card20);
                                        break;
                                }
                                user2SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        }, 150);// 0.5초 정도 딜레이를 준 후 시작//여기에 딜레이 후 시작할 작업들을 입력
                    }
                }, 150);
            }
        }, 150);// 0.5초 정도 딜레이를 준 후 시작


    }
    void user3FlipCard(int card1, int card2){//유저의 카드를 뒤집습니다.
        final int card01 = card1;
        final int card02 = card2;
        ObjectAnimator anim = ObjectAnimator.ofFloat(user3FirstCard, "rotationY", 0, 360);
        anim.setDuration(150);
        anim.start();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                switch (card01) {
                    case 1:    user3FirstCard.setImageResource(R.drawable.card01);
                        break;
                    case 2:    user3FirstCard.setImageResource(R.drawable.card02);
                        break;
                    case 3:    user3FirstCard.setImageResource(R.drawable.card03);
                        break;
                    case 4:    user3FirstCard.setImageResource(R.drawable.card04);
                        break;
                    case 5:    user3FirstCard.setImageResource(R.drawable.card05);
                        break;
                    case 6:    user3FirstCard.setImageResource(R.drawable.card06);
                        break;
                    case 7:    user3FirstCard.setImageResource(R.drawable.card07);
                        break;
                    case 8:    user3FirstCard.setImageResource(R.drawable.card08);
                        break;
                    case 9:    user3FirstCard.setImageResource(R.drawable.card09);
                        break;
                    case 10:    user3FirstCard.setImageResource(R.drawable.card10);
                        break;
                    case 11:    user3FirstCard.setImageResource(R.drawable.card11);
                        break;
                    case 12:    user3FirstCard.setImageResource(R.drawable.card12);
                        break;
                    case 13:    user3FirstCard.setImageResource(R.drawable.card13);
                        break;
                    case 14:    user3FirstCard.setImageResource(R.drawable.card14);
                        break;
                    case 15:    user3FirstCard.setImageResource(R.drawable.card15);
                        break;
                    case 16:    user3FirstCard.setImageResource(R.drawable.card16);
                        break;
                    case 17:    user3FirstCard.setImageResource(R.drawable.card17);
                        break;
                    case 18:    user3FirstCard.setImageResource(R.drawable.card18);
                        break;
                    case 19:    user3FirstCard.setImageResource(R.drawable.card19);
                        break;
                    case 20:    user3FirstCard.setImageResource(R.drawable.card20);
                        break;
                }
                user3FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(user3SecondCard, "rotationY", 0, 360);
                        anim2.setDuration(150);
                        anim2.start();
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                switch (card02) {
                                    case 1:    user3SecondCard.setImageResource(R.drawable.card01);
                                        break;
                                    case 2:    user3SecondCard.setImageResource(R.drawable.card02);
                                        break;
                                    case 3:    user3SecondCard.setImageResource(R.drawable.card03);
                                        break;
                                    case 4:    user3SecondCard.setImageResource(R.drawable.card04);
                                        break;
                                    case 5:    user3SecondCard.setImageResource(R.drawable.card05);
                                        break;
                                    case 6:    user3SecondCard.setImageResource(R.drawable.card06);
                                        break;
                                    case 7:    user3SecondCard.setImageResource(R.drawable.card07);
                                        break;
                                    case 8:    user3SecondCard.setImageResource(R.drawable.card08);
                                        break;
                                    case 9:    user3SecondCard.setImageResource(R.drawable.card09);
                                        break;
                                    case 10:    user3SecondCard.setImageResource(R.drawable.card10);
                                        break;
                                    case 11:    user3SecondCard.setImageResource(R.drawable.card11);
                                        break;
                                    case 12:    user3SecondCard.setImageResource(R.drawable.card12);
                                        break;
                                    case 13:    user3SecondCard.setImageResource(R.drawable.card13);
                                        break;
                                    case 14:    user3SecondCard.setImageResource(R.drawable.card14);
                                        break;
                                    case 15:    user3SecondCard.setImageResource(R.drawable.card15);
                                        break;
                                    case 16:    user3SecondCard.setImageResource(R.drawable.card16);
                                        break;
                                    case 17:    user3SecondCard.setImageResource(R.drawable.card17);
                                        break;
                                    case 18:    user3SecondCard.setImageResource(R.drawable.card18);
                                        break;
                                    case 19:    user3SecondCard.setImageResource(R.drawable.card19);
                                        break;
                                    case 20:    user3SecondCard.setImageResource(R.drawable.card20);
                                        break;
                                }
                                user3SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        }, 150);// 0.5초 정도 딜레이를 준 후 시작//여기에 딜레이 후 시작할 작업들을 입력
                    }
                }, 150);
            }
        }, 150);// 0.5초 정도 딜레이를 준 후 시작


    }
    void user4FlipCard(int card1, int card2){//유저의 카드를 뒤집습니다.
        final int card01 = card1;
        final int card02 = card2;
        ObjectAnimator anim = ObjectAnimator.ofFloat(user4FirstCard, "rotationY", 0, 360);
        anim.setDuration(150);
        anim.start();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                switch (card01) {
                    case 1:    user4FirstCard.setImageResource(R.drawable.card01);
                        break;
                    case 2:    user4FirstCard.setImageResource(R.drawable.card02);
                        break;
                    case 3:    user4FirstCard.setImageResource(R.drawable.card03);
                        break;
                    case 4:    user4FirstCard.setImageResource(R.drawable.card04);
                        break;
                    case 5:    user4FirstCard.setImageResource(R.drawable.card05);
                        break;
                    case 6:    user4FirstCard.setImageResource(R.drawable.card06);
                        break;
                    case 7:    user4FirstCard.setImageResource(R.drawable.card07);
                        break;
                    case 8:    user4FirstCard.setImageResource(R.drawable.card08);
                        break;
                    case 9:    user4FirstCard.setImageResource(R.drawable.card09);
                        break;
                    case 10:    user4FirstCard.setImageResource(R.drawable.card10);
                        break;
                    case 11:    user4FirstCard.setImageResource(R.drawable.card11);
                        break;
                    case 12:    user4FirstCard.setImageResource(R.drawable.card12);
                        break;
                    case 13:    user4FirstCard.setImageResource(R.drawable.card13);
                        break;
                    case 14:    user4FirstCard.setImageResource(R.drawable.card14);
                        break;
                    case 15:    user4FirstCard.setImageResource(R.drawable.card15);
                        break;
                    case 16:    user4FirstCard.setImageResource(R.drawable.card16);
                        break;
                    case 17:    user4FirstCard.setImageResource(R.drawable.card17);
                        break;
                    case 18:    user4FirstCard.setImageResource(R.drawable.card18);
                        break;
                    case 19:    user4FirstCard.setImageResource(R.drawable.card19);
                        break;
                    case 20:    user4FirstCard.setImageResource(R.drawable.card20);
                        break;
                }
                user4FirstCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(user4SecondCard, "rotationY", 0, 360);
                        anim2.setDuration(150);
                        anim2.start();
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                switch (card02) {
                                    case 1:    user4SecondCard.setImageResource(R.drawable.card01);
                                        break;
                                    case 2:    user4SecondCard.setImageResource(R.drawable.card02);
                                        break;
                                    case 3:    user4SecondCard.setImageResource(R.drawable.card03);
                                        break;
                                    case 4:    user4SecondCard.setImageResource(R.drawable.card04);
                                        break;
                                    case 5:    user4SecondCard.setImageResource(R.drawable.card05);
                                        break;
                                    case 6:    user4SecondCard.setImageResource(R.drawable.card06);
                                        break;
                                    case 7:    user4SecondCard.setImageResource(R.drawable.card07);
                                        break;
                                    case 8:    user4SecondCard.setImageResource(R.drawable.card08);
                                        break;
                                    case 9:    user4SecondCard.setImageResource(R.drawable.card09);
                                        break;
                                    case 10:    user4SecondCard.setImageResource(R.drawable.card10);
                                        break;
                                    case 11:    user4SecondCard.setImageResource(R.drawable.card11);
                                        break;
                                    case 12:    user4SecondCard.setImageResource(R.drawable.card12);
                                        break;
                                    case 13:    user4SecondCard.setImageResource(R.drawable.card13);
                                        break;
                                    case 14:    user4SecondCard.setImageResource(R.drawable.card14);
                                        break;
                                    case 15:    user4SecondCard.setImageResource(R.drawable.card15);
                                        break;
                                    case 16:    user4SecondCard.setImageResource(R.drawable.card16);
                                        break;
                                    case 17:    user4SecondCard.setImageResource(R.drawable.card17);
                                        break;
                                    case 18:    user4SecondCard.setImageResource(R.drawable.card18);
                                        break;
                                    case 19:    user4SecondCard.setImageResource(R.drawable.card19);
                                        break;
                                    case 20:    user4SecondCard.setImageResource(R.drawable.card20);
                                        break;
                                }
                                user4SecondCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        }, 150);// 0.5초 정도 딜레이를 준 후 시작//여기에 딜레이 후 시작할 작업들을 입력
                    }
                }, 150);
            }
        }, 150);// 0.5초 정도 딜레이를 준 후 시작


    }

    void myMsgOn(String msg) {
        myMsg.setText(msg);
        myMsg.setVisibility(View.VISIBLE);

    }
    void user1MsgOn(String msg) {
        user1Msg.setText(msg);
        user1Msg.setVisibility(View.VISIBLE);

    }
    void user2MsgOn(String msg) {
        user2Msg.setText(msg);
        user2Msg.setVisibility(View.VISIBLE);

    }
    void user3MsgOn(String msg) {
        user3Msg.setText(msg);
        user3Msg.setVisibility(View.VISIBLE);

    }
    void user4MsgOn(String msg) {
        user4Msg.setText(msg);
        user4Msg.setVisibility(View.VISIBLE);

    }

    void myMsgOff() {
        myMsg.setVisibility(View.INVISIBLE);

    }
    void user1MsgOff() {
        user1Msg.setVisibility(View.INVISIBLE);

    }
    void user2MsgOff() {
        user2Msg.setVisibility(View.INVISIBLE);

    }
    void user3MsgOff() {
        user3Msg.setVisibility(View.INVISIBLE);

    }
    void user4MsgOff() {
        user4Msg.setVisibility(View.INVISIBLE);

    }

    void myMsg(String msg) {
        myMsg.setText(msg);
        myMsg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                myMsg.setVisibility(View.INVISIBLE);//여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1000);// 0.5초 정도 딜레이를 준 후 시작
    }
    void user1Msg(String msg) {
        user1Msg.setText(msg);
        user1Msg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                user1Msg.setVisibility(View.INVISIBLE);//여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1000);// 0.5초 정도 딜레이를 준 후 시작
    }
    void user2Msg(String msg) {
        user2Msg.setText(msg);
        user2Msg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                user2Msg.setVisibility(View.INVISIBLE);//여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1000);// 0.5초 정도 딜레이를 준 후 시작
    }
    void user3Msg(String msg) {
        user3Msg.setText(msg);
        user3Msg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                user3Msg.setVisibility(View.INVISIBLE);//여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1000);// 0.5초 정도 딜레이를 준 후 시작
    }
    void user4Msg(String msg) {
        user4Msg.setText(msg);
        user4Msg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                user4Msg.setVisibility(View.INVISIBLE);//여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1000);// 0.5초 정도 딜레이를 준 후 시작
    }

    void user1Exit() {
        user1Profile.setVisibility(View.INVISIBLE);
        user1FirstCard.setVisibility(View.INVISIBLE);
        user1SecondCard.setVisibility(View.INVISIBLE);
        user1Nick.setVisibility(View.INVISIBLE);
        user1Gold.setVisibility(View.INVISIBLE);
        user[0]=0;

    }
    void user2Exit() {
        user2Profile.setVisibility(View.INVISIBLE);
        user2FirstCard.setVisibility(View.INVISIBLE);
        user2SecondCard.setVisibility(View.INVISIBLE);
        user2Nick.setVisibility(View.INVISIBLE);
        user2Gold.setVisibility(View.INVISIBLE);
        user[1]=0;

    }
    void user3Exit() {
        user3Profile.setVisibility(View.INVISIBLE);
        user3FirstCard.setVisibility(View.INVISIBLE);
        user3SecondCard.setVisibility(View.INVISIBLE);
        user3Nick.setVisibility(View.INVISIBLE);
        user3Gold.setVisibility(View.INVISIBLE);
        user[2]=0;

    }
    void user4Exit() {
        user4Profile.setVisibility(View.INVISIBLE);
        user4FirstCard.setVisibility(View.INVISIBLE);
        user4SecondCard.setVisibility(View.INVISIBLE);
        user4Nick.setVisibility(View.INVISIBLE);
        user4Gold.setVisibility(View.INVISIBLE);
        user[3]=0;

    }

    void clearCard() {//테이블의 모든카드를 보이지않게합니다.
        myFirstCard.setVisibility(View.INVISIBLE);
        user1FirstCard.setVisibility(View.INVISIBLE);
        user2FirstCard.setVisibility(View.INVISIBLE);
        user3FirstCard.setVisibility(View.INVISIBLE);
        user4FirstCard.setVisibility(View.INVISIBLE);

        mySecondCard.setVisibility(View.INVISIBLE);
        user1SecondCard.setVisibility(View.INVISIBLE);
        user2SecondCard.setVisibility(View.INVISIBLE);
        user3SecondCard.setVisibility(View.INVISIBLE);
        user4SecondCard.setVisibility(View.INVISIBLE);

        myMsgOff();
        user1MsgOff();
        user2MsgOff();
        user3MsgOff();
        user4MsgOff();
    }

    void exit() {

        sendExit.start();

    }


    Thread sendExit = new Thread() {
        @Override
        public void run() {
            super.run();
            try {
                networkWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                networkWriter.println("exit" + ";" + me + ";" + myid);
                networkWriter.flush();
                socket.close();
            }catch (IOException e){e.printStackTrace();}

        }
    };
    Thread closeSocket = new Thread() {
        @Override
        public void run() {
            super.run();
            try{
                socket.close();
            }catch (Exception e){}
        }
    };
    Thread sendJoin = new Thread() {
        @Override
        public void run() {
            super.run();
            try {
                networkWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                networkWriter.println("join" + ";" + me + ";" + myid);
                networkWriter.flush();
            }catch (IOException e){e.printStackTrace();}

        }
    };

    void sendData(final String data) {

        Thread send = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    networkWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                    networkWriter.println(data);
                    networkWriter.flush();
                }catch (IOException e){e.printStackTrace();}
            }
        };

        send.start();
    }






    /////////////////////////////////////activity 환경설정 메소드입니다.///////////////////////////////
    @Override //BGM 끊김 설정 //소켓 끊김 설정
    protected void onStop() {
        super.onStop();
        sendExit.start();
        bgm.stop();

    }
    @Override //BGM 끊김 설정
    protected void onRestart() {
        super.onRestart();

        hideNavigationBar();
        bgm = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();
    }


    @Override //액티비티 종료 애니에이션을 삭제합니다.
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }
    private void hideNavigationBar() {//하단바를 삭제합니다
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
