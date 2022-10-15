package com.example.gsy.request;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UserInfoRequest {
    //这个request需要带的参数
    int user_id;
    String token;
    public UserInfoRequest(){}

    public UserInfoRequest(int user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://192.168.100.6:8080/douyin/user/?user_id="+user_id+"&token="+token)
                .get()
                .build();
        Call call=client.newCall(request);
        return call;
    }
}
