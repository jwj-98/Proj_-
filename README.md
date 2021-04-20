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

## 프로젝트 소개<br/>
&nbsp;&nbsp;안드로이드 공부 후 처음으로 만들어본 프로젝트입니다.<br/>
안드로이드 앱의 기본적인 기능 위주로
웹서버, 게임 서버를 연동해 구현해본 온라인 섯다 게임 앱입니다.<br/>

1. 프로젝트 기능<br/><br/>
&nbsp;&nbsp;동시에 5명까지 참가가 가능한 실시간 섯다게임.

	* 기본 기능
	    * 유저 로그인 가능
		    * 로그인, 회원가입 - 로컬DB에 정보 저장, 조회
		    * 계정찾기(email)  - 회원정보 DB조회 후 email로 정보전송
	    * 게임 기능
		    * 입장/퇴장, 직전승리에 따른 패 순서, 방향 부여
		    * 화투 패 배분 (ObjectAnimator)
		    * 배팅 후 승패 판정

2. 프로젝트 개발환경<br/><br/>
&nbsp;&nbsp; OS: WINDOWS10<br/>
&nbsp;&nbsp; IDE: ECLIPSE 4, Android Studio 4<br/>
&nbsp;&nbsp; DB: Oracle 11g<br/>

3. 프로젝트 동작<br/><br/>
&nbsp;&nbsp; 유저가 Front-end(앱)을 통해 사용자 인증, 조회 요청시 localDB를 조회해 처리합니다. 이때 POST 메소드를 사용해 통신합니다. Front-end 에서 게임시작 요청시 Back-end(소켓서버) 에서는 새로운 스레드를 생성함과 동시에 현재 진행중인 게임 유저 수 의 따른 순서번호를 부여합니다. 게임이 진행되면 Back-end(소켓서버) 에서 화투패를 섞고 순서대로 배분합니다. 배분과 배팅이 모두 끝나면 승리한 유저에게 게임머니(소켓서버-웹-localDB 통신)를 지급하고 첫순서를 부여합니다.
<br/>&nbsp;&nbsp; Json구조에 대해 알지 못할때 만든 프로젝트이기 때문에 거의 모든통신을 수동으로, 세미콜론 단위로 구분하는 get방식으로 데이터 작성후 post메소드로 전송하는 방식을 사용했습니다.<br/>

4. 실행 영상<br/><br/>
<hr/>

## Front-End(Android App)

1. 레이아웃, 클래스 구성
>java src 
>>CreateNick<br/>
>>CreateNickRegister<br/>
>>FindAccount<br/>
>>FindAccountRegister<br/>
>>GameTable<br/>
>>LoginActivity<br/>
>>LoginRegister<br/>
>>mainPage<br/>
>>SignupActivity<br/>
>>SignupRegister<br/>
>>socketTest<br/>
>>SocketTestRegister<br/>
>>UserInfo<br/>
>>UserInfoRegister<br/>

>xml layout
>>activity_create_nick<br/>
>>activity_find_account<br/>
>>activity_game_table<br/>
>>activity_login<br/>
>>activity_main_page<br/>
>>activity_signup<br/>

#### CreateNick.java<br/>
&nbsp;&nbsp; 유저 첫 로그인 시 보여지는 액티비티에서 받은 닉네임을 AsyncTack를 상속받은 CreateNickRegister 객체로 Back-end(웹)로 전송합니다.<br/>

#### CreateNickRegister.java<br/>

&nbsp;&nbsp; 메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.<br/>

#### FindAccount.java<br/>

&nbsp;&nbsp; 계정찾기 액티비티에서 입력한 유저 정보를 AsyncTask를 상속받은 FindAccountRegister 객체로 Back-end(웹)로 전송합니다.<br/>

#### FindAccountRegister.java<br/>

&nbsp;&nbsp;  메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.<br/>

#### GameTable.java<br/>

&nbsp;&nbsp; Socket객체로 Back-end(게임서버)와 통신합니다. 내 정보와 플레이하는 다른 유저의 정보를 가져오기 위해 Back-end(웹)와 HTTP통신합니다.  게임 시작 시 Front-end(액티비티)에 보이는 게임 테이블을 clear합니다.<br/> 게임서버에서 받은 첫 데이터에 따라 각 동작을 나머지 데이터로 수행합니다. <br/>ok: 입장완료처리, 플레이하고 있는 유저수 데이터를 유저순번으로 부여합니다. 플레이하고 있는 유저의 정보를 요청합니다. <br/>join: 플레이하고 있는 유저의 정보나 새로 참가한 유저의 정보를 받고 순번에 따라 시계방향으로 게임이 진행되도록 자리에 맞춰 유저 정보를 액티비티에 보여줍니다. 

#### LoginActivity<br/>
#### LoginRegister<br/>
#### mainPage<br/>
#### SignupActivity<br/>
#### SignupRegister<br/>
#### socketTest<br/>
#### SocketTestRegister<br/>
#### UserInfo<br/>
#### UserInfoRegister<br/>

#### activity_create_nick<br/>
#### activity_find_account<br/>
#### activity_game_table<br/>
#### activity_login<br/>
#### activity_main_page<br/>
#### activity_signup<br/>
