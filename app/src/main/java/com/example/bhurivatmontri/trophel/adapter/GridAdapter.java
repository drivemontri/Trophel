package com.example.bhurivatmontri.trophel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bhurivatmontri.trophel.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    List<EndangeredItem> mItems;

    public GridAdapter() {
        super();
        mItems = new ArrayList<EndangeredItem>();
        EndangeredItem nama = new EndangeredItem();
        nama.setName("AngKhang");
        nama.setThumbnail(R.drawable.att_angkhang);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("DoilLuangchiangdao");
        nama.setThumbnail(R.drawable.att_doiluangchiangdao);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("DoiPhaHomPok");
        nama.setThumbnail(R.drawable.att_doiphahompok);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("Kewmaepan");
        nama.setThumbnail(R.drawable.att_kewmaepan);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("KunChangKian");
        nama.setThumbnail(R.drawable.att_kunchangkian);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("Monjong");
        nama.setThumbnail(R.drawable.att_monjong);
        mItems.add(nama);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_attraction, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        EndangeredItem nature = mItems.get(i);
        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView tvspecies;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_Attraction);
            tvspecies = (TextView) itemView.findViewById(R.id.name_Attraction);
        }
    }
}