package com.codepath.apps.mysimpletweets.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Chat;
import com.codepath.apps.mysimpletweets.models.Profile;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by MyPC on 12/15/2016.
 */
public class ChangeProfileActivity extends AppCompatActivity {

    public static String KEY_AVATARURL = "avatarUrl";
    public static String KEY_COVERUEL = "coverUrl";
    public static String KEY_NAME = "name";
    public static String KEY_DES = "des";
    public static String KEY_BITMAP_AVATAR = "bitmapavatar";
    public static String KEY_BITMAP_COVER = "bitmapcover";
    private static int RESULT_LOAD_IMAGE = 1;
    private String mAvartarUrl="", mCoverUrl="";
    private ImageView ivAvatar;
    private ImageView ivChangeAvatar;
    private ImageView ivCover;
    private ImageView ivChangeCover;
    private EditText edtChangeName;
    private EditText edtChangeDes;
    private ImageView ivBack;
    private TextView tvSave;
    private String pathImage="";
    private static int avatarOrCover =0; //0: not, 1: avatar, 2: cover
    private Bitmap mBitmapAvatar = null, mBitmapCover = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_profile);
    //cast data
        ivAvatar = (ImageView)findViewById(R.id.ivAvatar);
        ivCover = (ImageView) findViewById(R.id.ivCoverImage);
        ivChangeAvatar = (ImageView) findViewById(R.id.ivChangeAvatar);
        ivChangeCover = (ImageView) findViewById(R.id.ivChangeCover);
        edtChangeName = (EditText) findViewById(R.id.edtChangeName);
        edtChangeDes = (EditText) findViewById(R.id.edtDescribe);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvSave = (TextView) findViewById(R.id.tvSave);

        Intent intent = getIntent();
        mAvartarUrl = intent.getStringExtra(ProfileActivity.KEY_AVARTAR);
        mCoverUrl = intent.getStringExtra(ProfileActivity.KEY_COVER);

        Toast.makeText(getApplicationContext(),mAvartarUrl,Toast.LENGTH_SHORT).show();
        ivChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                avatarOrCover = 1;
            }
        });

        ivChangeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                avatarOrCover = 2;
            }
        });
        if (!(mAvartarUrl=="")){
            Glide.with(ivAvatar.getContext())
                    .load(mAvartarUrl)
                    .into(ivAvatar);
        }
        if (!(mCoverUrl=="")){
            Glide.with(ivCover.getContext())
                    .load(mCoverUrl)
                    .into(ivCover);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pathImage = cursor.getString(columnIndex);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                if (avatarOrCover == 2){
                    mBitmapCover = bmp;
                    mCoverUrl = pathImage;
                    Toast.makeText(getApplicationContext(),String.valueOf(pathImage),Toast.LENGTH_SHORT).show();
                    ivCover.setImageBitmap(bmp);
                }
                else if(avatarOrCover==1){
                    mBitmapAvatar = bmp;
                    mAvartarUrl = pathImage;
                    ivAvatar.setImageBitmap(bmp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            cursor.close();
            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra(KEY_AVATARURL,mAvartarUrl);
                    intent.putExtra(KEY_COVERUEL,mCoverUrl);
                    intent.putExtra(KEY_NAME,edtChangeName.getText().toString());
                    intent.putExtra(KEY_DES, edtChangeDes.getText().toString());
                    intent.putExtra(KEY_BITMAP_AVATAR,mBitmapAvatar);
                    intent.putExtra(KEY_BITMAP_COVER, mBitmapCover);
                    startActivity(intent);
                }
            });
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
