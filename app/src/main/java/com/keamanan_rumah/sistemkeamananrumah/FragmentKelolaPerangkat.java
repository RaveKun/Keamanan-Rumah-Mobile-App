package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class FragmentKelolaPerangkat extends Fragment {

    LinearLayout llNoNetwork,llNetworkAvailable,llSecureKey;

    boolean loaddata;
    ProgressDialog pDialog;

    LinearLayout llNotif;
    TextView tvNotif;
    TextView tvStatusPerangkat;
    Button btnKelolaPerangkat;
    Button btnSecureKeyOk;
    Button btnKembali;
    EditText etSecureKey;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String JSON_data;
    String url;
    String str_status_perangkat;
    String param_update;

    String aktifkan = "1";
    String non_aktifkan = "2";
    String status_perangkat_terakhir;

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
    public static String api_monitoring;
    public static String api_update_perangkat;


    public FragmentKelolaPerangkat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterKelolaPerangkat = inflater.inflate(R.layout.fragment_kelola_perangkat, container, false);
        btnSecureKeyOk = (Button) inflaterKelolaPerangkat.findViewById(R.id.btnSecureKeyOk);
        btnKembali = (Button) inflaterKelolaPerangkat.findViewById(R.id.btnKembali);
        llSecureKey = (LinearLayout) inflaterKelolaPerangkat.findViewById(R.id.llSecureKey);
        etSecureKey = (EditText) inflaterKelolaPerangkat.findViewById(R.id.etSecureKey);
        llNetworkAvailable = (LinearLayout) inflaterKelolaPerangkat.findViewById(R.id.llNetworkAvailable);
        llNoNetwork = (LinearLayout) inflaterKelolaPerangkat.findViewById(R.id.llNoNetwork);
        llNotif = (LinearLayout) inflaterKelolaPerangkat.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterKelolaPerangkat.findViewById(R.id.tvNotif);
        tvStatusPerangkat = (TextView) inflaterKelolaPerangkat.findViewById(R.id.tvStatusPerangkat);
        btnKelolaPerangkat = (Button) inflaterKelolaPerangkat.findViewById(R.id.btnKelolaPerangkat);
        return inflaterKelolaPerangkat;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getActivity().getSharedPreferences("KEAMANAN_RUMAH", 0);
        editor = pref.edit();

        pref_id = pref.getString("ID",null);
        pref_username = pref.getString("USERNAME",null);
        pref_nama = pref.getString("NAMA",null);
        pref_tipe = pref.getString("TIPE",null);
        pref_api_key = pref.getString("API_KEY",null);
        pref_secure_key = pref.getString("SECURE_KEY",null);
        pref_waktu = pref.getString("WAKTU",null);

        api_daftar = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_daftar));
        api_dashboard = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_dashboard));
        api_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_profil)).concat(pref_id);
        api_update_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_profil)).concat(pref_id);
        api_update_password = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_password)).concat(pref_id);
        api_load_all_parent = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_parent));

        api_daftar = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_daftar));
        api_dashboard = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_dashboard));
        api_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_profil)).concat(pref_id);
        api_update_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_profil)).concat(pref_id);
        api_update_password = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_password)).concat(pref_id);
        api_load_all_parent = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_parent));
        api_monitoring = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_monitoring));
        api_update_perangkat = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_perangkat));

        llNoNetwork.setVisibility(View.GONE);
        llNetworkAvailable.setVisibility(View.VISIBLE);
        llSecureKey.setVisibility(View.GONE);

        btnKelolaPerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSecureKey.setVisibility(View.VISIBLE);
                llNetworkAvailable.setVisibility(View.GONE);
            }
        });

        btnSecureKeyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param_update = new String("");
                if(etSecureKey.getText().toString().equals("") || etSecureKey.getText().toString().length() < 4){
                    llNotif.setVisibility(View.VISIBLE);
                    tvNotif.setText("Secure key tidak boleh kosong, pastikan panjang Secure Key 4 digit.");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else{
                    if(etSecureKey.getText().toString().equals(pref_secure_key)){
                        if(btnKelolaPerangkat.getText().toString().equals("Aktifkan")){
                            param_update = aktifkan.concat("/").concat(pref_api_key).concat("/").concat(pref_id).concat("/");
                        }else
                        if(btnKelolaPerangkat.getText().toString().equals("Non-Aktifkan")){
                            param_update = non_aktifkan.concat("/").concat(pref_api_key).concat("/").concat(pref_id).concat("/");
                        }
                        new AsyncUpdatePerangkat().execute();
                    }else{
                        llNotif.setVisibility(View.VISIBLE);
                        tvNotif.setText("Secure Key yang Anda masukkan tidak cocok.");
                        tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                    }
                }
                etSecureKey.setText("");
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSecureKey.setVisibility(View.GONE);
                llNetworkAvailable.setVisibility(View.VISIBLE);
            }
        });


        llNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncStatusPerangkat().execute();
            }
        });

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsyncStatusPerangkat async= new AsyncStatusPerangkat();
                            async.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
    }

    private class AsyncStatusPerangkat extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            url = api_monitoring.concat(pref_api_key);
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    JSONObject obj_sensor = response.getJSONObject(0);
                    str_status_perangkat = obj_sensor.getString("status_perangkat");
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(isAdded()){
                if(loaddata){
                    if(str_status_perangkat.equals("1")){
                        tvStatusPerangkat.setText("Status Perangkat Aktif");
                        btnKelolaPerangkat.setText("Non-Aktifkan");
                        btnKelolaPerangkat.setBackgroundColor(Color.parseColor("#FFCA28"));
                    }else
                    if(str_status_perangkat.equals("2")){
                        tvStatusPerangkat.setText("Status Perangkat Tidak Aktif");
                        btnKelolaPerangkat.setText("Aktifkan");
                        btnKelolaPerangkat.setBackgroundColor(Color.parseColor("#1bbc9b"));
                    }
                    status_perangkat_terakhir = str_status_perangkat;
                }else{
                    llNetworkAvailable.setVisibility(View.GONE);
                    llNotif.setVisibility(View.VISIBLE);
                    tvNotif.setText("Gangguan saat terhubung ke server");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }
            }
        }
    }

    private class AsyncUpdatePerangkat extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            url = api_update_perangkat.concat(param_update);
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    JSONObject obj_sensor = response.getJSONObject(0);
                    str_status_perangkat = obj_sensor.getString("status_perangkat");
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(loaddata){
                llNotif.setVisibility(View.VISIBLE);
                if(str_status_perangkat.equals(status_perangkat_terakhir)){
                    tvNotif.setText("Ubah status perangkat berhasil");
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }else{
                    tvNotif.setText("Ubah status perangkat gagal. Periksa sambungan internet Anda");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }
                llSecureKey.setVisibility(View.GONE);
                llNetworkAvailable.setVisibility(View.VISIBLE);
            }else{
                tvNotif.setText("Error, terjadi kendala saat terhubung ke server.");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }
}
