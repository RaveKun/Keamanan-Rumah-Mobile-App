package com.keamanan_rumah.sistemkeamananrumah;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class FragmentMonitoring extends Fragment {

    LinearLayout llNoNetwork, llNetworkAvailable;

    TextView tvIndoor, tvOutdoor, tvDoorLock,tvStatusSensor;
    ImageView ivIndoor, ivOutdoor, ivDoorLock,ivStatusSensor;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean loaddata;

    String JSON_data;
    String url;
    String str_id;
    String str_state;
    String str_outdoor;
    String str_indoor;
    String str_ussrf;
    String str_magnetic;
    String str_datetime;
    String str_status_perangkat;

    int ln = 0;

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
    public FragmentMonitoring() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterMonitoring = inflater.inflate(R.layout.fragment_monitoring, container, false);
        llNetworkAvailable = (LinearLayout) inflaterMonitoring.findViewById(R.id.llNetworkAvailable);
        llNoNetwork = (LinearLayout) inflaterMonitoring.findViewById(R.id.llNoNetwork);
        tvIndoor = (TextView) inflaterMonitoring.findViewById(R.id.tvIndoor);
        tvOutdoor = (TextView) inflaterMonitoring.findViewById(R.id.tvOutdoor);
        tvDoorLock = (TextView) inflaterMonitoring.findViewById(R.id.tvDoorLock);
        tvStatusSensor = (TextView) inflaterMonitoring.findViewById(R.id.tvStatusSensor);
        ivIndoor = (ImageView)inflaterMonitoring.findViewById(R.id.ivIndoor);
        ivOutdoor = (ImageView)inflaterMonitoring.findViewById(R.id.ivOutdoor);
        ivDoorLock = (ImageView)inflaterMonitoring.findViewById(R.id.ivDoorLock);
        ivStatusSensor = (ImageView)inflaterMonitoring.findViewById(R.id.ivStatusSensor);
        return inflaterMonitoring;
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
        api_monitoring = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_monitoring));


        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsyncMonitoring async= new AsyncMonitoring();
                            async.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
    }

    private class AsyncMonitoring extends AsyncTask<Void, Void, Void> {
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
                    ln = response.length();
                    if(ln > 0){
                        JSONObject obj_sensor = response.getJSONObject(0);
                        str_id = obj_sensor.getString("id");
                        str_state = obj_sensor.getString("state");
                        str_indoor = obj_sensor.getString("indoor");
                        str_outdoor = obj_sensor.getString("outdoor");
                        str_ussrf = obj_sensor.getString("ussrf");
                        str_magnetic = obj_sensor.getString("magnetic");
                        str_datetime = obj_sensor.getString("datetime");
                        str_status_perangkat = obj_sensor.getString("status_perangkat");
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
                if(ln > 0){
                    llNetworkAvailable.setVisibility(View.VISIBLE);
                    llNoNetwork.setVisibility(View.GONE);
                    String status_indoor, status_outdoor,status_doorlock;
                    if(str_indoor.equals("0")){
                        status_indoor = "AMAN";
                        ivIndoor.setImageResource(R.mipmap.home_secure);
                    }else{
                        status_indoor = "Terdeteksi Orang";
                        ivIndoor.setImageResource(R.mipmap.burglar);
                    }
                    if(str_outdoor.equals("0")){
                        status_outdoor = "AMAN";
                        ivOutdoor.setImageResource(R.mipmap.home_secure);
                    }else{
                        status_outdoor = "Terdeteksi Orang.";
                        ivOutdoor.setImageResource(R.mipmap.burglar);
                    }
                    if(str_magnetic.equals("0")){
                        status_doorlock = "AMAN";
                        ivDoorLock.setImageResource(R.mipmap.pintu_tertutup);
                    }else{
                        status_doorlock = "Terbuka";
                        ivDoorLock.setImageResource(R.mipmap.pintu_terbuka);
                    }
                    if(str_status_perangkat.equals("1")){
                        tvStatusSensor.setText("Online");
                    }else{
                        tvStatusSensor.setText("Offline");
                        ivIndoor.setImageResource(R.mipmap.no_data);
                        ivOutdoor.setImageResource(R.mipmap.no_data);
                        ivDoorLock.setImageResource(R.mipmap.no_data);
                        status_indoor = "Sensor Offline";
                        status_outdoor = "Sensor Offline";
                        status_doorlock = "Sensor Offline";
                    }
                    tvIndoor.setText(status_indoor);
                    tvOutdoor.setText(status_outdoor);
                    tvDoorLock.setText(status_doorlock);
                }else{
                    ivIndoor.setImageResource(R.mipmap.no_data);
                    ivOutdoor.setImageResource(R.mipmap.no_data);
                    ivDoorLock.setImageResource(R.mipmap.no_data);
                    tvIndoor.setText("Tidak ada data");
                    tvOutdoor.setText("Tidak ada data");
                    tvDoorLock.setText("Tidak ada data");
                }
            }else {
                llNetworkAvailable.setVisibility(View.GONE);
                llNoNetwork.setVisibility(View.VISIBLE);
            }
        }
    }
}