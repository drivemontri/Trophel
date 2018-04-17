package com.example.bhurivatmontri.trophel.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.DetailAttraction;
import com.example.bhurivatmontri.trophel.R;
import com.example.bhurivatmontri.trophel.fragment.FriendProfile;
import com.example.bhurivatmontri.trophel.fragment.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";
    private Context mContext;
    ArrayList<Friend> listFriend = new ArrayList<>();
    public CustomAdapter(Context context , ArrayList<Friend> listFriend){
        this.mContext = context;
        this.listFriend = listFriend;
    }

    //private String[] mDataSet,mDataSet2;
    //private int[] mDataSet3;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView,textView2,count_Star;
        private final ImageView icon;
        public ViewHolder(View v){
            super(v);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");

                    /*Intent intent = new Intent(v.getContext(),DetailAttraction.class);
                    intent.putExtra("nameOfListFriend",);
                    v.getContext().startActivity(intent);*/
                }
            });
            textView = (TextView) v.findViewById(R.id.name_Friend);
            textView2 = (TextView) v.findViewById(R.id.detail_Friend);
            icon = (ImageView) v.findViewById(R.id.icon_Friend);
            count_Star = (TextView) v.findViewById(R.id.count_Star_Friend) ;
        }
        public TextView getTextView() {
            return textView;
        }
        public TextView getTextView2() {
            return textView2;
        }
        public ImageView getImageView() {
            return icon;
        }
        public TextView gettextView3() {return count_Star;}
    }

    /*public CustomAdapter(String[] dataSet,String[] dataSet2,int[] dataSet3){
        this.mDataSet = dataSet;
        this.mDataSet2 = dataSet2;
        this.mDataSet3 = dataSet3;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_friend,viewGroup,false);
        return new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        /*viewHolder.getTextView().setText(mDataSet[position]);
        viewHolder.getTextView2().setText(mDataSet2[position]);
        viewHolder.getImageView().setImageResource(mDataSet3[position]);*/
        viewHolder.getTextView().setText(listFriend.get(position).getName());
        viewHolder.getTextView2().setText(listFriend.get(position).getDetail());
        viewHolder.gettextView3().setText(listFriend.get(position).getCountStar());
        //viewHolder.getImageView().setImageResource(listFriend.get(position).getIcon());
        Log.d("onDataChange3",""+listFriend.get(position).getUriImg());
        Picasso.with(mContext)
                .load(listFriend.get(position).getUriImg())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(viewHolder.getImageView());
        viewHolder.getImageView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(), "-->" + listFriend.get(position).getName() + "<--", Toast.LENGTH_SHORT).show();
                //FragmentManager manager = ((Activity)mContext).getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("FriendID",listFriend.get(position).getFriendID());

                FriendProfile friendProfile = new FriendProfile();
                friendProfile.setArguments(bundle);

                android.support.v4.app.FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                manager.beginTransaction()
                        .add(R.id.listFriend,friendProfile,friendProfile.getTag())
                        .addToBackStack(null)
                        .commit();

                /*Intent intent = new Intent(v.getContext(), Profile.class);
                intent.putExtra("nameOfListFriend",listFriend.get(position).getName());
                intent.putExtra("nameOfListFriend",listFriend.get(position).getFriendID());
                v.getContext().startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {return listFriend.size();}

    public void setFilter(ArrayList<Friend> newListFriend){
        listFriend = new ArrayList<>();
        listFriend.addAll(newListFriend);
        notifyDataSetChanged();
    }
}
