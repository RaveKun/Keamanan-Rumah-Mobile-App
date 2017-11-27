package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Daftar extends AppCompatActivity {

    List<NameValuePair> data_daftar = new ArrayList<NameValuePair>(7);

    Button btnDaftar,btnLogin;
    EditText editUsername,editPass,editNama,editAlamat;
    TextView tvNotif;
    ProgressDialog pDialog;

    String u,p,n,a;
    String api_daftar;
    String TAG;
    String status_cek,message,message_severity;

    Boolean loaddata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);
        TAG = getResources().getString(R.string.TAG);
        api_daftar = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_daftar));
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnDaftar=(Button) findViewById(R.id.btnDaftar);
        editUsername = (EditText)findViewById(R.id.editUsername);
        editPass = (EditText)findViewById(R.id.editPass);
        editNama = (EditText)findViewById(R.id.editNama);
        editAlamat = (EditText)findViewById(R.id.editAlamat);
        tvNotif = (TextView)findViewById(R.id.tvNotif);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Daftar.this,Login.class);
                finish();
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = editUsername.getText().toString();
                p = editPass.getText().toString();
                n = editNama.getText().toString();
                a = editAlamat.getText().toString();
                if(u.length() > 6 && p.length() > 6 && !n.equals("") && !a.equals("")){
                    data_daftar.add(new BasicNameValuePair("username", u));
                    data_daftar.add(new BasicNameValuePair("password", p));
                    data_daftar.add(new BasicNameValuePair("nama", n));
                    data_daftar.add(new BasicNameValuePair("alamat", a));
                    new AsyncDaftar().execute();
                }else{
                    tvNotif.setText("Pastikan semua field telah terisi. Untuk username dan password minimal 7 digit.");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }
            }
        });
    }
    private class AsyncDaftar extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Daftar.this);
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            String url = api_daftar;
            String JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_daftar);
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
                if(status_cek.equals("MATCH")){
                    tvNotif.setText(message);
                    editUsername.setText("");
                    editPass.setText("");
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
