package com.example.bhurivatmontri.trophel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.Camera;
import com.example.bhurivatmontri.trophel.DetailAttraction;
import com.example.bhurivatmontri.trophel.Home;
import com.example.bhurivatmontri.trophel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter2 extends RecyclerView.Adapter<GridAdapter2.ViewHolder> {

    List<List<EndangeredItem2>> mItems_outer;
    List<EndangeredItem2> mItems_inner;
    public Context mContext;
    public Fragment replaceFragment;
    public Activity activity;
    int region;
    //String[] conv = {"northern","central","northeastern","western","southern","eastern"};

    public GridAdapter2(Activity activity,String rg,ArrayList<ArrayList<String>> nameInput,ArrayList<ArrayList<String>> uri_ImgInput
            ,ArrayList<ArrayList<ArrayList<Integer>>> starInput,ArrayList<ArrayList<Integer>> trophyInput) {
        super();
        this.activity = activity;
        region = Integer.parseInt(rg);
        mItems_outer = new ArrayList<List<EndangeredItem2>>();

        /*String[][] name = {{"NorthAAA","NorthBBB","NorthCCC","NorthDDD","NorthEEE"},{"CentralAAA","CentralBBB"},{},{},{"SouthAAA"},{}};
        int[][][] star = {{{2,2,2,2,2},{2,2,2,0,0},{2,2,2,2,0},{2,2,2,1,0},{2,2,2,1,1}},{{2,2,2,0,0},{2,1,1,1,1}},{},{},{{2,2,2,1,0}},{}};
        int[][] trophy = {{2,2,2,1,1},{2,1},{},{},{1},{}};*/

        ArrayList<ArrayList<String>> name = nameInput;
        ArrayList<ArrayList<String>> uri_img = uri_ImgInput;
        ArrayList<ArrayList<ArrayList<Integer>>> star = starInput;
        ArrayList<ArrayList<Integer>> trophy = trophyInput;
        //ArrayList<ArrayList<String>> uri_img = new ArrayList<>();

        for (int i = 0 ; i < 6 ; i++){
            mItems_inner = new ArrayList<EndangeredItem2>();
            for (int j = 0; j < name.get(i).size(); j++) {
                EndangeredItem2 nama = new EndangeredItem2();
                nama.setName(name.get(i).get(j));
                nama.setThumbnail(uri_img.get(i).get(j));
                nama.setStar(star.get(i).get(j).get(0),star.get(i).get(j).get(1),star.get(i).get(j).get(2),star.get(i).get(j).get(3),star.get(i).get(j).get(4));
                nama.setTrophy(trophy.get(i).get(j));
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
        Picasso.with(mContext)
                .load(nature.getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(viewHolder.thumbnailTrophy);
        //viewHolder.imgTrophy.setImageResource(nature.getThumbnail());
        switch (nature.getTrophy()){
            case 0:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_green_png);
                break;
            case 1:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_yellow_png);
                break;
            case 2:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_orange_png);
                break;
            case 3:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_red_png);
                break;
            case 4:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_blue_png);
                break;
            case 5:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_purple_png);
                break;
            case 6:
                viewHolder.imgTrophy.setImageResource(R.drawable.trophy_unsuccess_png);
                break;

        }

        //LinearLayout layout_star = (LinearLayout)this.activity.findViewById(R.id.bottom_layout_trophy_in);
        String starNameImg = "trophy_star";
        ImageView[] imageStar = new ImageView[5];
        for (int j = 0; j < nature.getNumStarAtt() ; j++) {
            viewHolder.imgStar[j].setVisibility(View.VISIBLE);
            if(j < nature.getNumStarAttSucc()) {
                viewHolder.imgStar[j].setImageResource(R.drawable.star_success);
            }else{
            }
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
        public ImageView thumbnailTrophy;
        public ImageView[] imgStar = new ImageView[5];
        public ViewHolder(View itemView) {
            super(itemView);
            imgTrophy = (ImageView) itemView.findViewById(R.id.img_trophy);
            nameTrophy = (TextView) itemView.findViewById(R.id.name_trophy);
            thumbnailTrophy = (ImageView) itemView.findViewById(R.id.img_att_trophy);
            imgStar[0] = (ImageView) itemView.findViewById(R.id.trophy_star0);
            imgStar[1] = (ImageView) itemView.findViewById(R.id.trophy_star1);
            imgStar[2] = (ImageView) itemView.findViewById(R.id.trophy_star2);
            imgStar[3] = (ImageView) itemView.findViewById(R.id.trophy_star3);
            imgStar[4] = (ImageView) itemView.findViewById(R.id.trophy_star4);
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
