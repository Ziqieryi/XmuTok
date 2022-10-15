package com.example.gsy.fragment.index;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gsy.R;
import com.example.gsy.adapter.IndexFragmentAdapter;
import com.example.gsy.entity.IndexFragmentData;
import com.example.gsy.entity.ItemFragmentData;
import com.example.gsy.request.FeedRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IndexFragment extends Fragment {
    ViewPager2 itemListContainer;
    int flag;//标志状态位,记录是否第一次初始化indexFragment
    public IndexFragment() {
        // Required empty public constructor
    }
    public static IndexFragment newInstance(IndexFragmentData Data) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putSerializable("Data", Data);
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
        View view=inflater.inflate(R.layout.fragment_index,null);
        //绑定视图
        itemListContainer=view.findViewById(R.id.ItemListContainer);
//        //拿到Data,实例化Container里每个ItemFragment
//        Bundle args=getArguments();
//        if(args!=null){
//            //有bundle,自定义视图
//            IndexFragmentData indexFragmentData =(IndexFragmentData) args.getSerializable("Data");
//            if(indexFragmentData==null){
//                toastMessage("网络开了点小差,请稍后双击首页重试456");
//            }else{
//                toastMessage("没有问题捏");
//            }
//        }else{
//            //没有data,疑似网络出错,提示用户
//            toastMessage("网络开了点小差,请稍后双击首页重试123");
//        }
        flag=0;
        return view;
    }
    @Override
    public void onResume() {
        //可视前跑个请求更新ui
        if(flag==0){
            SharedPreferences read=getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
            String token=read.getString("token",null);
            String latest_time=read.getString("latest_time",null);
            FeedRequest feedRequest=new FeedRequest(token,latest_time);
            Call call=feedRequest.getRequest();
            call.enqueue(new FeedCallBack());
        }else{
            //提示用户如果想导些新视频进来去双击首页导入
            toastMessage("双击首页加载新视频");
        }
        super.onResume();
    }
    private class FeedCallBack implements Callback{
        @Override
        public void onFailure(Call call, IOException e) {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastMessage("网络请求出错,请稍后双击首页重试");
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
                        Log.d("TAGG",responseJson);
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
                            String latest_time=jsonObject.getString("next_time");
                            SharedPreferences write= getActivity().getSharedPreferences("info",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=write.edit();
                            editor.putString("latest_time",latest_time);
                            editor.commit();
                            //解码video_list
                            List<ItemFragmentData>data=new ArrayList<ItemFragmentData>();
                            JSONArray videoList=jsonObject.getJSONArray("video_list");
                            for(int i=0;i<videoList.length();i++)
                            {
                                ItemFragmentData temp=new ItemFragmentData();
                                JSONObject thisVideo=(JSONObject) videoList.get(i);
                                JSONObject author=thisVideo.getJSONObject("author");
                                //填充作者
                                temp.setUserId(author.getInt("id"));
                                temp.setUserName(author.getString("name"));
                                temp.setFollowCount(author.getInt("follow_count"));
                                temp.setFollowerCount(author.getInt("follower_count"));
                                temp.setFollow(author.getBoolean("is_follow"));
                                temp.setSignature(author.getString("signature"));
                                temp.setAvatar(author.getString("avatar"));
                                temp.setBackGroundImage(author.getString("background_image"));
                                //填充视频
                                temp.setVideoId(thisVideo.getInt("id"));
                                temp.setPlayUrl(thisVideo.getString("play_url"));
                                temp.setCoverUrl(thisVideo.getString("cover_url"));
                                temp.setFavCount(thisVideo.getInt("favorite_count"));
                                temp.setTitle(thisVideo.getString("title"));
                                temp.setComCount(thisVideo.getInt("comment_count"));
                                temp.setFav(thisVideo.getBoolean("is_favorite"));
                                data.add(temp);
                            }
                                List<ItemFragment>itemFragmentList=new ArrayList<ItemFragment>();
                                for(int i = 0; i< data.size(); i++)
                                {
                                    ItemFragment itemFragment=ItemFragment.newInstance(data.get(i));
                                    itemFragmentList.add(itemFragment);
                                }
                                //设置容器的适配器,管理每个itemFragment
                                IndexFragmentAdapter indexFragmentAdapter =new IndexFragmentAdapter(getChildFragmentManager(),getLifecycle(),itemFragmentList);
                                itemListContainer.setAdapter(indexFragmentAdapter);
                                //设置滑动方向,上下刷动itemFragment
                                itemListContainer.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                                //懒加载,只预加载一个(position==0的ItemFragment)
                                itemListContainer.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
                                Log.d("TAGG",data.toString());
                                flag=1;
                        }
                    }catch(JSONException e){
                        //解码出错
                        toastMessage("本地解码失败123");
                    }catch(Exception e){
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
}
