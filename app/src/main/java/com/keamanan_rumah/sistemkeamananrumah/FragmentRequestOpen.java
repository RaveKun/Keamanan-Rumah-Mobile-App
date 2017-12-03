package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


public class FragmentDaftarPengguna extends Fragment {

    ProgressDialog pDialog;

    List<NameValuePair> data_update = new ArrayList<NameValuePair>(7);

    LinearLayout llNoNetwork, llNetworkAvailable;

    LinearLayout llEditPengguna;
    LinearLayout llNotif;
    ListView lvPengguna;
    EditText etUsername;
    EditText etPassword;
    EditText etNama;
    EditText etAlamat;
    EditText etTipeAccount;
    EditText etTanggalRegistrasi;
    EditText etApiKey;
    EditText etSecureKey;
    Button btnSimpan;
    Button btnHapus;
    Button btnKembali;
    RadioButton rbActive;
    RadioButton rbBlocked;
    TextView tvNotif;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean loaddata;

    String JSON_data;
    String url;

    String affected_rows;

    public static String pref_id;
    public static String pref_username;
    public static String pref_nama;
    public static String pref_tipe;
    public static String pref_api_key;
    public static String pref_secure_key;
    public static String pref_waktu;

    public static String id;
    public static String api_daftar;
    public static String api_dashboard;
    public static String api_profil;
    public static String api_update_profil;
    public static String api_update_password;
    public static String api_load_all_parent;
    public static String api_load_all_user;
    public static String api_load_all_family;
    public static String api_update_pengguna;
    public static String api_delete_pengguna;

    String status_cek, message, message_severity;
    String arr_username[];
    String arr_nama[];
    String arr_status[];
    String selected_id;

    public int len;

    public FragmentDaftarPengguna() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflaterDaftarPengguna = inflater.inflate(R.layout.fragment_request_open, container, false);
        llNetworkAvailable = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNetworkAvailable);
        llNoNetwork = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNoNetwork);
        llNotif = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterDaftarPengguna.findViewById(R.id.tvNotif);
        return inflaterDaftarPengguna;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getActivity().getSharedPreferences("KEAMANAN_RUMAH", 0);
        editor = pref.edit();

        pref_id = pref.getString("ID", null);
        pref_username = pref.getString("USERNAME", null);
        pref_nama = pref.getString("NAMA", null);
        pref_tipe = pref.getString("TIPE", null);
        pref_api_key = pref.getString("API_KEY", null);
        pref_secure_key = pref.getString("SECURE_KEY", null);
        pref_waktu = pref.getString("WAKTU", null);

        api_daftar = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_daftar));
        api_dashboard = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_dashboard));
        api_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_profil)).concat(pref_id);
        api_update_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_profil)).concat(pref_id);
        api_update_password = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_password)).concat(pref_id);
        api_load_all_parent = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_parent));
        api_load_all_user = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_user));
        api_load_all_family = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_family));
        api_update_pengguna = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_pengguna));
        api_delete_pengguna = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_delete_pengguna));

        llNoNetwork.setVisibility(View.GONE);
        llNetworkAvailable.setVisibility(View.VISIBLE);

        llNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEditPengguna.setVisibility(View.GONE);
                lvPengguna.setVisibility(View.VISIBLE);
                llNotif.setVisibility(View.GONE);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanPerubahan();
            }
        });

        
    }
}
