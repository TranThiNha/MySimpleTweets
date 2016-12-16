package com.codepath.apps.mysimpletweets.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activity.ProfileActivity;

/**
 * Created by MyPC on 12/13/2016.
 */
public class ShowImageDialog {

    ImageView mImage;
    String mImageUrl;
    Dialog dialog;

    public ShowImageDialog(String ImageUrl) {
        mImageUrl = ImageUrl;
    }

    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_iamge);
        mImage = (ImageView) dialog.findViewById(R.id.Image);
        Glide.with(mImage.getContext())
                .load(mImageUrl)
                .into(mImage);
        dialog.show();
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialog.dismiss();
                }
                if (keyCode == KeyEvent.KEYCODE_ESCAPE){
                    dialog.dismiss();
                }

                return false;
            }
        });


    }
}
