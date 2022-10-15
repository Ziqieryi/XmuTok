package com.example.gsy.fragment.personal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.VpnService;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsy.MainActivity;
import com.example.gsy.R;
import com.example.gsy.SettingDialog;
import com.example.gsy.entity.AuthorFragmentData;
import com.example.gsy.request.UserInfoRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {
    ImageView personalBack;
    CircleImageView personalAvatar;
    TextView personalName;
    TextView personalSignature;
    TextView personalLikeCount;
    TextView personalFollowCount;
    TextView personalFansCount;
    Button personalSetting;
    private Handler uiHandler;
    int user_id;
    private boolean isGetData = false;
    public PersonalFragment() {
        // Required empty public constructor
    }
    public static PersonalFragment newInstance(AuthorFragmentData authorFragmentData) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",authorFragmentData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personal, container, false);
        //绑定布局控件
        personalAvatar=view.findViewById(R.id.personalAvatar);
        personalBack=view.findViewById(R.id.personalBack);
        personalFansCount=view.findViewById(R.id.personalFansCount);
        personalName=view.findViewById(R.id.personalName);
        personalFollowCount=view.findViewById(R.id.personalFollowCount);
        personalSignature=view.findViewById(R.id.personalSignature);
        personalLikeCount=view.findViewById(R.id.personalLikeCount);
        personalSetting=view.findViewById(R.id.personalSetting);
        //关注数和粉丝数的textview需要加监听,如果点击了,展示关注列表和粉丝列表
        personalFollowCount.setOnClickListener(new MyClick1());
        personalFansCount.setOnClickListener(new MyClick2());
        personalLikeCount.setText(""+20);
        personalSetting.setOnClickListener(new MyClick3());
        return view;
    }
    private class MyClick1 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //关注列表
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info",Context.MODE_PRIVATE);
            FollowListDialog followListDialog=FollowListDialog.newInstance(user_id,sharedPreferences.getString("token",null));
            followListDialog.show(getChildFragmentManager(),null);
        }
    }
    private class MyClick2 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //粉丝列表
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info",Context.MODE_PRIVATE);
            FansListDialog fansListDialog=FansListDialog.newInstance(user_id,sharedPreferences.getString("token",null));
            fansListDialog.show(getChildFragmentManager(),null);
        }
    }
    private class MyClick3 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SettingDialog settingDialog=new SettingDialog();
            settingDialog.show(getChildFragmentManager(),null);
        }
    }

    @Override
    public void onResume() {
        //可视前跑个请求更新ui
        SharedPreferences read=getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String token=read.getString("token","没有拿到token");
        int user_id=read.getInt("user_id",0);
        //发info请求,如果成功,则用response里的data更新ui,否则toast信息提示用户
        UserInfoRequest userInfoRequest=new UserInfoRequest(user_id,token);
        Log.d("TAGG","给我发请求！");
        Call call=userInfoRequest.getRequest();
        call.enqueue(new UserInfoRequestCallBack());
        super.onResume();
    }
    private class UserInfoRequestCallBack implements Callback {

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
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //解码
                try {
                    String responseJson = response.body().string();
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(responseJson);
                    int statusCode = jsonObject.getInt("status_code");
                    String statusMessage = jsonObject.getString("status_msg");
                    JSONObject user = jsonObject.getJSONObject("user");
                    if (statusCode != 0) {
                        //告诉错误信息
                        toastMessage(statusMessage);
                    } else {
                        //成功
                        personalFansCount.setText(""+user.getInt("follower_count"));
                        Log.d("TAGG", "进了+5+59");
//                        personalAvatar.setImageURI(Uri.parse(user.getString("avatar")));
//                        personalBack.setImageURI(Uri.parse(user.getString("background_image")));
                        Picasso.get().load(user.getString("avatar")).into(personalAvatar);
                        Picasso.get().load(user.getString("background_image")).into(personalBack);
                        personalFansCount.setText(""+user.getInt("follower_count"));
                        personalName.setText(""+user.getString("name"));
                        Log.d("TAGG", "进了2626");
                        personalFollowCount.setText(""+user.getInt("follow_count"));
                        personalSignature.setText(""+user.getString("signature"));
                        Log.d("TAGG", "进了5959");
                        user_id = user.getInt("id");
                    }
                } catch (JSONException e) {
                    //解码出错
                    toastMessage("本地解码失败");
                } catch (Exception e) {
                    toastMessage(e.getMessage());
                }
            }
        });
    }
}
    public void toastMessage (String message)
    {
        Toast toast = Toast.makeText(getContext(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }
    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }
}
