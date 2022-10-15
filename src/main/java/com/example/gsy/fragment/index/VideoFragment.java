package com.example.gsy.fragment.index;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.gsy.MainActivity;
import com.example.gsy.R;
import com.example.gsy.entity.ItemFragmentData;
import com.example.gsy.fragment.personal.LoginDialog;
import com.example.gsy.fragment.personal.PersonalFragment;
import com.example.gsy.request.FavouriteRequest;
import com.example.gsy.request.FeedRequest;
import com.example.gsy.request.FollowActionRequest;
import com.example.gsy.request.UserInfoRequest;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class VideoFragment extends Fragment {
    StandardGSYVideoPlayer videoPlayer;
    CircleImageView authorAvatar;
    ImageView follow,favourite,comment;
    TextView favouriteCount,commentCount,nickName,title;
    int favActionType;
    int folActionType;
    int authorId;
    int videoId;
    public VideoFragment() {
        // Required empty public constructor
    }
    public static VideoFragment newInstance(ItemFragmentData itemFragmentData) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putSerializable("Data",itemFragmentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout view=(RelativeLayout)inflater.inflate(R.layout.fragment_video,null);
        //绑定视图
        videoPlayer=view.findViewById(R.id.videoPlayer);
        authorAvatar=view.findViewById(R.id.video_fragment_avatar);
        follow=view.findViewById(R.id.video_fragment_follow);
        favourite=view.findViewById(R.id.video_fragment_favourite);
        favouriteCount=view.findViewById(R.id.video_fragment_favCount);
        commentCount=view.findViewById(R.id.video_fragment_comCount);
        nickName=view.findViewById(R.id.video_fragment_nickName);
        title=view.findViewById(R.id.video_fragment_title);
        //从bundle里取出data初始化View
        Bundle bundle=getArguments();
        ItemFragmentData data=(ItemFragmentData) bundle.getSerializable("Data");
//        videoPlayer.setUp(data.getPlayUrl(),"");
        videoPlayer.setUp(data.getPlayUrl(),true,"");
        Log.d("TAGG","565656完成了几次呢？");
        Picasso.get().load(data.getAvatar()).into(authorAvatar);
        Log.d("TAGG","完成了几次呢？");
        if(data.isFollow())follow.setVisibility(View.GONE);
        else{
            //给关注加监听
            follow.setOnClickListener(new focusClick());
            //设置下次操作类型为关注,1-关注,2-取消关注
            folActionType=1;
        }
        if(data.isFav()){
            //已经点赞过,图片为红色桃心
            favourite.setImageResource(R.drawable.ic_like);
            //设置下次操作类型为取消点赞,
            favActionType=2;
        }else{
            //没有点赞,图片为灰色桃心
            favourite.setImageResource(R.drawable.icon_like);
            favActionType=1;
        }
        favouriteCount.setText(""+data.getFavCount());
        favourite.setOnClickListener(new favClick());
        commentCount.setText(""+data.getComCount());
        videoId=data.getVideoId();
        authorId=data.getUserId();
        nickName.setText(data.getUserName());
        title.setText(data.getTitle());
        Log.d("TAGG","这里完成了几次呢？");
        return view;
    }
    private class focusClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //根据已有的actionType状态记录和authorId发送关注请求
            SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info",Context.MODE_PRIVATE);
            String token=sharedPreferences.getString("token",null);
            if(token==null){
                toastMessage("登录后才能关注");
                LoginDialog loginDialog = new LoginDialog();
                loginDialog.show(getChildFragmentManager(), null);
            }else {
                FollowActionRequest followActionRequest=new FollowActionRequest(token,authorId,folActionType);
                Call call=followActionRequest.getRequest();
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastMessage("关注请求失败");
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
                                        follow.setVisibility(View.GONE);
                                        folActionType=2;
                                        toastMessage("关注成功");
                                    }
                                } catch(Exception e){
                                    toastMessage(e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        }
    }
    private class favClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            if (token == null) {
                toastMessage("登录后才能点赞");
                LoginDialog loginDialog = new LoginDialog();
                loginDialog.show(getChildFragmentManager(), null);
            } else {
                FavouriteRequest favouriteRequest=new FavouriteRequest(token,videoId,favActionType);
                Call call=favouriteRequest.getRequest();
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
                                        if (favActionType==1){
                                            //发的是喜欢请求
                                            favActionType=2;
                                            favourite.setImageResource(R.drawable.ic_like);
                                            int count=Integer.parseInt(favouriteCount.getText().toString());
                                            count++;
                                            favouriteCount.setText(""+count);
                                            //红色桃心
                                        }else{
                                            //发的是取消喜欢请求
                                            favActionType=1;
                                            favourite.setImageResource(R.drawable.icon_like);
                                            int count=Integer.parseInt(favouriteCount.getText().toString());
                                            count--;
                                            favouriteCount.setText(""+count);
                                            //白色桃心
                                        }
                                    }
                                } catch(Exception e){
                                    toastMessage(e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
            }
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoPlayer.onVideoResume();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoPlayer.onVideoPause();
            }
        });
    }
    public void toastMessage(String message) {
        Toast toast = Toast.makeText(getContext(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }
}
