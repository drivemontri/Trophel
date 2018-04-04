package com.example.bhurivatmontri.trophel.adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.Camera;
import com.example.bhurivatmontri.trophel.DetailAttraction;
import com.example.bhurivatmontri.trophel.Home;
import com.example.bhurivatmontri.trophel.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter2 extends RecyclerView.Adapter<GridAdapter2.ViewHolder> {

    List<List<EndangeredItem2>> mItems_outer;
    List<EndangeredItem2> mItems_inner;
    public Context mContext;
    public Fragment replaceFragment;
    int region;
    //String[] conv = {"northern","central","northeastern","western","southern","eastern"};

    public GridAdapter2(String rg) {
        super();

        region = Integer.parseInt(rg);
        mItems_outer = new ArrayList<List<EndangeredItem2>>();

        String[][] name = {{"NorthAAA","NorthBBB","NorthCCC","NorthDDD","NorthEEE"},{"CentralAAA","CentralBBB"},{},{},{"SouthAAA"},{}};
        int[][][] star = {{{2,2,2,2,2},{2,2,2,0,0},{2,2,2,2,0},{2,2,2,0,0},{2,2,2,0,0}},{{2,2,2,0,0},{2,2,2,2,2}},{},{},{{2,2,2,2,0}},{}};
        int[][] trophy = {{2,2,2,1,1},{2,1},{},{},{1},{}};

        for (int i = 0 ; i < 6 ; i++){
            mItems_inner = new ArrayList<EndangeredItem2>();
            for (int j = 0; j < name[i].length; j++) {
                EndangeredItem2 nama = new EndangeredItem2();
                nama.setName(name[i][j]);
                nama.setStar(star[i][j][0],star[i][j][1],star[i][j][2],star[i][j][3],star[i][j][4]);
                nama.setTrophy(trophy[i][j]);
                nama.setAttractionID("++++");
                mItems_inner.add(nama);
                Log.d("sss++","i="+i+",j="+j);
            }
            mItems_outer.add(mItems_inner);
        }

        Log.d("sss","xxx");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_trophy, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final EndangeredItem2 nature = mItems_outer.get(region-1).get(i);
        Log.d("sss","xxx--"+nature.getName());
        viewHolder.nameTrophy.setText(nature.getName());
        //viewHolder.imgTrophy.setImageResource(nature.getThumbnail());
        switch (nature.getTrophy()){
            case 1:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophel_empty);
                break;
            case 2:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophel_central);
                break;
        }

        viewHolder.imgTrophy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(v.getContext(), "-->" + nature.getName() + "<--", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("sss**","xxx**out" + mItems_outer.size());
        Log.d("sss**","xxx**" + mItems_outer.get(region-1).size()); return mItems_outer.get(region-1).size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgTrophy;
        public TextView nameTrophy;

        public ViewHolder(View itemView) {
            super(itemView);
            imgTrophy = (ImageView) itemView.findViewById(R.id.img_trophy);
            nameTrophy = (TextView) itemView.findViewById(R.id.name_trophy);
            //nameTrophy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(itemView.getContext(), "default", Toast.LENGTH_SHORT).show();
        }
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }
}
