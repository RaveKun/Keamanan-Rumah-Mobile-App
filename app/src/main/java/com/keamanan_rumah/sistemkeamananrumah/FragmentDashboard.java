package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class FragmentDashboard extends Fragment {

    String JSON_data;
    boolean loaddata;
    String user_total;
    String user_block;
    String database_today;
    String database_total;

    TextView tvActive,tvBlocked,tvTotalRecord,tvRecordToday;

    public FragmentDashboard() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterDashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tvActive = (TextView) inflaterDashboard.findViewById(R.id.tvActive);
        tvBlocked = (TextView) inflaterDashboard.findViewById(R.id.tvBlocked);
        tvRecordToday = (TextView) inflaterDashboard.findViewById(R.id.tvRecordToday);
        tvTotalRecord = (TextView) inflaterDashboard.findViewById(R.id.tvTotalRecord);
        return inflaterDashboard;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        timer.schedule(doAsynchronousTask, 0, 5000);
    }


    private class AsyncDashboard extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            String url = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_dashboard));
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
                tvActive.setText("User aktif : " + user_total);
                tvBlocked.setText("User blocked : " + user_block);
                tvRecordToday.setText("Data hari ini : " + database_today);
                tvTotalRecord.setText("Total data : " + database_total);
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

}
