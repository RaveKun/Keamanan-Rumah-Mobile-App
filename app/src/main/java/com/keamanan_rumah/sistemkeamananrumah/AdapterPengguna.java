package com.keamanan_rumah.sistemkeamananrumah;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPengguna extends ArrayAdapter<Pengguna>{

    Context context;
    int layoutResourceId;
    Pengguna data[] = null;

    public AdapterPengguna(Context context, int layoutResourceId, Pengguna[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HolderPengguna holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HolderPengguna();
            holder.ivUser = (ImageView)row.findViewById(R.id.ivUser);
            holder.tvNama = (TextView)row.findViewById(R.id.tvNama);
            holder.tvUsername = (TextView)row.findViewById(R.id.tvUsername);
            holder.tvStatus = (TextView)row.findViewById(R.id.tvStatus);
            row.setTag(holder);
        }else{
            holder = (HolderPengguna)row.getTag();
        }

        Pengguna pengguna = data[position];
        holder.tvNama.setText(pengguna.nama);
        holder.tvUsername.setText(pengguna.username);
        if(pengguna.status.equals("1")){
            holder.tvStatus.setText("Active");
            holder.ivUser.setImageResource(R.mipmap.user_active);
        }else
        if(pengguna.status.equals("2")){
            holder.tvStatus.setText("Blocked");
            holder.ivUser.setImageResource(R.mipmap.user_blocked);
        }else
        if(pengguna.status.equals("3")){
            holder.tvStatus.setText("On proccess request open");
            holder.ivUser.setImageResource(R.mipmap.user_blocked);
        }


        return row;
    }

    static class HolderPengguna{
        ImageView ivUser;
        TextView tvNama;
        TextView tvUsername;
        TextView tvStatus;
    }
}
