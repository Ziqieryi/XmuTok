package com.example.gsy.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gsy.fragment.index.ItemFragment;

import java.util.List;

public class IndexFragmentAdapter extends FragmentStateAdapter {
    public List<ItemFragment>itemFragmentList;
    public IndexFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<ItemFragment>itemFragmentList)
    {
        super(fragmentManager, lifecycle);
        this.itemFragmentList=itemFragmentList;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return itemFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return itemFragmentList.size();
    }
}
