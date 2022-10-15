package com.example.gsy.request;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FavouriteRequest {
    String token;
    int video_id;
    int action_type;

    public FavouriteRequest(String token, int video_id, int action_type) {
        this.token = token;
        this.video_id = video_id;
        this.action_type = action_type;
    }

    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=new FormBody.Builder()
                .add("token",token)
                .add("video_id",""+video_id)
                .add("action_type",""+action_type)
                .build();
        String url="http://192.168.100.6:8080/douyin/favorite/action/";
        if(token!=null){
            url+="?token="+token;
            if(video_id!=0){
                url+="&video_id="+video_id;
            }
            if(action_type!=0){
                url+="&action_type="+action_type;
            }
        }
        else
        {
            if(video_id!=0){
                url+="?video_id="+video_id;
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
