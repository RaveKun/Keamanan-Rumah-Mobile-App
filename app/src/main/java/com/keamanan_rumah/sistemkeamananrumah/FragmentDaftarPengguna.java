package com.keamanan_rumah.sistemkeamananrumah;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class FragmentDaftarPengguna extends Fragment {

    ListView lvPengguna;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean loaddata;

    String JSON_data;
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
    public static String api_load_all_user;
    public static String api_load_all_family;

    public int len;

    String arr_username[];
    String arr_nama[];
    String arr_status[];

    public FragmentDaftarPengguna() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterDaftarPengguna = inflater.inflate(R.layout.fragment_daftar_pengguna, container, false);
        lvPengguna = (ListView) inflaterDaftarPengguna.findViewById(R.id.lvPengguna);
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

        new AsyncPengguna().execute();
    }

    private class AsyncPengguna extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
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
            super.onPostExecute(result);
            if(loaddata){
                if(len > 0){
                    Pengguna pengguna[] = new Pengguna[len];
                    for(int x=0;x<len;x++){
                        pengguna[x] = new Pengguna(arr_nama[x],arr_username[x],arr_status[x]);
                    }
                    AdapterPengguna adapter = new AdapterPengguna(getActivity(),R.layout.list_pengguna,pengguna);
                    lvPengguna.setAdapter(adapter);
                    lvPengguna.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView v = (TextView) view.findViewById(R.id.tvNama);
                            Toast.makeText(getActivity().getBaseContext(),v.getText().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

}
