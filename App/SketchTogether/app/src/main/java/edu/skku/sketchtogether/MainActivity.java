package edu.skku.sketchtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssomai.android.scalablelayout.ScalableLayout;

public class MainActivity extends AppCompatActivity {

    Context context;
    StickerView stickerView;
    FloatingActionButton suggestButton;
    FloatingActionButton deleteButton;
    LinearLayout imageViewLinearLayout;
    ImageView neighborImageView1;
    ImageView neighborImageView2;
    ImageView neighborImageView3;
    ImageView neighborImageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();

        stickerView = findViewById(R.id.stickerView);
        suggestButton = findViewById(R.id.suggestButton);
        deleteButton = findViewById(R.id.deleteButton);
        imageViewLinearLayout = findViewById(R.id.imageViewLinearLayout);
        neighborImageView1 = findViewById(R.id.neighborImageView1);
        neighborImageView2 = findViewById(R.id.neighborImageView2);
        neighborImageView3 = findViewById(R.id.neighborImageView3);
        neighborImageView4 = findViewById(R.id.neighborImageView4);


        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loadSticker()에서 스티커 이미지를 getDrawable()로 가져오면 OOM 에러가 생겨 Glide로 우회
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView1);
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView2);
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView3);
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView4);
                imageViewLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        neighborImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSticker(neighborImageView1);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        neighborImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSticker(neighborImageView2);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        neighborImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSticker(neighborImageView3);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        neighborImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSticker(neighborImageView4);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    // 스티커뷰에서 스티커 추가
    protected void loadSticker(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        stickerView.addSticker(drawableSticker);
    }

}