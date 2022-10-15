package com.example.gsy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.gsy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Map<String,Object>> data;
    @Override
    public int getCount() {
        return data.size();
    }
    public ShowListAdapter(Context context,ArrayList<Map<String,Object>>data){
        inflater=LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_fans,parent,false);
        }
        CircleImageView head=convertView.findViewById(R.id.showList_head);
        TextView name=convertView.findViewById(R.id.showList_nickname);
        Map<String,Object> itemData=data.get(position);
        Picasso.get().load((String) itemData.get("avatar")).into(head);
        name.setText(""+(String) itemData.get("name"));
        return convertView;
    }
}
