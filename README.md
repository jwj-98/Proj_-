# 안드로이드 섯다 게임 앱
![main](https://user-images.githubusercontent.com/65906245/115261992-13c43680-a16f-11eb-9688-6df67ba30da2.PNG)


## 목차
* 프로젝트 소개  
    1. 프로젝트 기능
    2. 프로젝트 개발 환경
    3. 프로젝트 동작
    4. 실행 영상
* Front-End(Android App)
    
    1.  레이아웃, 클래스 구성
<hr/>
1. 프로젝트 소개<br/>
&nbsp;&nbsp;안드로이드 공부 후 처음으로 만들어본 프로젝트입니다.<br/>
안드로이드 앱의 기본적인 기능 위주로
웹서버, 게임 서버를 연동해 구현해본 온라인 섯다 게임 앱입니다.

1-1. 프로젝트 기능<br/>
&nbsp;&nbsp;동시에 5명까지 참가가 가능한 실시간 섯다게임.

* 기본 기능
    * 유저 로그인 가능
	    * 로그인, 회원가입 - 로컬DB에 정보 저장, 조회
	    * 계정찾기(email)  - 회원정보 DB조회 후 email로 정보전송
    * 게임 기능
	    * 입장/퇴장, 직전승리에 따른 패 순서, 방향 부여
	    * 화투 패 배분 (ObjectAnimator)
	    * 배팅 후 승패 판정

1-2. 프로젝트 개발환경<br/>
&nbsp;&nbsp; OS: WINDOWS10
&nbsp;&nbsp; IDE: ECLIPSE 4, Android Studio 4
&nbsp;&nbsp; DB: Oracle 11g

1-3. 프로젝트 동작<br/>
&nbsp;&nbsp; 유저가 Front-end(앱)을 통해 사용자 인증, 조회 요청시 localDB를 조회해 처리합니다. 이때 POST 메소드를 사용해 통신합니다. Front-end 에서 게임시작 요청시 Back-end(소켓서버) 에서는 새로운 스레드를 생성함과 동시에 현재 진행중인 게임 유저 수 의 따른 순서번호를 부여합니다. 게임이 진행되면 Back-end(소켓서버) 에서 화투패를 섞고 순서대로 배분합니다. 배분과 배팅이 모두 끝나면 승리한 유저에게 게임머니(소켓서버-웹-localDB 통신)를 지급하고 첫순서를 부여합니다.<br/>

1-4. 실행 영상<br/>
<hr/>

## Front-End(Android App)

1. 레이아웃, 클래스 구성
>java src 
>>CreateNick
>>CreateNickRegister
>>FindAccount
>>FindAccountRegister
>>GameTable
>>LoginActivity
>>LoginRegister
>>mainPage
>>SignupActivity
>>SignupRegister
>>socketTest
>>SocketTestRegister
>>UserInfo
>>UserInfoRegister

>xml layout
>>activity_create_nick
>>activity_find_account
>>activity_game_table
>>activity_login
>>activity_main_page
>>activity_signup

#### CreateNick.java<br/>
&nbsp;&nbsp; 유저 첫 로그인 시 보여지는 액티비티에서 받은 닉네임을 AsyncTack를 상속받은 CreateNickRegister 객체로 Back-end(웹)로 전송합니다.<br/>

#### CreateNickRegister.java

&nbsp;&nbsp; 메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.
