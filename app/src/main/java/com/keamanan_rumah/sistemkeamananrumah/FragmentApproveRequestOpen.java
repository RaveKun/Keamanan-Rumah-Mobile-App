package com.keamanan_rumah.sistemkeamananrumah;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentApproveRequestOpen extends Fragment {

    ProgressDialog pDialog;

    List<NameValuePair> data_request = new ArrayList<NameValuePair>(7);

    LinearLayout llNoNetwork, llNetworkAvailable;

    LinearLayout llRequestOpen;
    LinearLayout llNotif;
    ListView lvPengguna;
    TextView tvNotif;
    EditText etUsername, etTipe;
    Button btnApprove, btnKembali;

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
    public static String api_load_blocked_user;
    public static String api_request_open_block;
    public static String api_load_request_block;
    public static String api_approve_request_open;

    String status_cek, message, message_severity;
    String arr_id_request[];
    String arr_id_user_blocked[];
    String arr_username[];
    String arr_nama[];
    String arr_status[];
    String arr_tipe[];
    String selected_id_user;
    String selected_id_request;
    String selected_username,selected_tipe;

    public int len;

    public FragmentApproveRequestOpen() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflaterDaftarPengguna = inflater.inflate(R.layout.fragment_approve_request_open, container, false);
        llRequestOpen = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llRequestOpen);
        llNetworkAvailable = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNetworkAvailable);
        llNoNetwork = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNoNetwork);
        llNotif = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterDaftarPengguna.findViewById(R.id.tvNotif);
        lvPengguna = (ListView) inflaterDaftarPengguna.findViewById(R.id.lvPengguna);
        etUsername = (EditText) inflaterDaftarPengguna.findViewById(R.id.etUsername);
        etTipe = (EditText) inflaterDaftarPengguna.findViewById(R.id.etTipeAccount);
        btnKembali = (Button) inflaterDaftarPengguna.findViewById(R.id.btnKembali);
        btnApprove = (Button) inflaterDaftarPengguna.findViewById(R.id.btnApprove);
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
        api_load_blocked_user = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_blocked_user));
        api_request_open_block = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_request_open_block));
        api_load_request_block = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_request_block));
        api_approve_request_open = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_approve_request_open));


        llNoNetwork.setVisibility(View.GONE);
        llNetworkAvailable.setVisibility(View.VISIBLE);
        llRequestOpen.setVisibility(View.GONE);

        llNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncListRequestOpen().execute();
                llNotif.setVisibility(View.GONE);
                tvNotif.setText("");
                llNoNetwork.setVisibility(View.GONE);
                llRequestOpen.setVisibility(View.GONE);
                lvPengguna.setVisibility(View.VISIBLE);
            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncApprove().execute();
            }
        });

        new AsyncListRequestOpen().execute();
    }

    private class AsyncListRequestOpen extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Load list pengguna . . .");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            url = api_load_request_block;
            Log.d(TAG,url);
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    len = response.length();
                    arr_id_request = new String[len];
                    arr_id_user_blocked = new String[len];
                    arr_username = new String[len];
                    arr_nama = new String[len];
                    arr_tipe = new String[len];
                    arr_status = new String[len];
                    for(int x=0;x<len;x++){
                        JSONObject obj_pengguna = response.getJSONObject(x);
                        arr_id_request[x] = obj_pengguna.getString("id");
                        arr_id_user_blocked[x] = obj_pengguna.getString("user_blocked");
                        arr_username[x] = obj_pengguna.getString("username");
                        arr_nama[x] = obj_pengguna.getString("nama");
                        arr_status[x] = "2";
                        arr_tipe[x] = obj_pengguna.getString("tipe");
                    }
                    loaddata=true;

                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                    loaddata = false;
                }
            }
            else{
                loaddata=false;
            }
            Log.d(TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            super.onPostExecute(result);
            if(loaddata){
                llNoNetwork.setVisibility(View.GONE);
                llNetworkAvailable.setVisibility(View.VISIBLE);
                if(len > 0){
                    Pengguna pengguna[] = new Pengguna[len];
                    for(int x=0;x<len;x++){
                        if(arr_status[x].equals("1")){
                            pengguna[x] = new Pengguna(arr_nama[x],arr_username[x],"3");
                        }else{
                            pengguna[x] = new Pengguna(arr_nama[x],arr_username[x], arr_status[x]);
                        }

                    }
                    AdapterPengguna adapter = new AdapterPengguna(getActivity(),R.layout.list_pengguna,pengguna);
                    lvPengguna.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lvPengguna.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView tvUs = (TextView) view.findViewById(R.id.tvUsername);
                            TextView tvSt = (TextView) view.findViewById(R.id.tvStatus);
                            selected_username = tvUs.getText().toString();
                            for(int x = 0;x < arr_username.length;x++){
                                if(arr_username[x].equals(selected_username)){
                                    selected_id_user = arr_id_user_blocked[x];
                                    selected_id_request = arr_id_request[x];
                                    etUsername.setText(selected_username);
                                    etTipe.setText(arr_tipe[x]);
                                    if(tvSt.getText().toString().equals("On proccess request open")){
                                        btnApprove.setVisibility(View.GONE);
                                    }else{
                                        btnApprove.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            llRequestOpen.setVisibility(View.VISIBLE);
                            lvPengguna.setVisibility(View.GONE);
                        }
                    });
                }else{
                    llRequestOpen.setVisibility(View.GONE);
                    lvPengguna.setVisibility(View.GONE);
                    llNotif.setVisibility(View.VISIBLE);
                    tvNotif.setText("Tidak ada pengguna terdaftar didatabase");
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }
            }else{
                llNoNetwork.setVisibility(View.VISIBLE);
                llNetworkAvailable.setVisibility(View.GONE);
            }
        }
    }

    private class AsyncApprove extends AsyncTask<Void, Void, Void> {
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
            data_request.add(new BasicNameValuePair("id_request", selected_id_request));
            data_request.add(new BasicNameValuePair("id_user", selected_id_user));
            data_request.add(new BasicNameValuePair("status", "2"));
            String url = api_approve_request_open;
            String JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_request);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    status_cek = response.getString("status_cek");
                    message = response.getString("message");
                    message_severity = response.getString("message_severity");
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
                tvNotif.setText(message);
                if(status_cek.equals("SUCCESS")){
                    lvPengguna.setVisibility(View.GONE);
                    llRequestOpen.setVisibility(View.GONE);
                    llNetworkAvailable.setVisibility(View.VISIBLE);
                    llNoNetwork.setVisibility(View.GONE);
                    llNotif.setVisibility(View.VISIBLE);
                    btnApprove.setVisibility(View.GONE);
                }
                if(message_severity.equals("success")){
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }else
                if(message_severity.equals("warning")){
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else
                if(message_severity.equals("danger")){
                    tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                }
            }else{
                tvNotif.setText("Error !");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }
}

