package com.example.gsy.request;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.gsy.MainActivity;
import com.example.gsy.entity.SimpleResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginRequest {
    //这个request需要带的参数
    String username;
    String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder()
                                        .add("username",username)
                                        .add("password",password)
                                        .build();
        String url="http://192.168.100.6:8080/douyin/user/login/";
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
