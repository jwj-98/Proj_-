

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
    2.  Code comment
<hr/>

## 프로젝트 소개<br/>
&nbsp;&nbsp;안드로이드 공부 후 처음으로 만들어본 프로젝트입니다.<br/>
안드로이드 앱의 기본적인 기능 위주로
웹서버, 게임 서버를 연동해 구현해본 온라인 섯다 게임 앱입니다.<br/>

1. 프로젝트 기능<br/><br/>
&nbsp;&nbsp;동시에 5명까지 참가가 가능한 실시간 섯다게임.

	* 기본 기능
	    * 유저 로그인 가능
		    * 로그인, 회원가입 - Back-end(웹)로컬DB에 정보 저장, 조회
		    * 계정찾기(email)  - 회원정보 DB조회 후 email로 정보전송
	    * 게임 기능
		    * 입장/퇴장, 직전승리에 따른 패 순서, 방향 부여
		    * 화투 패 배분 (ObjectAnimator & TranslateAnimation)
		    * 배팅 후 승패 판정

2. 프로젝트 개발환경<br/><br/>
&nbsp;&nbsp; OS: WINDOWS10<br/>
&nbsp;&nbsp; IDE: ECLIPSE 4, Android Studio 4<br/>
&nbsp;&nbsp; DB: Oracle 11g<br/>

3. 프로젝트 동작<br/><br/>
&nbsp;&nbsp; 유저가 Front-end(앱)을 통해 사용자 인증, 조회 요청시 localDB를 조회해 처리합니다. 이때 POST 메소드를 사용해 통신합니다. Front-end 에서 게임시작 요청시 Back-end(소켓서버) 에서는 새로운 스레드를 생성함과 동시에 현재 진행중인 게임 유저 수 의 따른 고유한 순서번호를 부여합니다. 게임이 진행되면 Back-end(소켓서버) 에서 화투패를 섞고 순서대로 배분합니다. 배분과 배팅이 모두 끝나면 승리한 유저에게 게임머니(소켓서버-웹-localDB 통신)를 지급하고 첫순서를 부여합니다.
<br/>&nbsp;&nbsp; Json구조에 대해 알지 못할때 만든 프로젝트이기 때문에 거의 모든통신을 수동으로,각 데이터를 세미콜론 단위로 구분하는  get방식과 유사한 방식을 사용해 통신합니다.(socket & http-post method)<br/>

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
>>UserInfo<br/>
>>UserInfoRegister<br/>

>xml layout
>>activity_create_nick<br/>
>>activity_find_account<br/>
>>activity_game_table<br/>
>>activity_login<br/>
>>activity_main_page<br/>
>>activity_signup<br/>
>
<br/>
2. Code comment
<br/>

#### CreateNick.java<br/>
&nbsp;&nbsp; 유저 첫 로그인 시 보여지는 액티비티에서 받은 닉네임을 AsyncTack를 상속받은 CreateNickRegister 객체로 Back-end(웹)로 전송합니다.<br/>

#### CreateNickRegister.java<br/>

&nbsp;&nbsp; 메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.<br/>

#### FindAccount.java<br/>

&nbsp;&nbsp; 계정찾기 액티비티에서 입력한 유저 정보를 AsyncTask를 상속받은 FindAccountRegister 객체로 Back-end(웹)로 전송합니다.<br/>

#### FindAccountRegister.java<br/>

&nbsp;&nbsp;  메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.<br/>

#### GameTable.java<br/>

&nbsp;&nbsp; 새로운 스레드에서 Socket객체로 Back-end(게임서버)와 통신합니다. 내 정보와 플레이하는 다른 유저의 정보를 가져오기 위해 Back-end(웹)와 HTTP통신합니다.  <br/>
게임 시작 시 Front-end(액티비티)에 보이는 위젯들을 invisible해 게임 테이블을 clear합니다.<br/><br/>

 게임서버에서 받은 첫 데이터에 따라 각 동작을 나머지 데이터로 수행합니다. <br/>
 ok: 입장완료처리, 플레이하고 있는 유저수 데이터를 유저순번으로 부여합니다. 플레이하고 있는 유저의 정보를 요청합니다. <br/>
 join: 플레이하고 있는 유저의 정보나 새로 참가한 유저의 정보를 받고 순번에 따라 시계방향으로 게임이 진행되도록 자리에 맞춰 유저 정보를 액티비티에 보여줍니다. 이때 DB에서 유저정보를 가져오기 위해 Back-end(웹)과 통신합니다.<br/>
 exit: 다른 유저가 접속 종료할 경우 해당 유저의 정보를 액티비티에서 invisible 합니다. <br/>
 ready: 받은 선번호가 유저 고유 순서번호와 일치하면 배팅버튼을 누를 경우 데이터를 보내고 게임이 시작됩니다. <br/>
 throw: 유저들에게 첫번째 패를 배분합니다. 유저 고유순번에 따라 시계방향으로 다른유저도 뒷면으로 패를 배분받는 애니메이션을 액티비티에 보여줍니다.<br/>
 play: 첫번째 배팅 여부를 선택합니다. 배팅버튼을 누를 경우 데이터를 보내고 다음차례로 넘어갑니다.<br/>
throw2: 유저들에게 두번째 패를 배분합니다.<br/>
play2: 두번째 배팅 여부를 선택하면 데이터를 보냅니다.<br/>
flip: 받은 유저 고유순번과 패번호 데이터로 다른유저의 패를 뒤집는 애니메이션을 보여줍니다.<br/>
call: 다른유저가 배팅할 경우 "call" 메세지를 액티비티의 유저 메세지칸에 띄워줍니다.<br/>
play3: 다음게임을 시작하기 위해 배팅 버튼을 누를 경우 데이터를 보냅니다.<br/>
clear: 모든 유저의 패 칸을 invisible합니다.<br/>
result: 게임 결과를 보여줍니다. 끗, 땡 등 패결과를 액티비티의 유저 메세지칸에 띄워줍니다.<br/><br/>

액티비티에서 나가기 버튼을 누를경우 퇴장 데이터를 Back-end(게임서버)로 보내고 메인 액티비티로 이동시킵니다.<br/>

패를뒤집거나 이동시키는 애니메이션은 TranslateAnimation 과 ObjectAnimator객체를 사용합니다.<br/>

서비스로 배경음악을 설정할 경우 onRestart 단계에서 중복으로 배경음악이 나오는 경우가 있어 각 액티비티마다 배경음악을 따로 설정합니다.<br/>

#### LoginActivity.java<br/>

&nbsp;&nbsp; 최초실행 로그인 성공 이후 자동로그인을 지원하기위해 Shared Preferences를 사용합니다.<br/> 로그인 버튼이벤트: ID&PW값의 조건(길이, 영문 등) 검사 후  AsyncTask를 상속받은 LoginRegister객체로 Back-end(웹)로컬DB에서 값 조회 요청, 로그인 성공여부를 통신합니다. 로그인 성공 시 mainPage액티비티 인텐트 생성 후 PK로 사용하는 ID값을 추가해 해당 액티비티로 이동합니다.<br/> 회원가입&계정찾기 버튼 이벤트: 회원가입, 계정찾기 액티비티Intent 생성 후 해당 액티비티로 이동합니다.

#### LoginRegister.java<br/>

&nbsp;&nbsp;  메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.<br/>

#### mainPage.java<br/>

&nbsp;&nbsp; 유저 정보 조회에 pk로 사용되는 id값을 SharedPreferences에서 가져와 Back-end(웹) 에 유저정보 조회를 요청합니다. 받은 유저 정보를 액티비티에 보여줍니다. <br/> 로그아웃버튼 이벤트: 앱 실행 시 자동로그인 해제를 위해 SharedPreferences를 null로 초기화합니다. 로그인 액티비티Intent 생성 후 해당 액티비티로 이동합니다.

#### SignupActivity.java<br/>

&nbsp;&nbsp; Front-end(액티비티)에서 입력한 값의 유효성 검사 후 SignupRegister객체로 Back-end(웹)로 값을 전송합니다. Back-end에서 유저 중복검사 후 회원 가입성공 데이터를 받을 경우 완료 메세지 Toast출력 후 현재 액티비티를 종료시켜 LoginActivity로 전환하게 합니다. <br/>

#### SignupRegister.java<br/>

&nbsp;&nbsp;  메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다.<br/>

#### UserInfo.java<br/>

&nbsp;&nbsp; Back-end(웹-로컬DB)에서 가져온 유저 정보를 저장 할 객체입니다. 유저 id, nick, gold 생정자.

#### UserInfoRegister.java<br/>

&nbsp;&nbsp;  메인 스레드에서의 HTTP통신을 권장하지않으므로 AsyncTask를 상속받은 클래스로 Back-end(웹)와 통신합니다. pk로 사용되는 user ID 로 유저정보를 요청해 UserInfo객체로 리턴합니다.<br/>


#### activity_create_nick.xml<br/>



#### activity_find_account.xml<br/>



#### activity_game_table.xml<br/>



#### activity_login.xml<br/>



#### activity_main_page.xml<br/>



#### activity_signup.xml<br/>



