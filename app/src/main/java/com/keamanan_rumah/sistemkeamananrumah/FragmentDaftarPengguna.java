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
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class FragmentDaftarPengguna extends Fragment {

    ProgressDialog pDialog;

    List<NameValuePair> data_update = new ArrayList<NameValuePair>(7);

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

    String status_cek,message,message_severity;
    String arr_username[];
    String arr_nama[];
    String arr_status[];
    String selected_id;

    public int len;

    public FragmentDaftarPengguna() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterDaftarPengguna = inflater.inflate(R.layout.fragment_daftar_pengguna, container, false);
        llNotif = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterDaftarPengguna.findViewById(R.id.tvNotif);
        rbActive = (RadioButton) inflaterDaftarPengguna.findViewById(R.id.radioActive);
        rbBlocked = (RadioButton) inflaterDaftarPengguna.findViewById(R.id.radioBlocked);
        llEditPengguna = (LinearLayout) inflaterDaftarPengguna.findViewById(R.id.llEditPengguna);
        lvPengguna = (ListView) inflaterDaftarPengguna.findViewById(R.id.lvPengguna);
        etUsername = (EditText) inflaterDaftarPengguna.findViewById(R.id.etUsername);
        etPassword = (EditText) inflaterDaftarPengguna.findViewById(R.id.etPassword);
        etNama = (EditText) inflaterDaftarPengguna.findViewById(R.id.etNama);
        etAlamat = (EditText) inflaterDaftarPengguna.findViewById(R.id.etAlamat);
        etTipeAccount = (EditText) inflaterDaftarPengguna.findViewById(R.id.etTipeAccount);
        etTanggalRegistrasi = (EditText) inflaterDaftarPengguna.findViewById(R.id.etTanggalRegistrasi);
        etApiKey = (EditText) inflaterDaftarPengguna.findViewById(R.id.etApiKey);
        etSecureKey = (EditText) inflaterDaftarPengguna.findViewById(R.id.etSecureKey);
        btnSimpan = (Button) inflaterDaftarPengguna.findViewById(R.id.btnSimpan);
        btnHapus = (Button) inflaterDaftarPengguna.findViewById(R.id.btnHapus);
        btnKembali = (Button) inflaterDaftarPengguna.findViewById(R.id.btnKembali);
        return inflaterDaftarPengguna;
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
        api_load_all_user = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_user));
        api_load_all_family = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_family));
        api_update_pengguna = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_pengguna));
        api_delete_pengguna = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_delete_pengguna));

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEditPengguna.setVisibility(View.GONE);
                lvPengguna.setVisibility(View.VISIBLE);
                llNotif.setVisibility(View.GONE);
                new AsyncPengguna().execute();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanPerubahan();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncDelete().execute();
            }
        });
        new AsyncPengguna().execute();
    }


    void dataToEdit(String username){
        try {
            JSONObject jsonObj = new JSONObject(JSON_data);
            JSONArray response = jsonObj.getJSONArray("response");
            for(int x=0;x<len;x++){
                JSONObject obj_pengguna = response.getJSONObject(x);
                arr_username[x] = obj_pengguna.getString("username");
                if(obj_pengguna.getString("username").toString().equals(username)){
                    selected_id = obj_pengguna.getString("id").toString();
                    etUsername.setText(obj_pengguna.getString("username").toString());
                    etPassword.setText(obj_pengguna.getString("password").toString());
                    etNama.setText(obj_pengguna.getString("nama").toString());
                    etAlamat.setText(obj_pengguna.getString("alamat").toString());
                    etTipeAccount.setText(obj_pengguna.getString("tipe").toString());
                    etTanggalRegistrasi.setText(obj_pengguna.getString("register_datetime").toString());
                    etApiKey.setText(obj_pengguna.getString("API_KEY").toString());
                    etSecureKey.setText(obj_pengguna.getString("secure_key").toString());
                    etUsername.setEnabled(false);
                    etPassword.setEnabled(true);
                    etNama.setEnabled(true);
                    etAlamat.setEnabled(true);
                    etTipeAccount.setEnabled(false);
                    etTanggalRegistrasi.setEnabled(false);
                    etApiKey.setEnabled(false);
                    etSecureKey.setEnabled(false);
                    if(obj_pengguna.getString("status").toString().equals("1")){
                        rbActive.setChecked(true);
                        rbBlocked.setChecked(false);
                    }else
                    if(obj_pengguna.getString("status").toString().equals("2")){
                        rbActive.setChecked(false);
                        rbBlocked.setChecked(true);
                    }
                }
            }
        } catch (final JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    void simpanPerubahan(){
        llNotif.setVisibility(View.VISIBLE);
        String str_pass = etPassword.getText().toString();
        String str_nama = etNama.getText().toString();
        String str_alamat = etAlamat.getText().toString();
        if(str_nama.equals("") || str_pass.equals("") || str_alamat.equals("")){
            tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
            tvNotif.setText("Pastikan semua field sudah diisi.");
        }else{
            data_update.add(new BasicNameValuePair("nama", str_nama));
            data_update.add(new BasicNameValuePair("password", str_pass));
            data_update.add(new BasicNameValuePair("alamat", str_alamat));
            if(rbActive.isChecked()){
                data_update.add(new BasicNameValuePair("status", "1"));
            }else
            if(rbBlocked.isChecked()){
                data_update.add(new BasicNameValuePair("status", "2"));
            }
            new AsyncUpdate().execute();
        }
    }

    private class AsyncPengguna extends AsyncTask<Void, Void, Void> {
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
            if(pref_tipe.equals("1")){
                url = api_load_all_user;
            }else{
                url = api_load_all_family.concat(pref_api_key);
            }
            Log.d(TAG,url);
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    len = response.length();
                    arr_username = new String[len];
                    arr_nama = new String[len];
                    arr_status = new String[len];
                    for(int x=0;x<len;x++){
                        JSONObject obj_pengguna = response.getJSONObject(x);
                        arr_username[x] = obj_pengguna.getString("username");
                        arr_nama[x] = obj_pengguna.getString("nama");
                        arr_status[x] = obj_pengguna.getString("status");
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
                if(len > 0){
                    Pengguna pengguna[] = new Pengguna[len];
                    for(int x=0;x<len;x++){
                        pengguna[x] = new Pengguna(arr_nama[x],arr_username[x],arr_status[x]);
                    }
                    AdapterPengguna adapter = new AdapterPengguna(getActivity(),R.layout.list_pengguna,pengguna);
                    lvPengguna.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lvPengguna.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView v = (TextView) view.findViewById(R.id.tvUsername);
                            String username = v.getText().toString();
                            llEditPengguna.setVisibility(View.VISIBLE);
                            lvPengguna.setVisibility(View.GONE);
                            dataToEdit(username);
                        }
                    });
                }
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncUpdate extends AsyncTask<Void, Void, Void> {
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
            String url = api_update_pengguna.concat(selected_id).concat("/");
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_update);
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

    private class AsyncDelete extends AsyncTask<Void, Void, Void> {
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
            String url = api_delete_pengguna.concat(selected_id).concat("/");
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_update);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    affected_rows = response.getString("affected_rows");
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
                if(Integer.parseInt(affected_rows) > 1){
                    tvNotif.setText("Hapus pengguna berhasil");
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }else
                if(Integer.parseInt(affected_rows) == 0){
                    tvNotif.setText("Hapus pengguna gagal");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }
            }else{
                tvNotif.setText("Error !");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }


}
