package com.example.gsy.fragment.upload;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.gsy.MainActivity;
import com.example.gsy.R;
import com.example.gsy.entity.SimpleResponse;
import com.example.gsy.request.PublishActionRequest;
import com.example.gsy.tools.systemService.UriToFilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpLoadDialog extends DialogFragment {
    ImageView uploadCover;
    EditText uploadTitle;
    Button uploadSubmit;
    File file;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen); //dialog全屏
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去掉dialog的标题，需要在setContentView()之前
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        final View view = inflater.inflate(R.layout.dialog_upload, null); //自己的布局文件
        uploadTitle=view.findViewById(R.id.uploadTitle);
        uploadCover=view.findViewById(R.id.uploadCover);
        uploadSubmit=view.findViewById(R.id.uploadSubmit);
        //一开始禁用button提交
        //给提交和调用相册设置监听
        uploadCover.setOnClickListener(new UploadVideoFile());
        uploadSubmit.setEnabled(false);
        uploadSubmit.setOnClickListener(new UploadSubmit());
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }
    private class UploadSubmit implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //根据已有的Title和视频File以及Token发投稿请求,并且异步处理response
            SharedPreferences write= getActivity().getSharedPreferences("info",Context.MODE_PRIVATE);
            PublishActionRequest publishActionRequest=new PublishActionRequest(file,write.getString("token",null),uploadTitle.getText().toString());
            Call call=publishActionRequest.getRequest();
            call.enqueue(new ThisRequestCallBack());
            //userid=1的"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiVGVzdDAwMSIsInBhc3N3b3JkIjoidGVzdDAwMSJ9.qoVK5nyFnanmh9BWWSGRzW7tT0KOaTgwHBK0rNs99G8"
        }
    }
    private class UploadVideoFile implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent,2);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode==2&&resultCode == Activity.RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    file=null;
                    file=new File(UriToFilePath.getFilePathByUri(getContext(),uri));
                    if (!file.exists()) {
                        toastMessage("选取视频失败");
                    }
                    if (file.length() > 100 * 1024 * 1024) {
                        toastMessage("视频大于100M,无法上传");
                    }else{
                        //成功拿到视频,放开提交权限
                        Log.d("TAGG",file.getAbsolutePath());
                        uploadSubmit.setEnabled(true);
                        uploadSubmit.setTextColor(Color.WHITE);
                    }
                } catch (Exception e) {
                    toastMessage("发生了意外的app内部错误");
                }
            }else{
                toastMessage("选取视频失败");
        }
    }
    private class ThisRequestCallBack implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            (getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    simpleResponse=new SimpleResponse(-1,"网络请求失败");
                    toastMessage("网络请求失败");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            (getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //解码
                    try{
                        String responseJson=response.body().string();
                        JSONObject jsonObject=null;
                        jsonObject=new JSONObject(responseJson);
                        int statusCode=jsonObject.getInt("status_code");
                        String statusMessage=jsonObject.getString("status_msg");
                        //解码成功,返回response
//                        simpleResponse=new SimpleResponse(statusCode,statusMessage);
                        if(statusCode!=0){
                            //告诉错误信息
                            toastMessage(statusMessage);
                        }else{
                            //成功
                            toastMessage("上传视频成功");
                            dismiss();
                        }
                    }catch(JSONException e){
                        //解码出错
                        toastMessage("本地解码失败");
                    }catch(Exception e){
                        toastMessage("本地解码失败");
                    }
                }
            });
        }
    }
    public void toastMessage(String message) {
        Toast toast = Toast.makeText(getContext(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }
}
