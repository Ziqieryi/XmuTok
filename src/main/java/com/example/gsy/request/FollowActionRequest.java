package com.example.gsy.request;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FollowActionRequest {
    String token;
    int to_user_id;
    int action_type;

    public FollowActionRequest(String token, int to_user_id, int action_type) {
        this.token = token;
        this.to_user_id = to_user_id;
        this.action_type = action_type;
    }

    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder()
                .add("token",token)
                .add("to_user_id",""+to_user_id)
                .add("action_type",""+action_type)
                .build();
        String url="http://192.168.100.6:8080/douyin/relation/action/";
        if(token!=null){
            url+="?token="+token;
            if(to_user_id!=0){
                url+="&to_user_id="+to_user_id;
            }
            if(action_type!=0){
                url+="&action_type="+action_type;
            }
        }
        else
        {
            if(to_user_id!=0){
                url+="?to_user_id="+to_user_id;
                if(action_type!=0){
                    url+="&action_type="+action_type;
                }
            }else
            {
                if(action_type!=0){
                    url+="?action_type="+action_type;
                }
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
