package com.example.bhurivatmontri.trophel.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.Camera;
import com.example.bhurivatmontri.trophel.DetailAttraction;
import com.example.bhurivatmontri.trophel.DetailAttraction2;
import com.example.bhurivatmontri.trophel.Home;
import com.example.bhurivatmontri.trophel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    ArrayList<EndangeredItem> mItems;
    public Context mContext;
    public Fragment replaceFragment;

    public GridAdapter(Context context,ArrayList<EndangeredItem> listAttractions) {
        super();
        this.mContext = context;
        this.mItems = listAttractions;


        /*mItems = new ArrayList<EndangeredItem>();
        EndangeredItem nama = new EndangeredItem();
        nama.setName("AngKhang");
        nama.setThumbnail(R.drawable.att_angkhang);
        mItems.add(nama);

        nama = new EndangeredItem();
        nama.setName("DoiLuangchiangdao");
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
        mItems.add(nama);*/

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_attraction, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final EndangeredItem nature = mItems.get(position);
        viewHolder.tvspecies.setText(nature.getName());
        Picasso.with(mContext)
                .load(mItems.get(position).getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(viewHolder.getImgThumbnail());
        viewHolder.getCard_attrs().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetailAttraction2.class);
                intent.putExtra("keyOfAttraction",nature.getKeyName());
                v.getContext().startActivity(intent);
            }
        });

        /*viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
        viewHolder.tvspecies.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(), "-->" + nature.getName() + "<--", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),DetailAttraction.class);
                intent.putExtra("nameOfAttraction",nature.getName());
                v.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView tvspecies;
        public RelativeLayout card_attrs;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                }
            });
            tvspecies = (TextView) itemView.findViewById(R.id.name_Attraction);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_Attraction);
            card_attrs = (RelativeLayout) itemView.findViewById(R.id.top_layout_Attraction);
        }
        public TextView getTvspecies(){return tvspecies ;}
        public ImageView getImgThumbnail() {return imgThumbnail ;}
        public RelativeLayout getCard_attrs() {return card_attrs;}
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }
}