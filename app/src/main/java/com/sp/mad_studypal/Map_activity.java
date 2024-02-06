package com.sp.mad_studypal;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;
import androidx.appcompat.app.AppCompatActivity;

public class Map_activity extends AppCompatActivity {

    private ImageView imageView;
    private ZoomControls zoomControls;
    private float screenWidth;
    private float screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        imageView = findViewById(R.id.imageView2);
        zoomControls = findViewById(R.id.zoomControls);

        // Get screen dimensions in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Set initial zoom level
        imageView.setScaleX(1f); // Zoom in by 1.5x
        imageView.setScaleY(1f);

        // Calculate initial scroll position to center the image
        int initialScrollX = (imageView.getWidth() - (int) screenWidth) / 2;
        int initialScrollY = (imageView.getHeight() - (int) screenHeight) / 2;

        // Scroll to the center of the image
        findViewById(R.id.scrollView).scrollTo(initialScrollX, initialScrollY);
        findViewById(R.id.horizontalScrollView).scrollTo(initialScrollX, initialScrollY);

        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float scaleX = imageView.getScaleX() + 0.1f;
                float scaleY = imageView.getScaleY() + 0.1f;
                imageView.setScaleX(scaleX);
                imageView.setScaleY(scaleY);
            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the current scale is smaller than the screen dimensions
                if (imageView.getScaleX() > screenWidth / imageView.getWidth() &&
                        imageView.getScaleY() > screenHeight / imageView.getHeight()) {
                    float scaleX = imageView.getScaleX() - 0.1f;
                    float scaleY = imageView.getScaleY() - 0.1f;
                    imageView.setScaleX(scaleX);
                    imageView.setScaleY(scaleY);
                }
            }
        });
    }
}

