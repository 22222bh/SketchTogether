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

        // loadSticker()에서 스티커 이미지를 getDrawable()로 가져오면 OOM 에러가 생겨 Glide로 우회
        Glide.with(this).load(R.drawable.icon_heart).into(imageView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSticker();
            }
        });
    }

    // 스티커뷰에서 스티커 추가
    protected void loadSticker(){
        Drawable drawable = imageView.getDrawable();
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        stickerView.addSticker(drawableSticker);
    }

}