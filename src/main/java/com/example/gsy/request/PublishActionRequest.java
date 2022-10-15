package com.example.gsy.request;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.gsy.entity.SimpleResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActionRequest {
    File data;
    String token;
    String title;
    SimpleResponse simpleResponse;
    public PublishActionRequest(File data, String token, String title) {
        this.data = data;
        this.token = token;
        this.title = title;
    }
    public Call getRequest(){
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody=RequestBody.create(MediaType.parse("mp4/*"),data);
        MultipartBody body=new MultipartBody.Builder().addFormDataPart("token",token)
                                                        .addFormDataPart("title",title)
                                                        .addFormDataPart("data",data.getName(),formBody)
                                                        .setType(MultipartBody.FORM)
                                                        .build();
        Request request=new Request.Builder().post(body).url("http://192.168.100.6:8080/douyin/publish/action/").build();
        Call call=client.newCall(request);
        return call;
    }
}
