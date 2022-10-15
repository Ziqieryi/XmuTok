package com.example.gsy.fragment.personal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gsy.R;
import com.example.gsy.adapter.ShowListAdapter;
import com.example.gsy.request.GetFansListRequest;
import com.example.gsy.request.GetFolListRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FollowListDialog extends DialogFragment {
    ListView fansList;
    int user_id;
    String token;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen); //dialog全屏
    }
    public static FollowListDialog newInstance(int user_id,String token) {
        FollowListDialog fragment = new FollowListDialog();
        Bundle args = new Bundle();
        args.putInt("user_id",user_id);
        args.putString("token",token);
        fragment.setArguments(args);
        return fragment;
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
        final View view = inflater.inflate(R.layout.show_list, null); //自己的布局文件
        fansList=view.findViewById(R.id.list);
        Bundle args=getArguments();
        user_id=args.getInt("user_id");
        token=args.getString("token");
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
    @Override
    public void onResume() {
        super.onResume();
        if (token==null||user_id==0){
            toastMessage("信息缺失,无法导入粉丝列表");
            return;
        }
        GetFolListRequest getFolListRequest=new GetFolListRequest(token,user_id);
        Call call=getFolListRequest.getRequest();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //解码
                        try {
                            String body=response.body().string();
                            JSONObject jsonObject=new JSONObject(body);
                            int statusCode=jsonObject.getInt("status_code");
                            String statusMsg=jsonObject.getString("status_msg");
                            if(statusCode!=0){
                                toastMessage(statusMsg);
                            }else{
                                //成功
                                JSONArray userList=jsonObject.getJSONArray("user_list");
                                ArrayList<Map<String,Object>> data=new ArrayList<>();
                                for(int i=0;i<userList.length();i++){
                                    JSONObject jsonObject1=userList.getJSONObject(i);
                                    Map<String,Object>map=new HashMap<>();
                                    String avatar=jsonObject1.getString("avatar");
                                    map.put("avatar",avatar);
                                    String name=jsonObject1.getString("name");
                                    map.put("name",name);
                                    data.add(map);
                                }
                                ShowListAdapter adapter=new ShowListAdapter(getContext(),data);
                                fansList.setAdapter(adapter);
                            }
                        } catch (IOException | JSONException e) {
                            toastMessage("本地解码失败");
                        }
                    }
                });
            }
        });
    }
    public void toastMessage(String message) {
        Toast toast = Toast.makeText(getContext(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }
}
