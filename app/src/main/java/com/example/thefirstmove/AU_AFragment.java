package com.example.thefirstmove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AU_AFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.au_afragment,container,false);
        return view;
        //return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle saveInstanceState){
        super.onViewCreated(view,saveInstanceState);
        //TextView Member1=(TextView)view.findViewById(R.id.member1);
        //ImageView Mempt1=(ImageView)view.findViewById(R.id.mempt1);
    }
}
