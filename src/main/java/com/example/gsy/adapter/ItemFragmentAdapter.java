package com.example.gsy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gsy.fragment.index.AuthorFragment;
import com.example.gsy.fragment.index.VideoFragment;

public class ItemFragmentAdapter extends FragmentStateAdapter{

    public VideoFragment videoFragment;
    public AuthorFragment authorFragment;
    public ItemFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, VideoFragment videoFragment, AuthorFragment authorFragment)
    {
        super(fragmentManager, lifecycle);
        this.videoFragment=videoFragment;
        this.authorFragment=authorFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return videoFragment;
        }else{
            return authorFragment;
        }
    }
}
