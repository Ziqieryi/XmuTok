package com.example.gsy;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gsy.fragment.upload.UpLoadDialog;

public class SettingDialog extends DialogFragment {
    Button exit;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen); //dialog全屏
    }
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
        final View view = inflater.inflate(R.layout.dialog_setting, null); //自己的布局文件
        exit=view.findViewById(R.id.settingExit);
        exit.setOnClickListener(new ExitClick());
        return view;
    }
    private class ExitClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //删除软件保存的info信息
            SharedPreferences write= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=write.edit();
            String token=write.getString("token",null);
            int user_id=write.getInt("user_id",0);
            String latest_time=write.getString("latest_time",null);
            if(token!=null){
                editor.remove("token");
            }
            if(user_id>0){
                editor.remove("user_id");
            }
            if(latest_time!=null){
                editor.remove("latest_time");
            }
            editor.commit();
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
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
}
