package edu.skku.sketchdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.List;

import edu.skku.sketchdemo.ml.EfficientnetLite4Int82;

public class MainActivity extends AppCompatActivity {
    private final int GET_IMAGE_FOR_GALLERYVIEW = 201;
    private ImageView galleryImage;
    private ImageView suggestImage1;
    private ImageView suggestImage2;
    private ImageView suggestImage3;
    private ImageView suggestImage4;
    private Button suggestButton;
    private Bitmap galleryImageBmp;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        galleryImage = findViewById(R.id.galleryImageView);
        suggestImage1 = findViewById(R.id.suggestedImageView1);
        suggestImage2 = findViewById(R.id.suggestedImageView2);
        suggestImage3 = findViewById(R.id.suggestedImageView3);
        suggestImage4 = findViewById(R.id.suggestedImageView4);
        suggestButton = findViewById(R.id.suggestButton);
        suggestButton.setVisibility(View.INVISIBLE);

        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_IMAGE_FOR_GALLERYVIEW);
            }
        });

        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable galleryImageDrawable = galleryImage.getDrawable();
                galleryImageBmp = ((BitmapDrawable)galleryImageDrawable).getBitmap();
                Bitmap galleryImageBmpResized = Bitmap.createScaledBitmap(galleryImageBmp, 300, 300, false);
                try {
                    EfficientnetLite4Int82 model = EfficientnetLite4Int82.newInstance(MainActivity.this);
                    TensorImage image = TensorImage.fromBitmap(galleryImageBmpResized);
                    EfficientnetLite4Int82.Outputs outputs = model.process(image);
                    List<Category> probability = outputs.getProbabilityAsCategoryList();

                    double maxScore = 0;
                    int maxScoreIndex = 0;
                    for (int i = 0; i < 1000; i++) {
                        if (probability.get(i).getScore() > maxScore) {
                            maxScore = probability.get(i).getScore();
                            maxScoreIndex = i;
                        }
                    }
                    suggestButton.setText(probability.get(maxScoreIndex).getLabel());

                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

                suggestImage1.setImageResource(R.drawable.image_icon);
                suggestImage2.setImageResource(R.drawable.image_icon);
                suggestImage3.setImageResource(R.drawable.image_icon);
                suggestImage4.setImageResource(R.drawable.image_icon);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri;
        RequestOptions option1 = new RequestOptions().centerInside();

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Glide.with(getApplicationContext()).load(selectedImageUri).apply(option1).into(galleryImage);
            Glide.with(getApplicationContext()).asBitmap().load(selectedImageUri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    galleryImage.setImageBitmap(resource);
                }
            });
            suggestButton.setVisibility(View.VISIBLE);
            suggestImage1.setImageResource(0);
            suggestImage2.setImageResource(0);
            suggestImage3.setImageResource(0);
            suggestImage4.setImageResource(0);
        }
    }
}