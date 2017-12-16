package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class FragmentDashboard extends Fragment {


    TextView tvActive,tvBlocked,tvTotalRecord,tvRecordToday,tvJumlahKoordinator,tvJumlahSibling,tvRequestBukaBlock,tvPerangkatAktif;
    LinearLayout llNoNetwork,llNetworkAvailable,llAdminOnly;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean loaddata;

    String JSON_data;
    String user_total;
    String user_block;
    String database_today;
    String database_total;
    String jml_koordinator;
    String jml_sibling;
    String request_open;
    String perangkat_aktif;
    String url;

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

    public FragmentDashboard() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterDashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);
        llNetworkAvailable = (LinearLayout) inflaterDashboard.findViewById(R.id.llNetworkAvailable);
        llNoNetwork = (LinearLayout) inflaterDashboard.findViewById(R.id.llNoNetwork);
        llAdminOnly = (LinearLayout) inflaterDashboard.findViewById(R.id.llAdminOnly);
        tvActive = (TextView) inflaterDashboard.findViewById(R.id.tvActive);
        tvBlocked = (TextView) inflaterDashboard.findViewById(R.id.tvBlocked);
        tvRecordToday = (TextView) inflaterDashboard.findViewById(R.id.tvRecordToday);
        tvTotalRecord = (TextView) inflaterDashboard.findViewById(R.id.tvTotalRecord);
        tvJumlahKoordinator = (TextView) inflaterDashboard.findViewById(R.id.tvJumlahKoordinator);
        tvJumlahSibling =  (TextView) inflaterDashboard.findViewById(R.id.tvJumlahSibling);
        tvRequestBukaBlock = (TextView) inflaterDashboard.findViewById(R.id.tvRequestBukaBlock);
        tvPerangkatAktif = (TextView) inflaterDashboard.findViewById(R.id.tvPerangkatAktif);
        return inflaterDashboard;
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

        llNoNetwork.setVisibility(View.GONE);
        llNetworkAvailable.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsyncDashboard async = new AsyncDashboard();
                            async.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000);
    }


    private class AsyncDashboard extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPSvc sh = new HTTPSvc();
            if(pref_tipe.equals("1")){
                url = api_dashboard;
            }else{
                url = api_dashboard.concat(pref_api_key);
            }
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    JSONArray user = response.getJSONArray("user");
                    JSONObject obj_user = user.getJSONObject(0);
                    user_total = obj_user.getString("jumlah_user_total");
                    user_block = obj_user.getString("jumlah_user_blocked");
                    JSONArray database = response.getJSONArray("database");
                    JSONObject obj_database = database.getJSONObject(0);
                    database_today = obj_database.getString("jumlah_record_today");
                    database_total = obj_database.getString("jumlah_record_total");
                    if(pref_tipe.equals("1")){
                        JSONArray jumlah_koordinator = response.getJSONArray("jumlah_koordinator");
                        JSONObject obj_koordinator = jumlah_koordinator.getJSONObject(0);
                        jml_koordinator = obj_koordinator.getString("jumlah_koordinator");
                        JSONArray jumlah_sibling = response.getJSONArray("jumlah_sibling");
                        JSONObject obj_sibling = jumlah_sibling.getJSONObject(0);
                        jml_sibling = obj_sibling.getString("jumlah_sibling");
                        JSONArray request_buka_block = response.getJSONArray("request_buka_block");
                        JSONObject obj_request = request_buka_block.getJSONObject(0);
                        request_open = obj_request.getString("request_buka_block");
                        JSONArray total_perangkat_aktif = response.getJSONArray("total_perangkat_aktif");
                        JSONObject obj_perangkat_aktif = total_perangkat_aktif.getJSONObject(0);
                        perangkat_aktif = obj_perangkat_aktif.getString("jumlah_perangkat_aktif");
                    }
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
            if(loaddata){
                llNetworkAvailable.setVisibility(View.VISIBLE);
                llNoNetwork.setVisibility(View.GONE);
                tvActive.setText(user_total);
                tvBlocked.setText(user_block);
                tvRecordToday.setText(database_today);
                tvTotalRecord.setText(database_total);
                if(pref_tipe.equals("1")){
                    llAdminOnly.setVisibility(View.VISIBLE);
                    tvJumlahKoordinator.setText(jml_koordinator);
                    tvJumlahSibling.setText(jml_sibling);
                    tvRequestBukaBlock.setText(request_open);
                    tvPerangkatAktif.setText(perangkat_aktif);
                }else{
                    llAdminOnly.setVisibility(View.GONE);
                }
            }else{
                llNetworkAvailable.setVisibility(View.GONE);
                llNoNetwork.setVisibility(View.VISIBLE);
            }
        }
    }

}
