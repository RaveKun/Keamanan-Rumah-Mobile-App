package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    String status_cek,message;
    
    Boolean loaddata;

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
        u = editUsername.getText().toString();
        p = editPass.getText().toString();

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
            ServiceHandler sh = new ServiceHandler();
            String url = api_site_url.concat(api_login);
            String JSON_data = sh.makeServiceCall(url, ServiceHandler.POST, data_login);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    status_cek = response.getString("status_cek");
                    message = response.getString("message");
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
                }else{
                    tvNotif.setText(message);
                }
            }else{
                tvNotif.setText("Error !");
            }
        }
    }

}
