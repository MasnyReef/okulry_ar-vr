package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;




public class ViewRight extends AppCompatActivity implements Camera.PreviewCallback, SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private SurfaceView surfaceView1;
    private SurfaceView surfaceView2;
    private SurfaceHolder surfaceHolder0;
    private SurfaceHolder surfaceHolder1;
    private SurfaceHolder surfaceHolder2;
    private Camera camera;

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // Obsługa sukcesu w ustawieniu ostrości
            } else {
                // Obsługa niepowodzenia w ustawieniu ostrości
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView1 = findViewById(R.id.surfaceViewRight);
        surfaceView2 = findViewById(R.id.surfaceViewLeft);
        surfaceView = findViewById(R.id.surfaceView);

        surfaceHolder1 = surfaceView1.getHolder();
        surfaceHolder2 = surfaceView2.getHolder();
        surfaceHolder0 = surfaceView.getHolder();

        surfaceHolder0.addCallback(this);

                // Otwieranie kamery
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceHolder0);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        camera = Camera.open();

        try {
            camera.setPreviewDisplay(surfaceHolder0);
            camera.setPreviewCallback(this);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Canvas canvas2 = surfaceHolder2.lockCanvas();
        if (canvas2 == null) return;
        canvas2.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Canvas canvas1 = surfaceHolder1.lockCanvas();
        if (canvas1 == null) return;
        canvas1.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        YuvImage image = new YuvImage(data, ImageFormat.NV21, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 50, out);
        byte[] jpegData = out.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);

        if (bmp == null) {
            surfaceHolder2.unlockCanvasAndPost(canvas2);
            surfaceHolder1.unlockCanvasAndPost(canvas1);
            return;
        }

        Matrix matrix = new Matrix();

        float[] src = {0, 0, bmp.getWidth(), 0, bmp.getWidth(), bmp.getHeight(), 0, bmp.getHeight()};
        float[] dst = {0, 0, surfaceView2.getWidth(), 0, surfaceView2.getWidth(), surfaceView2.getHeight(), 0, surfaceView2.getHeight()};
        matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        canvas2.drawBitmap(bmp, matrix, null);
        surfaceHolder2.unlockCanvasAndPost(canvas2);

        matrix.reset();
        src = new float[]{0, 0, bmp.getWidth(), 0, bmp.getWidth(), bmp.getHeight(), 0, bmp.getHeight()};
        dst = new float[]{0, 0, surfaceView1.getWidth(), 0, surfaceView1.getWidth(), surfaceView1.getHeight(), 0, surfaceView1.getHeight()};
        matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        canvas1.drawBitmap(bmp, matrix, null);
        surfaceHolder1.unlockCanvasAndPost(canvas1);
        bmp.recycle();
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            camera.setPreviewDisplay(surfaceHolder0);
            camera.startPreview();
            camera.autoFocus(autoFocusCallback); // wywołanie metody autoFocus z przekazaniem obiektu AutoFocusCallback

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}






