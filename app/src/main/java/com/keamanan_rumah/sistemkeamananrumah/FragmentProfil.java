package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentProfil extends Fragment {


    List<NameValuePair> data_profil = new ArrayList<NameValuePair>(3);
    LinearLayout llNoNetwork,llNetworkAvailable;

    boolean loaddata;
    ProgressDialog pDialog;

    LinearLayout llNotif;
    TextView tvNotif;
    EditText etUsername,etNama,etAlamat,etTipeAccount,etTanggalRegistrasi,etApiKey,etSecureKey;
    Button btnEdit,btnSimpan;

    String status_cek,message,message_severity;
    String JSON_data;
    String username, nama, alamat, tipe, tanggal, API_KEY, secure_key;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

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

    public FragmentProfil() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterProfil = inflater.inflate(R.layout.fragment_profil, container, false);
        llNetworkAvailable = (LinearLayout) inflaterProfil.findViewById(R.id.llNetworkAvailable);
        llNoNetwork = (LinearLayout) inflaterProfil.findViewById(R.id.llNoNetwork);
        llNotif = (LinearLayout) inflaterProfil.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterProfil.findViewById(R.id.tvNotif);
        etUsername = (EditText) inflaterProfil.findViewById(R.id.etUsername);
        etNama = (EditText) inflaterProfil.findViewById(R.id.etNama);
        etAlamat = (EditText) inflaterProfil.findViewById(R.id.etAlamat);
        etTipeAccount = (EditText) inflaterProfil.findViewById(R.id.etTipeAccount);
        etTanggalRegistrasi = (EditText) inflaterProfil.findViewById(R.id.etTanggalRegistrasi);
        etApiKey = (EditText) inflaterProfil.findViewById(R.id.etApiKey);
        etSecureKey = (EditText) inflaterProfil.findViewById(R.id.etSecureKey);
        btnEdit =(Button)inflaterProfil.findViewById(R.id.btnEdit);
        btnSimpan =(Button)inflaterProfil.findViewById(R.id.btnSimpan);
        return inflaterProfil;
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

        llNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncProfil().execute();
            }
        });

        new AsyncProfil().execute();
    }

    private class AsyncProfil extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mengambil data profil . . .");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            String url = api_profil;
            JSON_data = sh.makeServiceCall(url, HTTPSvc.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    JSONObject obj_profil = response.getJSONObject(0);
                    username = obj_profil.getString("username");
                    nama = obj_profil.getString("nama");
                    alamat = obj_profil.getString("alamat");
                    tipe = obj_profil.getString("tipe_user");
                    tanggal = obj_profil.getString("register_datetime");
                    API_KEY = obj_profil.getString("API_KEY");
                    secure_key = obj_profil.getString("secure_key");
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
                llNetworkAvailable.setVisibility(View.VISIBLE);
                llNoNetwork.setVisibility(View.GONE);
                etUsername.setText(username);
                etNama.setText(nama);
                etAlamat.setText(alamat);
                etTipeAccount.setText(tipe);
                etTanggalRegistrasi.setText(tanggal);
                etApiKey.setText(API_KEY);
                etSecureKey.setText(secure_key);

                etUsername.setEnabled(false);
                etNama.setEnabled(false);
                etAlamat.setEnabled(false);
                etTipeAccount.setEnabled(false);
                etTanggalRegistrasi.setEnabled(false);
                etApiKey.setEnabled(false);
                etSecureKey.setEnabled(false);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(btnEdit.getText().equals("Edit")){
                            btnEdit.setText("Kembali");
                            etNama.setEnabled(true);
                            etAlamat.setEnabled(true);
                            btnSimpan.setVisibility(View.VISIBLE);
                            btnSimpan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llNotif.setVisibility(View.VISIBLE);
                                    String upt_nama = etNama.getText().toString().trim();
                                    String upt_alamat = etAlamat.getText().toString().trim();
                                    if(upt_nama.equals("") || upt_nama == null || upt_alamat.equals("") || upt_alamat == null){
                                        tvNotif.setText("Semua field wajib diisi!");
                                        tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                                    }else{
                                        data_profil.add(new BasicNameValuePair("username", etUsername.getText().toString().trim()));
                                        data_profil.add(new BasicNameValuePair("nama", upt_nama));
                                        data_profil.add(new BasicNameValuePair("alamat", upt_alamat));
                                        new AsyncUpdateProfil().execute();
                                    }

                                }
                            });
                        }else
                        if(btnEdit.getText().equals("Kembali")){
                            btnEdit.setText("Edit");
                            etNama.setEnabled(false);
                            etAlamat.setEnabled(false);
                            btnSimpan.setVisibility(View.GONE);
                            tvNotif.setText("");
                            llNotif.setVisibility(View.GONE);
                        }
                    }
                });
            }else{
                llNetworkAvailable.setVisibility(View.GONE);
                llNoNetwork.setVisibility(View.VISIBLE);
            }
        }
    }

    private class AsyncUpdateProfil extends AsyncTask<Void, Void, Void> {
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
            String url = api_update_profil;
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_profil);
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
                if(message_severity.equals("success")){
                    etNama.setEnabled(false);
                    etAlamat.setEnabled(false);
                    btnSimpan.setVisibility(View.GONE);
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
