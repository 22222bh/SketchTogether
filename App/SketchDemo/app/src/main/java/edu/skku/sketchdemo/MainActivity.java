package edu.skku.sketchdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
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
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import smile.TSNE;

import edu.skku.sketchdemo.ml.EffiExtractor;

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

        Classifier classifier = new Classifier();
        loadData();

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
                List<String> suggestedImageList = new ArrayList<String>();

                try {
                    EffiExtractor model = EffiExtractor.newInstance(MainActivity.this);

                    // Creates inputs for reference.
                    TensorImage image = TensorImage.fromBitmap(galleryImageBmpResized);

                    // Runs model inference and gets result.
                    EffiExtractor.Outputs outputs = model.process(image);
                    TensorBuffer newFeatureTmp = outputs.getFeatureAsTensorBuffer();
                    double[] newFeature = newFeatureTmp.getFloatArray();
                    featureSet[featureNum] = newFeature; // newFeature를 어레이로 바꾼 것
                    model.close();

                    TSNE tsne = new TSNE(featureSet, 2);
                    int idx = 0;
                    for (int i = 0; i<featureNum+1; i++) {
                        double x = tsne[i][0];
                        double y = tsne[i][1];
                        listDataPointOriginal[idx].setX(x);
                        listDataPoint[idx++].setY(y);
                    }
                    // 이제 listDataPoint에 newfeature까지 다 들어있음

                    int distIdx = 0; // 일단은 유클리드
                    DistanceAlgorithm distanceAlgorithm = distanceAlgorithms[distIdx];
                    // if (distanceAlgorithm instanceof MinkowskiDistance){
                    //     int p = bundle.getInt(Constants.MINKOWSKI_P);
                    //     ((MinkowskiDistance)distanceAlgorithm).setP(p);
                    // }

                    classifier.reset();
                    classifier.setDistanceAlgorithm(distanceAlgorithms[distIdx]);
                    classifier.setListDataPoint(listDataPoint);


                    DataPoint newFeature = featureSet[featureNum];
                    suggestedImageList = classifier.classify(newFeature)

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Resources resources = MainActivity.this.getResources();
                int resourceId1 = resources.getIdentifier(suggestedImageList.get(0), "drawable", MainActivity.this.getPackageName());
                int resourceId2 = resources.getIdentifier(suggestedImageList.get(1), "drawable", MainActivity.this.getPackageName());
                int resourceId3 = resources.getIdentifier(suggestedImageList.get(2), "drawable", MainActivity.this.getPackageName());
                int resourceId4 = resources.getIdentifier(suggestedImageList.get(3), "drawable", MainActivity.this.getPackageName());

                suggestImage1.setImageResource(resourceId1);
                suggestImage2.setImageResource(resourceId2);
                suggestImage3.setImageResource(resourceId3);
                suggestImage4.setImageResource(resourceId4);

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

    public void loadData() { // 이거를 최초 1회만 하게 해야 함!!!
        try {
            dataSetFile = "path to data set";
            int idx = 0;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open
                    (dataSetFile))); // 1. 안닫아도 되는지 2. 메모리 해제는?
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] data = line.split(" "); // 일단은 공백으로 구분되어 있다고 가정.
                double value;
                for (int i = 0; i< featureLen; i++) {
                    value = Double.parseDouble(data[i]);
                    featuerSet[idx][i] = value;
                }
                idx++;
            }

            fileNameFile = "path to filename";
            BufferedReader fileNameReader = new BufferedReader(new InputStreamReader(getAssets().open(fileNameFile))); // 1. 안닫아도 되는지 2. 메모리 해제는?
            String line, filename;
            DataPoint dataPoint;
            while ((line = fileNameReader.readLine()) != null){
                filename = line
                DataPoint dataPoint = new DataPoint(0, 0, filename);
                listDataPointOriginal.add(dataPoint);
                listDataPoint.add(dataPoint);
            }
            dataPoint = new DataPoint(0, 0, "input_image");
            listDataPointOriginal.add(dataPoint);
            listDataPoint.add(dataPoint);
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
}