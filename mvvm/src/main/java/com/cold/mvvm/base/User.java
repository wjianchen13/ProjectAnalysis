package com.cold.mvvm.base;

import android.view.View;
import android.widget.Toast;

/**
 * name: User
 * desc: databinding 实体
 * author:
 * date: 2017-06-22 10:41
 * remark:
 */
public class User {
    private String name;//用户名
    private String nickName;//昵称
    private String email;//邮箱

    private boolean vip;//是否是会员
    private int level;//级别

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public User() {
    }

    public User(String name, String nickName, String email) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void clickName(View view){
        Toast.makeText(view.getContext(),"点击了用户名",Toast.LENGTH_SHORT).show();
    }

    public boolean longClickNickName(View view){
        Toast.makeText(view.getContext(),"长按了昵称！",Toast.LENGTH_SHORT).show();
        return true;
    }
}