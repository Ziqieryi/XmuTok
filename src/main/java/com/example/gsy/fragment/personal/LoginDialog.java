package com.example.gsy.fragment.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gsy.MainActivity;
import com.example.gsy.R;
import com.example.gsy.request.LoginRequest;
import com.example.gsy.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginDialog extends DialogFragment {
    EditText loginAccount,loginPassword;
    Button loginLogin,loginRegister;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.DialogFullScreen); //dialog全屏
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
        final View view = inflater.inflate(R.layout.dialog_login, null); //自己的布局文件
        loginAccount=view.findViewById(R.id.loginAccount);
        loginPassword=view.findViewById(R.id.loginPassword);
        loginLogin=view.findViewById(R.id.login_login);
        loginRegister=view.findViewById(R.id.loginRegister);
        //给登录和注册分别注册监听器
        loginLogin.setOnClickListener(new LoginClick());
        loginRegister.setOnClickListener(new RegisterClick());
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
    private class LoginClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //根据已有的账号和密码发送登录请求并异步处理
            LoginRequest loginRequest=new LoginRequest(loginAccount.getText().toString(),loginPassword.getText().toString());
            Call call=loginRequest.getRequest();
            call.enqueue(new LoginRequestCallBack());
        }
    }
    private class RegisterClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //根据已有的账号和密码发送登录请求并异步处理
            //根据已有的账号和密码发送注册请求并异步处理
            RegisterRequest registerRequest=new RegisterRequest(loginAccount.getText().toString(),loginPassword.getText().toString());
            Call call=registerRequest.getRequest();
            call.enqueue(new RegisterRequestCallBack());
        }
    }
    private class LoginRequestCallBack implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastMessage("网络请求失败");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //解码
                    try{
                        String responseJson=response.body().string();
                        JSONObject jsonObject=null;
                        jsonObject=new JSONObject(responseJson);
                        int statusCode=jsonObject.getInt("status_code");
                        String statusMessage=jsonObject.getString("status_msg");
                        int user_id=jsonObject.getInt("user_id");
                        String token=jsonObject.getString("token");
                        if(statusCode!=0){
                            //告诉错误信息
                            toastMessage(statusMessage);
                        }else{
                            //成功,把服务器给的token和userid存起来并重启应用
                            toastMessage("登录成功！重启应用完毕");
                            SharedPreferences write= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=write.edit();
                            editor.putInt("user_id",user_id);
                            editor.putString("token",token);
                            editor.commit();
                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
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
    private class RegisterRequestCallBack implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastMessage("网络请求失败");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //解码
                    try{
                        String responseJson=response.body().string();
                        JSONObject jsonObject=null;
                        jsonObject=new JSONObject(responseJson);
                        int statusCode=jsonObject.getInt("status_code");
                        String statusMessage=jsonObject.getString("status_msg");
                        int user_id=jsonObject.getInt("user_id");
                        String token=jsonObject.getString("token");
                        if(statusCode!=0){
                            //告诉错误信息
                            toastMessage(statusMessage);
                        }else{
                            //成功,把服务器给的token和userid存起来并重启应用
                            toastMessage("注册成功！即将重启应用");
                            SharedPreferences write= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=write.edit();
                            editor.putInt("user_id",user_id);
                            editor.putString("token",token);
                            editor.commit();
                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
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
