package com.keamanan_rumah.sistemkeamananrumah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RootActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String JSON_data;
    String status_cek,message,message_severity;
    public static String id, username, nama, tipe, API_KEY, secure_key, waktu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent i = getIntent();
        JSON_data = i.getStringExtra("JSON_data");
        if(JSON_data!=null){
            try {
                JSONObject jsonObj = new JSONObject(JSON_data);
                JSONObject response = jsonObj.getJSONObject("response");
                status_cek = response.getString("status_cek");
                message = response.getString("message");
                message_severity = response.getString("message_severity");
                //--
                JSONArray data_user = response.getJSONArray("data_user");
                JSONObject objUser = data_user.getJSONObject(0);
                id = objUser.getString("id");
                username = objUser.getString("username");
                nama = objUser.getString("nama");
                tipe = objUser.getString("tipe");
                API_KEY = objUser.getString("API_KEY");
                secure_key = objUser.getString("secure_key");
                waktu = objUser.getString("waktu");
                //--
                Toast.makeText(
                        getApplicationContext(),
                        "ID : " + id + "\n" +
                                "Username : " + username + "\n" +
                                "Nama : " + nama + "\n" +
                                "Tipe : " + tipe + "\n" +
                                "API_KEY : " + API_KEY + "\n" +
                                "Sec : " + secure_key + "\n"+
                                "Waktu : " + waktu + "\n",
                        Toast.LENGTH_LONG
                ).show();
            } catch (final JSONException e) {
                Log.e("Error Bos : ", e.getMessage());
            }
        }
        else{
            Log.e("Error Bos : ","JSON NULL");
        }
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.FrameRoot, new FragmentDashboard());
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.root, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        }
//        else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
