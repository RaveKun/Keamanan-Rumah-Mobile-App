package com.keamanan_rumah.sistemkeamananrumah;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.keamanan_rumah.sistemkeamananrumah.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    Timer timer = new Timer();
    public BackgroundService(){}
    SharedPreferences pref = getApplicationContext().getSharedPreferences("KEAMANAN_RUMAH", 0);

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID",null);
        editor.putString("USERNAME",null);
        editor.putString("TIPE",null);
        editor.putString("API_KEY",null);
        editor.putString("SECURITY_KEY",null);
        editor.putString("WAKTU",null);
        editor.commit();

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
        timer.schedule(doAsynchronousTask, 0, 5000);
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
            if(pref.getString("ID",null) == null){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
