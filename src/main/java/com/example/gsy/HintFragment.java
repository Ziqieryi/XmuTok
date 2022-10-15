package com.example.gsy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.gsy.entity.ItemFragmentData;
import com.example.gsy.fragment.index.AuthorFragment;
import com.example.gsy.fragment.personal.LoginDialog;
import com.example.gsy.fragment.personal.LoginFragment;

public class HintFragment extends Fragment {
    Button hintTransfer;
    public HintFragment() {
        // Required empty public constructor
    }
    public static HintFragment newInstance(ItemFragmentData itemFragmentData) {
        HintFragment fragment = new HintFragment();
        Bundle args = new Bundle();
        args.putSerializable("itemFragmentData",itemFragmentData);
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
        View view=inflater.inflate(R.layout.hint_layout,null);
        hintTransfer=view.findViewById(R.id.hintTransfer);
        hintTransfer.setOnClickListener(new MyClick());
        //给转发button添加监听器,功能是点击之后打开一个新的登录dialog
        return view;
    }
    private class MyClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            LoginDialog loginDialog=new LoginDialog();
            loginDialog.show(getChildFragmentManager(),null);
        }
    }
}
