package com.keamanan_rumah.sistemkeamananrumah;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class FragmentProfil extends Fragment {

    String JSON_data;
    boolean loaddata;
    String user_total;
    String user_block;
    String database_today;
    String database_total;

    EditText etUsername,etNama,etAlamat,etTipeAccount,etTanggalRegistrasi,etApiKey,etSecureKey;
    Button btnEdit,btnSimpan;

    String username, nama, alamat, tipe, tanggal, API_KEY, secure_key;

    public FragmentProfil() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterProfil = inflater.inflate(R.layout.fragment_profil, container, false);
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
        new AsyncProfil().execute();
    }

    private class AsyncProfil extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            String url = RootActivity.api_profil;
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
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
            if(loaddata){
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
                        }else
                        if(btnEdit.getText().equals("Kembali")){
                            btnEdit.setText("Edit");
                            etNama.setEnabled(false);
                            etAlamat.setEnabled(false);
                            btnSimpan.setVisibility(View.GONE);
                        }
                    }
                });
            }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }


}
