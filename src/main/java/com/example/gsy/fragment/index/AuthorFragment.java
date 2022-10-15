package com.example.gsy.fragment.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gsy.R;
import com.example.gsy.entity.AuthorFragmentData;
import com.example.gsy.entity.ItemFragmentData;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorFragment extends Fragment {
    ImageView authorBack;
    CircleImageView authorAvatar;
    TextView authorFollow;
    TextView authorName;
    TextView authorSignature;
    TextView authorLikeCount;
    TextView authorFollowCount;
    TextView authorFansCount;
    public AuthorFragment() {
        // Required empty public constructor
    }
    public static AuthorFragment newInstance(ItemFragmentData itemFragmentData) {
        AuthorFragment fragment = new AuthorFragment();
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
        View view= inflater.inflate(R.layout.fragment_author,null);
        authorBack=view.findViewById(R.id.authorBack);
        authorAvatar=view.findViewById(R.id.authorAvatar);
        authorFollow=view.findViewById(R.id.authorFollow);
        authorName=view.findViewById(R.id.authorName);
        authorSignature=view.findViewById(R.id.authorSignature);
        authorLikeCount=view.findViewById(R.id.authorLikeCount);
        authorFollowCount=view.findViewById(R.id.authorFollowCount);
        authorFansCount=view.findViewById(R.id.authorFansCount);
        Bundle bundle=getArguments();
        ItemFragmentData data=(ItemFragmentData) bundle.getSerializable("Data");
        Picasso.get().load(data.getAvatar()).into(authorAvatar);
        Picasso.get().load(data.getBackGroundImage()).into(authorBack);
        authorName.setText(""+data.getUserName());
        authorSignature.setText(""+data.getSignature());
        authorFollowCount.setText(""+data.getFollowCount());
        authorFansCount.setText(""+data.getFollowerCount());
        authorLikeCount.setText("20");
        return view;
    }
}
