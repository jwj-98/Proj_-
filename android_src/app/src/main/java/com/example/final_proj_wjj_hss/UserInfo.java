package com.example.final_proj_wjj_hss;



public class UserInfo  {

    String id;
    String nick;
    long gold;

    public UserInfo(String id, String nick, long gold) {
        this.id = id;
        this.nick = nick;
        this.gold = gold;
    }


    public String getId() {
        return id;
    }


    public String getNick() {
        return nick;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }
}
