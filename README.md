# 안드로이드 섯다 게임 앱
![main](https://user-images.githubusercontent.com/65906245/115261992-13c43680-a16f-11eb-9688-6df67ba30da2.PNG)


## 목차
* 들어가며
  1. 프로젝트 소개  
    1-1. 프로젝트 기능
<hr/>
1. 프로젝트 소개<br/>
&nbsp;&nbsp;첫 안드로이드 프로젝트<br/>
안드로이드 앱의 기본적인 기능 위주로
웹서버, 게임 서버를 연동해 구현해본 섯다 게임 앱입니다.<hr/>

1_1. 프로젝트 기능<br/>
&nbsp;&nbsp;동시에 5명까지 참가가 가능한 실시간 섯다게임.

* 기본 기능
    * 유저 로그인 가능
	    * 로그인, 회원가입 - 로컬DB에 정보 저장, 조회
	    * 계정찾기(email)  - 회원정보 DB조회 후 email로 정보전송
    * 게임 기능
	    * 입장/퇴장, 직전승리에 따른 패 순서, 방향 부여
	    * 화투 패 배분 (ObjectAnimator)
	    * 배팅 후 승패 판정

### 앱 개발환경 android studio
<img src = "https://miro.medium.com/max/700/1*98MZF039wW7qw_TdtDzvmg.png" width="300">

#회원가입, 로그인, 계정찾기 뷰 구현

#게임 테이블 뷰 -  에니메이션 기능으로 화투 패 배분 등 동작 구현<hr/>

### 웹서버 개발환경 Java eclipse(jsp+tomcat), 데이터베이스 local Oracle DB
<img src = "https://algol.dev/wp-content/uploads/2020/10/logo-eclipse.png" width="170"><img src = "https://mblogthumb-phinf.pstatic.net/MjAxODAzMDRfNDIg/MDAxNTIwMTQ4ODYzNTI1.pafkG0llpCTnavxBCXoBl4stv5nDS3P-Xcj5CbZF9c8g.Eai6_HfOtmc45TPcoi4rZr0vQk0pu_LRvjigYShqu50g.PNG.feel940/image_1154452801520148641525.png?type=w800" width="200"><img src = "https://www.baaer.eu/wp-content/uploads/2018/07/Slide1.jpg" width="330">


#회원가입, 로그인, 계정찾기 응답 수행 웹, 로컬DB와 연동

#게임, 인증 정보 응답<hr/>
### 소켓서버 개발환경 Java eclipse
<img src = "https://algol.dev/wp-content/uploads/2020/10/logo-eclipse.png" width="170">

#실시간 게임에 필요한 데이터 소켓통신

#유저 순서, 입장·퇴장 실시간 처리</hr>
