package com.codepath.apps.mysimpletweets.models;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;

/**
 * Created by MyPC on 11/17/2016.
 */
public class Destination {
    ImageView ivImage;
    TextView tvDes;
    TextView tvAddress;

    public Destination(){
        tvDes.setText("hihi");
        tvAddress.setText("day nek");
        ivImage.setImageResource(R.drawable.ic_star);
    }
    public TextView getTvDes() {
        return tvDes;
    }

    public TextView getTvAddress() {
        return tvAddress;
    }


}
