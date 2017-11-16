package com.keamanan_rumah.sistemkeamananrumah;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentLandingRoot extends Fragment {

    TextView tvId, tvUsername, tvNama, tvTipe, tvAPIKEY, tvSecure, tvWaktu;

    public FragmentLandingRoot() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterRootLanding = inflater.inflate(R.layout.fragment_landing_root, container, false);
        tvId = (TextView) inflaterRootLanding.findViewById(R.id.id);
        tvUsername = (TextView) inflaterRootLanding.findViewById(R.id.username);
        tvNama = (TextView) inflaterRootLanding.findViewById(R.id.nama);
        tvTipe = (TextView) inflaterRootLanding.findViewById(R.id.tipe);
        tvAPIKEY = (TextView) inflaterRootLanding.findViewById(R.id.API_KEY);
        tvSecure = (TextView) inflaterRootLanding.findViewById(R.id.secure_key);
        tvWaktu = (TextView) inflaterRootLanding.findViewById(R.id.waktu);
        return inflaterRootLanding;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvId.setText(RootActivity.id);
        tvUsername.setText(RootActivity.username);
        tvNama.setText(RootActivity.nama);
        tvTipe.setText(RootActivity.tipe);
        tvAPIKEY.setText(RootActivity.API_KEY);
        tvSecure.setText(RootActivity.secure_key);
        tvWaktu.setText(RootActivity.waktu);
    }
}
