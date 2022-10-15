package com.example.gsy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MainAdapter extends FragmentStateAdapter {
    List<Fragment> fragments;
    public MainAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment>fragments){
        super(fragmentManager,lifecycle);
        this.fragments=fragments;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
