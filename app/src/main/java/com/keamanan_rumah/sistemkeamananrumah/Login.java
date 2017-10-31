package com.keamanan_rumah.sistemkeamananrumah;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button btnDaftar,btnLogin;
    EditText editUsername,editPass;
    TextView tvNotif;
    String u,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
                if(u.equals("") || p.equals("")){
                    tvNotif.setText("Masukkan Username dan Password");
                }else{
                    //heheheh welcome to restful
                }
            }
        });
    }

}
