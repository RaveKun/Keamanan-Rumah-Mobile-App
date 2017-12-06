package com.keamanan_rumah.sistemkeamananrumah;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;


public class FragmentDownloadReport extends Fragment {

    ProgressDialog pDialog;

    LinearLayout llNotif, llNetworkAvailable;
    Spinner spTanggalAwal;
    Spinner spTanggalAkhir;
    Spinner spBulanAwal;
    Spinner spBulanAkhir;
    Spinner spTahunAwal;
    Spinner spTahunAkhir;
    Button btnCekLaporan, btnDownload;
    TextView tvNotif;
    WebView wvDownload;


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean loaddata;

    String JSON_data;
    String url;
    String str_tgl_awal;
    String str_tgl_akhir;
    String jumlah_record;
    String nama_laporan;

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
    public static String api_cek_download_report;
    public static String api_download_report;

    int ln;

    public FragmentDownloadReport() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflaterDownloadReport = inflater.inflate(R.layout.fragment_download_report, container, false);
        llNetworkAvailable = (LinearLayout) inflaterDownloadReport.findViewById(R.id.llNetworkAvailable);
        llNotif = (LinearLayout) inflaterDownloadReport.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterDownloadReport.findViewById(R.id.tvNotif);
        spTanggalAwal = (Spinner) inflaterDownloadReport.findViewById(R.id.spTanggalAwal);
        spTanggalAkhir = (Spinner) inflaterDownloadReport.findViewById(R.id.spTanggalAkhir);
        spBulanAwal = (Spinner) inflaterDownloadReport.findViewById(R.id.spBulanAwal);
        spBulanAkhir = (Spinner) inflaterDownloadReport.findViewById(R.id.spBulanAkhir);
        spTahunAwal = (Spinner) inflaterDownloadReport.findViewById(R.id.spTahunAwal);
        spTahunAkhir = (Spinner) inflaterDownloadReport.findViewById(R.id.spTahunAkhir);
        btnCekLaporan = (Button) inflaterDownloadReport.findViewById(R.id.btnCekLaporan);
        btnDownload = (Button) inflaterDownloadReport.findViewById(R.id.btnDownloadLaporan);
        wvDownload = (WebView) inflaterDownloadReport.findViewById(R.id.wvDownload);
        return inflaterDownloadReport;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getActivity().getSharedPreferences("KEAMANAN_RUMAH", 0);
        editor = pref.edit();

        pref_id = pref.getString("ID", null);
        pref_username = pref.getString("USERNAME", null);
        pref_nama = pref.getString("NAMA", null);
        pref_tipe = pref.getString("TIPE", null);
        pref_api_key = pref.getString("API_KEY", null);
        pref_secure_key = pref.getString("SECURE_KEY", null);
        pref_waktu = pref.getString("WAKTU", null);

        api_daftar = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_daftar));
        api_dashboard = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_dashboard));
        api_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_profil)).concat(pref_id);
        api_update_profil = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_profil)).concat(pref_id);
        api_update_password = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_update_password)).concat(pref_id);
        api_load_all_parent = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_load_all_parent));
        api_cek_download_report = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_cek_download_report ));
        api_download_report = getResources().getString(R.string.api_site_url).concat(getResources().getString(R.string.api_download_report ));


        llNotif.setVisibility(View.GONE);
        llNetworkAvailable.setVisibility(View.VISIBLE);
        btnDownload.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> tanggalAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.tanggal, android.R.layout.simple_spinner_item);
        tanggalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTanggalAwal.setAdapter(tanggalAdapter);
        spTanggalAkhir.setAdapter(tanggalAdapter);
        ArrayAdapter<CharSequence> bulanAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.bulan, android.R.layout.simple_spinner_item);
        bulanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBulanAwal.setAdapter(bulanAdapter);
        spBulanAkhir.setAdapter(bulanAdapter);
        ArrayAdapter<CharSequence> tahunAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.tahun,android.R.layout.simple_spinner_item);
        tahunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTahunAwal.setAdapter(tahunAdapter);
        spTahunAkhir.setAdapter(tahunAdapter);

        btnCekLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_tgl_awal = spTahunAwal.getSelectedItem().toString().concat("-")
                        .concat(spBulanAwal.getSelectedItem().toString()).concat("-")
                        .concat(spTanggalAwal.getSelectedItem().toString());
                str_tgl_akhir = spTahunAkhir.getSelectedItem().toString().concat("-")
                        .concat(spBulanAkhir.getSelectedItem().toString()).concat("-")
                        .concat(spTanggalAkhir.getSelectedItem().toString());
                int awal = Integer.parseInt(spTahunAwal.getSelectedItem().toString()
                        .concat(spBulanAwal.getSelectedItem().toString())
                        .concat(spTanggalAwal.getSelectedItem().toString()));
                int akhir = Integer.parseInt(spTahunAkhir.getSelectedItem().toString()
                        .concat(spBulanAkhir.getSelectedItem().toString())
                        .concat(spTanggalAkhir.getSelectedItem().toString()));
                nama_laporan = String.valueOf(awal).concat(String.valueOf(akhir));
                if(akhir < awal){
                    llNotif.setVisibility(View.VISIBLE);
                    tvNotif.setText("Pastikan tanggal awal lebih kecil/sama dengan tanggal akhir. Terima kasih");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else{
                    new AsyncCekLaporan().execute();
                }

            }
        });

//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            FragmentDownloadReport.AsyncDashboard async = new FragmentDownloadReport.AsyncDashboard();
//                            async.execute();
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    private class AsyncCekLaporan extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            url = api_cek_download_report.concat(str_tgl_awal).concat("/").concat(str_tgl_akhir).concat("/").concat(pref_api_key).concat("/");
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST);
            Log.d("url",url);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONArray response = jsonObj.getJSONArray("response");
                    ln = response.length();
                    if(ln > 0){
                        JSONObject obj_sensor = response.getJSONObject(0);
                        jumlah_record = obj_sensor.getString("jumlah_record");
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
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(loaddata){
                if(ln > 0){
                    if(Integer.parseInt(jumlah_record) > 0){
                        llNotif.setVisibility(View.VISIBLE);
                        tvNotif.setText("Laporan tersedia dan siap didownload.");
                        tvNotif.setBackgroundColor(Color.parseColor("#FFCA28"));
                        btnDownload.setVisibility(View.VISIBLE);
                        btnDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("Konfirmasi");
                                alertDialog.setMessage("Apakah Anda yakin akan melakukan download laporan?");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        btnDownload.setVisibility(View.GONE);
                                        wvDownload.getSettings().setJavaScriptEnabled(true);
                                        wvDownload.getSettings().setDomStorageEnabled(true);
                                        wvDownload.loadUrl(api_download_report.concat(str_tgl_awal).concat("/").concat(str_tgl_akhir).concat("/").concat(pref_api_key).concat("/"));
                                        wvDownload.setDownloadListener(new DownloadListener() {
                                            @Override
                                            public void onDownloadStart(String url, String userAgent,
                                                                        String contentDisposition, String mimetype,
                                                                        long contentLength) {
                                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                                request.allowScanningByMediaScanner();
                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nama_laporan+".pdf");
                                                DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                                dm.enqueue(request);
                                                Toast.makeText(getActivity(), "Download laporan :" + nama_laporan + ".pdf" ,Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }else
                    if(Integer.parseInt(jumlah_record)==0){
                        llNotif.setVisibility(View.VISIBLE);
                        tvNotif.setText("Tidak ada laporan tersimpan untuk periode tersebut.");
                        tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                        btnDownload.setVisibility(View.GONE);
                    }
                }else{
                    llNotif.setVisibility(View.VISIBLE);
                    tvNotif.setText("Tidak ada laporan tersimpan untuk periode tersebut.");
                    tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                    btnDownload.setVisibility(View.GONE);
                }

            }else{
                llNotif.setVisibility(View.VISIBLE);
                tvNotif.setText("Error, terjadi kendala saat terhubung ke server.");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                btnDownload.setVisibility(View.GONE);
            }
        }
    }

}