package com.example.gsy.request;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetFansListRequest {
    String token;
    int user_id;

    public GetFansListRequest(String token, int user_id) {
        this.token = token;
        this.user_id = user_id;
    }
    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://192.168.100.6:8080/douyin/relation/follower/list/?user_id="+user_id+"&token="+token)
                .get()
                .build();
        Call call=client.newCall(request);
        return call;
    }
}
