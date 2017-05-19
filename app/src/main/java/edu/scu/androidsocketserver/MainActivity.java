package edu.scu.androidsocketserver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SimpleHttpServer shs;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        WebConfiguration wc = new WebConfiguration();
        wc.setPort(8088);
        wc.setMaxParallels(50);
        shs = new SimpleHttpServer(wc);
        shs.registerResourceHandler(new ResourceInAssetsHandler(this));
        shs.registerResourceHandler(new UploadImageHandler(this) {
            @Override
            public void onImageLoaded(final String path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showImage(path);
                    }
                });
            }
        });
        shs.registerResourceHandler(new GetPostHandler());
        shs.startAsync();
    }

    protected void showImage(final String path) {
        Log.d("spy", "showImage:"+path);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView mImageView = (ImageView) findViewById(R.id.imageView);
                Bitmap bm = BitmapFactory.decodeFile(path);
                mImageView.setImageBitmap(bm);
                Toast.makeText(MainActivity.this, "upload success!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        shs.stopAsync();
    }
}