package io.oskm.helloandroid.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import io.oskm.helloandroid.R;

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        final CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(getApplicationContext());
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(cameraSurfaceView);

        Button shootButton = (Button) findViewById(R.id.shootButton);

        shootButton.setVisibility(View.VISIBLE);

        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSurfaceView.capture(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {

                            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

                            String fileUrl = MediaStore.Images.Media.insertImage(getContentResolver(), bm, "카메라정지화상", "sample");

                            if (null == fileUrl) {
                                Log.d("Still", "Image insert failed");
                                return;
                            } else {
                                Uri picUri = Uri.parse(fileUrl);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, picUri));
                            }

                        }catch(Exception e) {
                            Log.e("Still", "Error wirting file", e);


                        } finally {
                            cameraSurfaceView.surfaceCreated(cameraSurfaceView.getHolder());
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
