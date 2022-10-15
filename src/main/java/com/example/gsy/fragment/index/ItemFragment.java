package com.example.gsy.fragment.index;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.GenericTransitionOptions;
import com.example.gsy.MainActivity;
import com.example.gsy.R;
import com.example.gsy.adapter.ItemFragmentAdapter;
import com.example.gsy.entity.ItemFragmentData;

public class ItemFragment extends Fragment {
    ViewPager2 itemContainer;
    FragmentManager ItemManager;
    ItemFragmentAdapter itemFragmentAdapter;
    ItemFragmentData data;
    public ItemFragment() {
        // Required empty public constructor
    }

    public static ItemFragment newInstance(ItemFragmentData itemFragmentData) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("itemFragmentData", itemFragmentData);
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
        View view=inflater.inflate(R.layout.fragment_item,null);
        //绑定视图
        itemContainer=view.findViewById(R.id.ItemContainer);
        ItemManager=getChildFragmentManager();
        //实例化videoFragment
        Bundle args=getArguments();
        VideoFragment videoFragment=null;
        AuthorFragment authorFragment=null;
        if(args!=null){
            data=(ItemFragmentData) args.getSerializable("itemFragmentData");
            Log.d("TAGG","这里也完成了吗");
            if(data!=null)
            {
                videoFragment=VideoFragment.newInstance(data);
                Log.d("TAGG",data.toString());
                authorFragment=AuthorFragment.newInstance(data);
            }
        }
        //绑定适配器
        itemFragmentAdapter=new ItemFragmentAdapter(getChildFragmentManager(),getLifecycle(),videoFragment,authorFragment);
        itemContainer.setAdapter(itemFragmentAdapter);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    public void onResume() {
        super.onResume();
    }
    public void onStop() {
        super.onStop();
    }
    public void onPause() {
        super.onPause();
    }
}
