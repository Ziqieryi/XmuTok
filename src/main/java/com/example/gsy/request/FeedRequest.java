package com.example.gsy.request;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FeedRequest {
    //这个request需要带的参数
    String token;
    String latest_time;

    public FeedRequest(String token, String latest_time) {
        this.token = token;
        this.latest_time = latest_time;
    }
    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        String url="http://192.168.100.6:8080/douyin/feed/";
        if(token!=null){
            url+="?token="+token;
            if(latest_time!=null){
                url+="&latest_time="+latest_time;
            }
        }
        else{
            if(latest_time!=null){
                url+="?latest_time="+latest_time;
            }
        }
        Request request=new Request.Builder()
                            .url(url)
                            .get()
                            .build();
        Call call=client.newCall(request);
        return call;
    }
}
