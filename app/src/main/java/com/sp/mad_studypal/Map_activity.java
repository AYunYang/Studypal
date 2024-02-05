package com.sp.mad_studypal;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;
import androidx.appcompat.app.AppCompatActivity;

public class Map_activity extends AppCompatActivity {

    private ImageView imageView;
    private ZoomControls zoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        imageView = findViewById(R.id.imageView2);
        zoomControls = findViewById(R.id.zoomControls);

        // Set initial zoom level
        imageView.setScaleX(1.5f); // Zoom in by 1.5x
        imageView.setScaleY(1.5f);

        // Calculate the center of the image
        int centerX = imageView.getWidth() / 2;
        int centerY = imageView.getHeight() / 2;

        // Scroll to the center of the image
        findViewById(R.id.scrollView).scrollTo(centerX, centerY);
        findViewById(R.id.horizontalScrollView).scrollTo(centerX, centerY);

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
                float scaleX = imageView.getScaleX() - 0.1f;
                float scaleY = imageView.getScaleY() - 0.1f;
                imageView.setScaleX(scaleX);
                imageView.setScaleY(scaleY);
            }
        });
    }
}
