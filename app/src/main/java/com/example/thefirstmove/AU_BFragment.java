package com.example.thefirstmove;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AU_BFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.au_bfragment,container,false);
        return view;
        //return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle saveInstanceState){
        super.onViewCreated(view,saveInstanceState);
        TextView Member2=(TextView)view.findViewById(R.id.member2);
        ImageView Mempt2=(ImageView)view.findViewById(R.id.mempt2);
    }
}
