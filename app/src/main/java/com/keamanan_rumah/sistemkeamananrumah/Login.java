package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    List<NameValuePair> data_login = new ArrayList<NameValuePair>(7);

    Button btnDaftar,btnLogin;
    EditText editUsername,editPass;
    TextView tvNotif;
    ProgressDialog pDialog;

    String u,p;
    String api_site_url,api_login;
    String TAG;
    String status_cek,message,message_severity;
    String id, username, nama, tipe, API_KEY, secure_key, waktu;
    String pref_tipe;

    Boolean loaddata;
    String JSON_data;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TAG = getResources().getString(R.string.TAG);
        api_site_url = getResources().getString(R.string.api_site_url);
        api_login = getResources().getString(R.string.api_login);
        btnDaftar = (Button)findViewById(R.id.btnDaftar);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        editUsername = (EditText)findViewById(R.id.editUsername);
        editPass = (EditText)findViewById(R.id.editPass);
        tvNotif = (TextView)findViewById(R.id.tvNotif);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Daftar.class);
                finish();
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = editUsername.getText().toString();
                p = editPass.getText().toString();
               if(!u.equals("") && !p.equals("")){
                    data_login.add(new BasicNameValuePair("username", u));
                    data_login.add(new BasicNameValuePair("password", p));
                    new AsyncLogin().execute();
                }else
                if(u.equals("") || p.equals("")){
                    tvNotif.setText("Masukkan Username dan Password");
                }
            }
        });

        pref = getApplicationContext().getSharedPreferences("KEAMANAN_RUMAH", 0);
        editor = pref.edit();
        pref_tipe = pref.getString("TIPE",null);


        if(pref_tipe != null){
            if(pref_tipe.equals("1")){
//            Intent i = new Intent(Login.this, RootActivity.class);
                Toast.makeText(getApplicationContext(),"Anda sedang login sebagai root",Toast.LENGTH_LONG).show();
            }else
            if(pref_tipe.equals("2")){
//            Intent i = new Intent(Login.this, RootActivity.class);
                Toast.makeText(getApplicationContext(),"Anda sedang login sebagai coordinator",Toast.LENGTH_LONG).show();
            }else
            if(pref_tipe.equals("3")){
//            Intent i = new Intent(Login.this, RootActivity.class);
                Toast.makeText(getApplicationContext(),"Anda sedang login sebagai sibling",Toast.LENGTH_LONG).show();
            }
        }


    }

    private class AsyncLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            String url = api_site_url.concat(api_login);
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_login);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    status_cek = response.getString("status_cek");
                    message = response.getString("message");
                    message_severity = response.getString("message_severity");
                    //user
                    JSONArray data_user = response.getJSONArray("data_user");
                    JSONObject objUser = data_user.getJSONObject(0);
                    id = objUser.getString("id");
                    username = objUser.getString("username");
                    nama = objUser.getString("nama");
                    tipe = objUser.getString("tipe");
                    API_KEY = objUser.getString("API_KEY");
                    secure_key = objUser.getString("secure_key");
                    waktu = objUser.getString("waktu");
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
                if(status_cek.equals("MATCH")){
                    tvNotif.setText(message);
                    editUsername.setText("");
                    editPass.setText("");
                    editor.putString("ID",id);
                    editor.putString("USERNAME",username);
                    editor.putString("NAMA",nama);
                    editor.putString("TIPE",tipe);
                    editor.putString("API_KEY",API_KEY);
                    editor.putString("SECURE_KEY",secure_key);
                    editor.putString("WAKTU",waktu);
                    editor.commit();
                    Intent i = new Intent(Login.this, RootActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    tvNotif.setText(message);
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
