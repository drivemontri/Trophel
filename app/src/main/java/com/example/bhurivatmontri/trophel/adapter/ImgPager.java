package com.example.bhurivatmontri.trophel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bhurivat Montri on 4/18/2018.
 */

public class ImgPager extends PagerAdapter {

    private Context context;
    private ArrayList<String> imgUrls = new ArrayList<>();
    //private String[] imgUrls;

    public ImgPager(Context context,ArrayList<String> imgUrls){
        this.context = context;
        this.imgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return this.imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        ImageView imageView = new ImageView(context);
        Picasso.with(context)
                .load(imgUrls.get(position))
                .fit()
                .centerCrop()
                .into(imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}
