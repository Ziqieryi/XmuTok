package com.example.gsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsy.adapter.MainAdapter;
import com.example.gsy.entity.AuthorFragmentData;
import com.example.gsy.entity.ItemFragmentData;
import com.example.gsy.entity.IndexFragmentData;
import com.example.gsy.fragment.friend.FriendFragment;
import com.example.gsy.fragment.index.IndexFragment;
import com.example.gsy.fragment.message.MessageFragment;
import com.example.gsy.fragment.personal.LoginDialog;
import com.example.gsy.fragment.personal.LoginFragment;
import com.example.gsy.fragment.personal.PersonalFragment;
import com.example.gsy.fragment.upload.UpLoadDialog;
import com.example.gsy.request.FeedRequest;
import com.example.gsy.request.PublishActionRequest;
import com.example.gsy.request.UserInfoRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.RealInterceptorChain;
import okio.BufferedSink;


public class MainActivity extends AppCompatActivity {
    ViewPager2 mainPager2;
    List<Fragment>fragments;
    TabLayout mainMenu;
    TabLayoutMediator tabLayoutMediator;
    String[] titles={"     首页","朋友","           消息","       我"};
    IndexFragmentData Data;
    MainAdapter mainAdapter;
    ImageView upload;
    TextView refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestMyPermissions();
        fragments = new ArrayList<Fragment>();
        mainPager2 = findViewById(R.id.mainPager2);
        mainMenu = findViewById(R.id.mainMenu);
        upload = findViewById(R.id.uploadEntry);
        refresh=findViewById(R.id.refresh);
        refresh.setOnTouchListener(new OnDoubleClickListener());
        upload.setOnClickListener(new UploadListener());
        Log.d("TAGG","给我发请求！");
        fragments.add(new IndexFragment());
        fragments.add(new FriendFragment());
        fragments.add(new MessageFragment());
        SharedPreferences read=getSharedPreferences("info", Context.MODE_PRIVATE);
        String token=read.getString("token","没有拿到token");
        int user_id=read.getInt("user_id",0);
        if (token=="没有拿到token"||user_id==0)
        {
            //还没有登录,"我"这个tab点击是给用户呈现登录界面
            fragments.add(new LoginFragment());
        }else{
            //已经登录了,"我"这个tab就给用户展示个人信息了,拿
            fragments.add(new PersonalFragment());
            Log.d("WASD","9494949");
        }
        mainAdapter = new MainAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        mainPager2.setAdapter(mainAdapter);
        mainPager2.setUserInputEnabled(false);
        tabLayoutMediator = new TabLayoutMediator(mainMenu, mainPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    TextView textView = new TextView(MainActivity.this);
                    textView.setTextSize(15);
                    textView.setTextColor(Color.WHITE);
                    textView.setText(titles[position]);
                    tab.setCustomView(textView);
                } else {
                    TextView textView = new TextView(MainActivity.this);
                    textView.setTextSize(15);
                    textView.setTextColor(Color.GRAY);
                    textView.setText(titles[position]);
                    tab.setCustomView(textView);
                }
            }
        });
        tabLayoutMediator.attach();
        mainMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                TextView view = (TextView) tab.getCustomView();
                view.setTextColor(Color.WHITE);
                if (position == 3) {
                    SharedPreferences read = getSharedPreferences("info", Context.MODE_PRIVATE);
                    String token = read.getString("token", "没有拿到token");
                    int user_id = read.getInt("user_id", 0);
                    if (token == "没有拿到token" || user_id == 0) {
                        //当前是未登录状态,用户点击了"我"这个tab提示用户要登录才能看个人信息
                        toastMessage("登录后才能查看个人主页");
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView view = (TextView) tab.getCustomView();
                view.setTextColor(Color.GRAY);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //权限已成功申请
                }else{
                    //用户拒绝授权
                    Toast.makeText(this, "无法获取SD卡读写权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void requestMyPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d("TAGG", "requestMyPermissions: 有写SD权限");
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d("TAGG", "requestMyPermissions: 有读SD权限");
        }
    }
    private class UploadListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //点了上传视频imageView
            //验证token有无
            SharedPreferences read=getSharedPreferences("info", Context.MODE_PRIVATE);
            String token=read.getString("token","没有拿到token");
            if(token=="没有拿到token"){
                //没有token,show login_dialog让用户登录或者注册
                LoginDialog loginDialog=new LoginDialog();
                loginDialog.show(getSupportFragmentManager(),null);
                toastMessage("登录后才能上传视频");
            }else{
                //有token,show uploadDialog
                UpLoadDialog upLoadDialog = new UpLoadDialog();
                upLoadDialog.show(getSupportFragmentManager(), null);
            }
        }
    }
    public void toastMessage (String message)
    {
        Toast toast = Toast.makeText(MainActivity.this,
                message, Toast.LENGTH_LONG);
        toast.show();
    }
    public class OnDoubleClickListener implements View.OnTouchListener{

        private int count = 0;//点击次数
        private long firstClick = 0;//第一次点击时间
        private long secondClick = 0;//第二次点击时间
        /**
         * 两次点击时间间隔，单位毫秒
         */
        private final int totalTime = 1000;
        /**
         * 自定义回调接口
         */


        /**
         * 触摸事件处理
         * @param v
         * @param event
         * @return
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {//按下
                count++;
                if (1 == count) {
                    firstClick = System.currentTimeMillis();//记录第一次点击时间
                } else if (2 == count) {
                    secondClick = System.currentTimeMillis();//记录第二次点击时间
                    if (secondClick - firstClick < totalTime) {//判断二次点击时间间隔是否在设定的间隔时间之内
                        Intent intent=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        firstClick = secondClick;
                        count = 1;
                    }
                    secondClick = 0;
                }
            }
            return true;
        }
    }
}