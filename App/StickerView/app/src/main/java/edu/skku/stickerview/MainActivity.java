package edu.skku.stickerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    StickerView stickerView;
    ImageView imageView;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stickerView = findViewById(R.id.stickerView);
        addButton = findViewById(R.id.addButton);
        imageView = findViewById(R.id.tempImageView);
        Glide.with(this).load(R.drawable.icon_heart).into(imageView);

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_white_18dp), BitmapStickerIcon.LEFT_TOP);
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_flip_white_18dp), BitmapStickerIcon.RIGHT_TOP);
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_scale_white_18dp), BitmapStickerIcon.RIGHT_BOTTOM);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        flipIcon.setIconEvent(new FlipVerticallyEvent());
        zoomIcon.setIconEvent(new ZoomIconEvent());
        List<BitmapStickerIcon> iconList = new ArrayList<BitmapStickerIcon>();
        iconList.add(deleteIcon);
        iconList.add(flipIcon);
        iconList.add(zoomIcon);
        stickerView.setIcons(iconList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSticker();
            }
        });

    }

    protected void loadSticker(){
        Drawable drawable = imageView.getDrawable();
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        stickerView.addSticker(drawableSticker);
    }

}

