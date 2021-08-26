package edu.skku.sketchtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {

    Context context;
    FrameLayout sketchLayout;
    DrawingView drawingView;
    DrawingView coloringView;
    CursorView cursorView;
    StickerView stickerView;
    LinearLayout brushViewLinearLayout;
    ImageView smallBrushView;
    ImageView mediumBrushView;
    ImageView largeBrushView;
    FloatingActionButton checkButton;
    FloatingActionButton penButton;
    FloatingActionButton colorButton;
    FloatingActionButton brushButton;
    FloatingActionButton eraserButton;
    FloatingActionButton cursorButton;
    FloatingActionButton suggestButton;
    FloatingActionButton deleteButton;
    LinearLayout imageViewLinearLayout;
    ImageView neighborImageView1;
    ImageView neighborImageView2;
    ImageView neighborImageView3;
    ImageView neighborImageView4;

    private static final float SMALL_BRUSH_SIZE = 20;
    private static final float MEDIUM_BRUSH_SIZE = 60;
    private static final float LARGE_BRUSH_SIZE = 100;
    private boolean isSketchFinished = false;
    private boolean isEraserMode = false;

    Bitmap croppedScreenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();

        findViewsById();

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSketchFinished = true;
                brushViewLinearLayout.setVisibility(View.INVISIBLE);
                cursorButton.setVisibility(View.INVISIBLE);
                suggestButton.setVisibility(View.INVISIBLE);
                checkButton.setVisibility(View.GONE);
                penButton.setVisibility(View.GONE);
                colorButton.setVisibility(View.VISIBLE);
                brushButton.setVisibility(View.VISIBLE);
                coloringView.setVisibility(View.VISIBLE);
                coloringView.bringToFront();
            }
        });

        penButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.bringToFront();
                drawingView.setPenMode();
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        brushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEraserMode = false;
                coloringView.bringToFront();
                coloringView.setPenMode();
                setBrushView(smallBrushView, SMALL_BRUSH_SIZE);
                setBrushView(mediumBrushView, MEDIUM_BRUSH_SIZE);
                setBrushView(largeBrushView, LARGE_BRUSH_SIZE);
                brushViewLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEraserMode = true;
                setBrushView(smallBrushView, SMALL_BRUSH_SIZE);
                setBrushView(mediumBrushView, MEDIUM_BRUSH_SIZE);
                setBrushView(largeBrushView, LARGE_BRUSH_SIZE);
                brushViewLinearLayout.setVisibility(View.VISIBLE);
                if (isSketchFinished == false) {
                    drawingView.bringToFront();
                    drawingView.setEraserMode();
                }
                else {
                    coloringView.bringToFront();
                    coloringView.setEraserMode();
                }
            }
        });

        smallBrushView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEraserMode == false) {
                    coloringView.setPenBrushSize(SMALL_BRUSH_SIZE);
                }
                else if (isSketchFinished == false) {
                    drawingView.setEraserBrushSize(SMALL_BRUSH_SIZE);
                }
                else {
                    coloringView.setEraserBrushSize(SMALL_BRUSH_SIZE);
                }
            }
        });

        mediumBrushView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEraserMode == false) {
                    coloringView.setPenBrushSize(MEDIUM_BRUSH_SIZE);
                }
                else if (isSketchFinished == false) {
                    drawingView.setEraserBrushSize(MEDIUM_BRUSH_SIZE);
                }
                else {
                    coloringView.setEraserBrushSize(MEDIUM_BRUSH_SIZE);
                }
            }
        });

        largeBrushView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEraserMode == false) {
                    coloringView.setPenBrushSize(LARGE_BRUSH_SIZE);
                }
                else if (isSketchFinished == false) {
                    drawingView.setEraserBrushSize(LARGE_BRUSH_SIZE);
                }
                else {
                    coloringView.setEraserBrushSize(LARGE_BRUSH_SIZE);
                }
            }
        });

        cursorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushViewLinearLayout.setVisibility(View.INVISIBLE);
                cursorView.bringToFront();
            }
        });

        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushViewLinearLayout.setVisibility(View.INVISIBLE);
                stickerView.bringToFront();
                croppedScreenshot = getCroppedScreenshot(sketchLayout);
                if (croppedScreenshot != null) {
                    neighborImageView1.setImageBitmap(croppedScreenshot);
                }
                else {
                    Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView1);
                }
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView2);
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView3);
                Glide.with(context).load(R.drawable.icon_heart).into(neighborImageView4);
                imageViewLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        neighborImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.eraseStickerArea(cursorView.getBeginCoordinate().x, cursorView.getBeginCoordinate().y, cursorView.getEndCoordinate().x, cursorView.getEndCoordinate().y);
                loadSticker(neighborImageView1);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        neighborImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.eraseStickerArea(cursorView.getBeginCoordinate().x, cursorView.getBeginCoordinate().y, cursorView.getEndCoordinate().x, cursorView.getEndCoordinate().y);
                loadSticker(neighborImageView2);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        neighborImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.eraseStickerArea(cursorView.getBeginCoordinate().x, cursorView.getBeginCoordinate().y, cursorView.getEndCoordinate().x, cursorView.getEndCoordinate().y);
                loadSticker(neighborImageView3);
                imageViewLinearLayout.setVisibility(View.INVISIBLE);
            }
        });

        neighborImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.eraseStickerArea(cursorView.getBeginCoordinate().x, cursorView.getBeginCoordinate().y, cursorView.getEndCoordinate().x, cursorView.getEndCoordinate().y);
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

    // 브러쉬 사이즈 표시
    protected void setBrushView(ImageView view, float size) {
        int width = eraserButton.getWidth() * 2;
        int height = eraserButton.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint drawPaint = drawingView.getDrawPaint();
        if (isEraserMode == true) {
            drawPaint.setXfermode(null);
            drawPaint.setColor(Color.BLACK);
        }
        drawPaint.setStrokeWidth(size);
        canvas.drawLine(width / 4, height / 2, width * 3 / 4, height / 2, drawPaint);
        view.setImageBitmap(bitmap);
    }

    // 스티커뷰에서 스티커 추가
    protected void loadSticker(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        stickerView.addSticker(drawableSticker);
    }

    // 스크린샷 부분캡쳐
    protected Bitmap getCroppedScreenshot(View view) {

        float beginX = cursorView.getBeginCoordinate().x;
        float beginY = cursorView.getBeginCoordinate().y;
        float endX = cursorView.getEndCoordinate().x;
        float endY = cursorView.getEndCoordinate().y;

        int x = Math.round(beginX);
        int y = Math.round(beginY);
        int width = Math.abs(Math.round(endX - beginX));
        int height = Math.abs(Math.round(endY - beginY));

        if (width == 0 || height == 0) {
            return null;
        }

        Bitmap originBmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(originBmp);
        view.draw(canvas);

        Bitmap croppedBmp = Bitmap.createBitmap(originBmp, x, y, width, height);
        return croppedBmp;
    }

    // 색상 선택
    protected void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        final ArrayList<String> colors = new ArrayList<>();
        colors.add("#258180");
        colors.add("#3C8D2F");
        colors.add("#20724F");
        colors.add("#6a3ab2");
        colors.add("#323299");
        colors.add("#800080");
        colors.add("#b79716");
        colors.add("#966d37");
        colors.add("#b77231");
        colors.add("#000000");

        colorPicker.setRoundColorButton(true).setColumns(5).setColorButtonTickColor(Color.parseColor("#000000"))
                .setDefaultColorButton(Color.parseColor("#000000")).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                coloringView.setPaintColor(color);
                setBrushView(smallBrushView, SMALL_BRUSH_SIZE);
                setBrushView(mediumBrushView, MEDIUM_BRUSH_SIZE);
                setBrushView(largeBrushView, LARGE_BRUSH_SIZE);
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    protected void findViewsById() {
        sketchLayout = findViewById(R.id.sketchLayout);
        drawingView = findViewById(R.id.drawingView);
        coloringView = findViewById(R.id.coloringView);
        cursorView = findViewById(R.id.cursorView);
        stickerView = findViewById(R.id.stickerView);
        brushViewLinearLayout = findViewById(R.id.brushViewLinearLayout);
        smallBrushView = findViewById(R.id.smallBrushView);
        mediumBrushView = findViewById(R.id.mediumBrushView);
        largeBrushView = findViewById(R.id.largeBrushView);
        checkButton = findViewById(R.id.checkButton);
        penButton = findViewById(R.id.penButton);
        colorButton = findViewById(R.id.colorButton);
        brushButton = findViewById(R.id.brushButton);
        eraserButton = findViewById(R.id.eraserButton);
        cursorButton = findViewById(R.id.cursorButton);
        suggestButton = findViewById(R.id.suggestButton);
        deleteButton = findViewById(R.id.deleteButton);
        imageViewLinearLayout = findViewById(R.id.imageViewLinearLayout);
        neighborImageView1 = findViewById(R.id.neighborImageView1);
        neighborImageView2 = findViewById(R.id.neighborImageView2);
        neighborImageView3 = findViewById(R.id.neighborImageView3);
        neighborImageView4 = findViewById(R.id.neighborImageView4);
    }

}