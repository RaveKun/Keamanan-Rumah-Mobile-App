package com.keamanan_rumah.sistemkeamananrumah;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.net.Uri;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class BackgroundService extends Service {

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
    String exp[];
    String outdoor;
    String indoor;
    String magnetic;
    String recent_data = "";


    public static String pref_id;
    public static String pref_username;
    public static String pref_nama;
    public static String pref_tipe;
    public static String pref_api_key;
    public static String pref_secure_key;
    public static String pref_waktu;

    public static String id;
    public static String api_notif;

    Timer timer = new Timer();
    public BackgroundService(){}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("KEAMANAN RUMAH");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Notifikasi ON", Toast.LENGTH_LONG).show();
        pref =  getSharedPreferences("KEAMANAN_RUMAH", 0);
        editor = pref.edit();

        pref_id = pref.getString("ID",null);
        pref_username = pref.getString("USERNAME",null);
        pref_nama = pref.getString("NAMA",null);
        pref_tipe = pref.getString("TIPE",null);
        pref_api_key = pref.getString("API_KEY",null);
        pref_secure_key = pref.getString("SECURE_KEY",null);
        pref_waktu = pref.getString("WAKTU",null);

        api_notif = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_notif));

        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            GetJSONDataNotif getjsondata = new GetJSONDataNotif();
                            getjsondata.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private class GetJSONDataNotif extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("Service Keamanan Rumah","Do in background SERVICE NOTIFIKASI");
            HTTPSvc sh = new HTTPSvc();
            url = api_notif.concat(pref_api_key);
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    JSONObject obj_sensor = response.getJSONObject(0);
                    str_id = obj_sensor.getString("id");
                    str_state = obj_sensor.getString("state");
                    str_indoor = obj_sensor.getString("indoor");
                    str_outdoor = obj_sensor.getString("outdoor");
                    str_ussrf = obj_sensor.getString("ussrf");
                    str_magnetic = obj_sensor.getString("magnetic");
                    str_datetime = obj_sensor.getString("datetime");
                } catch (final JSONException e) {
                    Log.e("Service Keamanan Rumah", e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d("Service Keamanan Rumah", "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(loaddata){
                if(!str_state.equals("") ){
                    exp = str_state.split("_");
                    outdoor = exp[0];
                    indoor = exp[1];
                    magnetic = exp[2];
                    Log.d("outdoor :" , outdoor);
                    Log.d("indoor :" ,indoor);
                    Log.d("magnetic :" , magnetic);

                    if((indoor.equals("SIAGA") || indoor.equals("AWAS") || outdoor.equals("SIAGA") || outdoor.equals("AWAS") || magnetic.equals("SIAGA") || magnetic.equals("AWAS")) && !str_state.equals(recent_data)){
                        Notification.Builder builder = new Notification.Builder(getApplication().getBaseContext());
                        Intent notificationIntent = null;
                        if(pref_tipe.equals("2")){
                            notificationIntent = new Intent(getApplication().getBaseContext(),CoordinatorActivity.class);

                        }else
                        if(pref_tipe.equals("3")){
                            notificationIntent = new Intent(getApplication().getBaseContext(),SiblingActivity.class);
                        }
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        notificationIntent.putExtra("redirect", "monitoring");
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
                        builder.setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Notifikasi Keamanan Rumah")
                                .setContentText("Klik di sini untuk detail")
                                .setContentIntent(pendingIntent);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(alarmSound);
                        NotificationManager notificationManager = (NotificationManager) getApplication().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = builder.getNotification();
                        notificationManager.notify(R.drawable.notification_template_icon_bg, notification);
                    }
                    recent_data = str_state;
                }
            }
        }
    }
}
