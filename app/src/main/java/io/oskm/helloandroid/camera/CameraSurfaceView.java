package io.oskm.helloandroid.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by oskm on 2014-08-18.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public CameraSurfaceView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = camera.getParameters();
        params.setPreviewSize(width, height);
        //camera.setParameters(params);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera = null;
    }

    public boolean capture(Camera.PictureCallback jpegHandler) {
        if (null != camera) {
            camera.takePicture(null, null, jpegHandler);
            return true;
        } else {
            return false;
        }
    }

    private SurfaceHolder holder;
    private Camera camera;

}
