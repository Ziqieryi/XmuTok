package com.example.gsy.request;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegisterRequest {
    //这个request需要带的参数
    String username;
    String password;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        String url="http://192.168.100.6:8080/douyin/user/register/";
        if(username!=null){
            url+="?username="+username;
            if(password!=null){
                url+="&password="+password;
            }
        }
        else
        {
            if(password!=null){
                url+="?password="+password;
            }
        }
        Request request=new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        Call call=client.newCall(request);
        return call;
    }
}
