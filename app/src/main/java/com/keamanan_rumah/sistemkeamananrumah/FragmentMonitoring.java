package com.keamanan_rumah.sistemkeamananrumah;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMonitoring extends Fragment {

    public FragmentMonitoring() {
        // Required empty public constructor
    }


    public static FragmentMonitoring newInstance(String param1, String param2) {
        FragmentMonitoring fragment = new FragmentMonitoring();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitoring, container, false);
    }
}