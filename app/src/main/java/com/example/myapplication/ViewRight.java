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

//public class ViewRight extends AppCompatActivity implements SurfaceHolder.Callback{
//
//    private static final String TAG = "MainActivity";
//    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
//
//    private CameraManager cameraManager;
//    private String cameraId;
//    private CameraDevice cameraDevice;
//    private CaptureRequest.Builder captureRequestBuilder;
//    private CameraCaptureSession cameraCaptureSession;
//    private SurfaceView surfaceViewLeft;
//    private SurfaceView surfaceViewRight;
//    private SurfaceHolder holderLeft;
//    private SurfaceHolder holderRight;
//    private HandlerThread backgroundThread;
//    private Handler backgroundHandler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // get references to the SurfaceViews in the layout
////        surfaceViewLeft = findViewById(R.id.surfaceViewLeft);
////        holderLeft = surfaceViewLeft.getHolder();
////        holderLeft.addCallback(this);
//
//        surfaceViewRight = findViewById(R.id.surfaceViewRight);
//        holderRight = surfaceViewRight.getHolder();
//        holderRight.addCallback(this);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startBackgroundThread();
//    }
//
//    @Override
//    protected void onPause() {
//        closeCamera();
//        stopBackgroundThread();
//        super.onPause();
//    }
//
//    @Override
//    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String cameraId = manager.getCameraIdList()[0];
//            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            Size[] sizes = map.getOutputSizes(SurfaceHolder.class);
//
//            // Choose the smallest size that is at least 640x480
//            Size chosenSize = null;
//            for (Size size : sizes) {
//                if (size.getWidth() >= 640 && size.getHeight() >= 480) {
//                    if (chosenSize == null || chosenSize.getWidth() > size.getWidth()) {
//                        chosenSize = size;
//                    }
//                }
//            }
//
//            // Open the camera and configure the preview
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//
//            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
//                @Override
//                public void onOpened(CameraDevice camera) {
//                    cameraDevice = camera;
//                    Surface surface = holder.getSurface();
//                    try {
//                        captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                        captureRequestBuilder.addTarget(surface);
//
//                        camera.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
//                            @Override
//                            public void onConfigured(CameraCaptureSession session) {
//                                if (cameraDevice == null) {
//                                    return;
//                                }
//
//                                try {
//                                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//                                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//                                    cameraCaptureSession = session;
//                                    captureRequestBuilder.build();
//                                    cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
//                                } catch (CameraAccessException e) {
//                                    Log.d("Camera", "Error starting camera preview: " + e.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onConfigureFailed(CameraCaptureSession session) {
//                                Log.d("Camera", "Configuration failed for camera session");
//                            }
//                        }, backgroundHandler);
//                    } catch (CameraAccessException e) {
//                        Log.d("Camera", "Error setting camera preview: " + e.getMessage());
//                    }
//                }
//
//                @Override
//                public void onDisconnected(CameraDevice camera) {
//                    closeCamera();
//                }
//
//                @Override
//                public void onError(CameraDevice camera, int error) {
//                    closeCamera();
//                }
//            }, backgroundHandler);
//        } catch (CameraAccessException e) {
//            Log.d("Camera", "Error accessing camera: " + e.getMessage());
//        }
//    }
//
//
//    @Override
//    public void surfaceChanged(@NonNull SurfaceHolder holder, int i, int i1, int i2) {
//        if (cameraDevice != null) {
//            closeCamera();
//            surfaceCreated(holder);
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//        closeCamera();
//    }
//
//    private void closeCamera() {
//        if (cameraCaptureSession != null) {
//            cameraCaptureSession.close();
//            cameraCaptureSession = null;
//        }
//
//        if (cameraDevice != null) {
//            cameraDevice.close();
//            cameraDevice = null;
//        }
//    }
//
//    private void startBackgroundThread() {
//        backgroundThread = new HandlerThread("CameraBackground");
//        backgroundThread.start();
//        backgroundHandler = new Handler(backgroundThread.getLooper());
//    }
//
//    private void stopBackgroundThread() {
//        backgroundThread.quitSafely();
//        try {
//            backgroundThread.join();
//            backgroundThread = null;
//            backgroundHandler = null;
//        } catch (InterruptedException e) {
//            Log.d("Camera", "Error stopping background thread: " + e.getMessage());
//        }
//    }
//
//}

//public class ViewRight extends AppCompatActivity implements SurfaceHolder.Callback {
//
//    private Camera camera;
//    private SurfaceView surfaceView1, surfaceView2;
//    private SurfaceHolder surfaceHolder1, surfaceHolder2;
//
//    private boolean jd = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        surfaceView1 = findViewById(R.id.surfaceViewLeft);
//        surfaceView2 = findViewById(R.id.surfaceViewRight);
//
//        surfaceHolder1 = surfaceView1.getHolder();
//        surfaceHolder1.addCallback(this);
//
//        surfaceHolder2 = surfaceView2.getHolder();
//        surfaceHolder2.addCallback(this);
//
//        // Otwieranie kamery
//        try {
//            camera = Camera.open();
//            camera.setPreviewDisplay(surfaceHolder1);
//            camera.startPreview();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // Do nothing
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        if (holder == surfaceHolder2 || holder == surfaceHolder1) {
//            try {
//                camera.setPreviewDisplay(surfaceHolder2);
//                //camera.setPreviewDisplay(surfaceHolder1);
//
////                // Pobieramy Bitmap z SurfaceView, do którego jest przypisany holderRight
////                surfaceView1.setDrawingCacheEnabled(true);
////                Bitmap bitmap = Bitmap.createBitmap(surfaceView1.getDrawingCache());
////                surfaceView1.setDrawingCacheEnabled(false);
////
////                // Rysujemy Bitmap na SurfaceView, do którego jest przypisany holderLeft
////                Canvas canvas = surfaceHolder2.lockCanvas();
////                canvas.drawBitmap(bitmap, 0, 0, null);
////                holder.unlockCanvasAndPost(canvas);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // Zamykanie kamery
//        camera.stopPreview();
//        camera.release();
//        camera = null;
//    }
//}


//public class ViewRight extends AppCompatActivity implements SurfaceHolder.Callback {
//
//    private Camera camera;
//    private SurfaceView surfaceView1, surfaceView2;
//    private SurfaceHolder surfaceHolder1, surfaceHolder2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        surfaceView1 = findViewById(R.id.surfaceViewLeft);
//        surfaceHolder1 = surfaceView1.getHolder();
//        surfaceHolder1.addCallback(this);
//
//        surfaceView2 = findViewById(R.id.surfaceViewRight);
//        surfaceHolder2 = surfaceView2.getHolder();
//        surfaceHolder2.addCallback(this);
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        camera = Camera.open();
//        try {
//            camera.setPreviewDisplay(surfaceHolder1);
//            camera.setPreviewDisplay(surfaceHolder2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        camera.startPreview();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        // kod do obsługi zmiany rozmiaru powierzchni wyświetlania
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        camera.stopPreview();
//        camera.release();
//    }
//}


//public class ViewRight extends AppCompatActivity {
//
//    private SurfaceView surfaceView1;
//    private SurfaceView surfaceView2;
//    private SurfaceHolder holder1;
//    private SurfaceHolder holder2;
//    private Camera mCamera;
//    private Bitmap bmp;
//    private boolean isRunning = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        surfaceView1 = findViewById(R.id.surfaceViewLeft);
//        surfaceView2 = findViewById(R.id.surfaceViewRight);
//
//        holder1 = surfaceView1.getHolder();
//        holder2 = surfaceView2.getHolder();
//
//        holder1.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                startCameraPreview(surfaceHolder);
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                stopCameraPreview();
//            }
//        });
//
//        holder2.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//            }
//        });
//    }
//
//    private void startCameraPreview(SurfaceHolder holder) {
//        try {
//            mCamera = Camera.open();
//            mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();
//            isRunning = true;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (isRunning) {
//                        try {
//                            Thread.sleep(50);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            surfaceView1.setDrawingCacheEnabled(true);
//                            Bitmap bitmap = surfaceView1.getDrawingCache();
//                            surfaceView1.setDrawingCacheEnabled(false);
//                            Canvas canvas = holder2.lockCanvas();
//                            canvas.drawBitmap(bitmap, 0, 0, null);
//                            holder2.unlockCanvasAndPost(canvas);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void stopCameraPreview() {
//        isRunning = false;
//        if (mCamera != null) {
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//}


public class ViewRight extends AppCompatActivity implements Camera.PreviewCallback, SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private SurfaceView surfaceView1;
    private SurfaceView surfaceView2;
    private SurfaceHolder surfaceHolder0;
    private SurfaceHolder surfaceHolder1;
    private SurfaceHolder surfaceHolder2;
    private Camera camera;

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
//        int centerX = bmp.getWidth() / 2;
//        int centerY = bmp.getHeight() / 2;
//        float radius = Math.min(centerX, centerY) * 0.6f;
//        float scale = (float) bmp.getHeight() / (float) bmp.getWidth();
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


    public Bitmap transformBitmap(Bitmap source) {
        // Definiowanie przekształcenia
        Matrix matrix = new Matrix();
        float[] src = {0, 0, source.getWidth(), 0, 0, source.getHeight(), source.getWidth(), source.getHeight()};
        float[] dst = {0, 0, source.getWidth(), 0, source.getWidth() * 0.6f, source.getHeight(), source.getWidth() * 0.6f, source.getHeight()};
        matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        float[] values = {0.6f, 0, 0, 0, 0.6f, 0, 0, 0, 1};
        Matrix matrix1 = new Matrix();
        matrix1.setValues(values);
        matrix.postConcat(matrix1);

        // Tworzenie przekształconego obrazu
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(source, matrix, paint);

        return output;
    }


//    float xscale;
//    float yscale;
//    float xshift;
//    float yshift;
//    int [] s;
//    long getRadXStart = 0;
//    long getRadXEnd = 0;
//    long startSample = 0;
//    long endSample = 0;
//
//
//    public Bitmap barrel (Bitmap input, float k){
//        //Log.e(TAG, "***********INSIDE BARREL METHOD ");
//
//
//        float centerX=input.getWidth()/2; //center of distortion
//        float centerY=input.getHeight()/2;
//
//        int width = input.getWidth(); //image bounds
//        int height = input.getHeight();
//
//        Bitmap dst = Bitmap.createBitmap(width, height,input.getConfig() ); //output pic
//        // Log.e(TAG, "***********dst bitmap created ");
//        xshift = calc_shift(0,centerX-1,centerX,k);
//
//        float newcenterX = width-centerX;
//        float xshift_2 = calc_shift(0,newcenterX-1,newcenterX,k);
//
//        yshift = calc_shift(0,centerY-1,centerY,k);
//
//        float newcenterY = height-centerY;
//        float yshift_2 = calc_shift(0,newcenterY-1,newcenterY,k);
//
//        xscale = (width-xshift-xshift_2)/width;
//        //  Log.e(TAG, "***********xscale ="+xscale);
//        yscale = (height-yshift-yshift_2)/height;
//        //  Log.e(TAG, "***********yscale ="+yscale);
//        //  Log.e(TAG, "***********filter.barrel() about to loop through bm");
//
//
//        int origPixel;
//        long startLoop = System.currentTimeMillis();
//        for(int j=0;j<dst.getHeight();j++){
//            for(int i=0;i<dst.getWidth();i++){
//                origPixel= input.getPixel(i,j);
//                getRadXStart = System.currentTimeMillis();
//                float x = getRadialX((float)j,(float)i,centerX,centerY,k);
//                getRadXEnd= System.currentTimeMillis();
//
//                float y = getRadialY((float)j,(float)i,centerX,centerY,k);
//
//                sampleImage(input,x,y);
//
//                int color = ((s[1]&0x0ff)<<16)|((s[2]&0x0ff)<<8)|(s[3]&0x0ff);
//                //            System.out.print(i+" "+j+" \\");
//
//                if( Math.sqrt( Math.pow(i - centerX, 2) + ( Math.pow(j - centerY, 2) ) ) <= 150 ){
//                    dst.setPixel(i, j, color);
//                }else{
//                    dst.setPixel(i,j,origPixel);
//                }
//            }
//        }
//
//        return dst;
//    }
//
//    void sampleImage(Bitmap arr, float idx0, float idx1)
//    {
//        startSample = System.currentTimeMillis();
//        s = new int [4];
//        if(idx0<0 || idx1<0 || idx0>(arr.getHeight()-1) || idx1>(arr.getWidth()-1)){
//            s[0]=0;
//            s[1]=0;
//            s[2]=0;
//            s[3]=0;
//            return;
//        }
//
//        float idx0_fl=(float) Math.floor(idx0);
//        float idx0_cl=(float) Math.ceil(idx0);
//        float idx1_fl=(float) Math.floor(idx1);
//        float idx1_cl=(float) Math.ceil(idx1);
//
//        int [] s1 = getARGB(arr,(int)idx0_fl,(int)idx1_fl);
//        int [] s2 = getARGB(arr,(int)idx0_fl,(int)idx1_cl);
//        int [] s3 = getARGB(arr,(int)idx0_cl,(int)idx1_cl);
//        int [] s4 = getARGB(arr,(int)idx0_cl,(int)idx1_fl);
//
//        float x = idx0 - idx0_fl;
//        float y = idx1 - idx1_fl;
//
//        s[0]= (int) (s1[0]*(1-x)*(1-y) + s2[0]*(1-x)*y + s3[0]*x*y + s4[0]*x*(1-y));
//        s[1]= (int) (s1[1]*(1-x)*(1-y) + s2[1]*(1-x)*y + s3[1]*x*y + s4[1]*x*(1-y));
//        s[2]= (int) (s1[2]*(1-x)*(1-y) + s2[2]*(1-x)*y + s3[2]*x*y + s4[2]*x*(1-y));
//        s[3]= (int) (s1[3]*(1-x)*(1-y) + s2[3]*(1-x)*y + s3[3]*x*y + s4[3]*x*(1-y));
//
//        endSample = System.currentTimeMillis();
//    }
//
//    int [] getARGB(Bitmap buf,int x, int y){
//
//        int rgb = buf.getPixel(y, x); // Returns by default ARGB.
//        int [] scalar = new int[4];
//        scalar[0] = (rgb >>> 24) & 0xFF;
//        scalar[1] = (rgb >>> 16) & 0xFF;
//        scalar[2] = (rgb >>> 8) & 0xFF;
//        scalar[3] = (rgb >>> 0) & 0xFF;
//        return scalar;
//    }
//
//    float getRadialX(float x,float y,float cx,float cy,float k){
//
//        x = (x*xscale+xshift);
//        y = (y*yscale+yshift);
//        float res = x+((x-cx)*k*((x-cx)*(x-cx)+(y-cy)*(y-cy)));
//        return res;
//    }
//
//    float getRadialY(float x,float y,float cx,float cy,float k){
//
//        x = (x*xscale+xshift);
//        y = (y*yscale+yshift);
//        float res = y+((y-cy)*k*((x-cx)*(x-cx)+(y-cy)*(y-cy)));
//        return res;
//    }
//
//    float thresh = 1;
//
//    float calc_shift(float x1,float x2,float cx,float k){
//
//        float x3 = (float)(x1+(x2-x1)*0.5);
//        float res1 = x1+((x1-cx)*k*((x1-cx)*(x1-cx)));
//        float res3 = x3+((x3-cx)*k*((x3-cx)*(x3-cx)));
//
//        if(res1>-thresh && res1 < thresh)
//            return x1;
//        if(res3<0){
//            return calc_shift(x3,x2,cx,k);
//        }
//        else{
//            return calc_shift(x1,x3,cx,k);
//        }
//    }

    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSession;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String cameraId = manager.getCameraIdList()[0];
//            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            Size[] sizes = map.getOutputSizes(SurfaceHolder.class);
//
//            // Choose the smallest size that is at least 640x480
//            Size chosenSize = null;
//            for (Size size : sizes) {
//                if (size.getWidth() >= 640 && size.getHeight() >= 480) {
//                    if (chosenSize == null || chosenSize.getWidth() > size.getWidth()) {
//                        chosenSize = size;
//                    }
//                }
//            }
//
//            // Open the camera and configure the preview
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//
//            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
//                @Override
//                public void onOpened(CameraDevice camera) {
//                    cameraDevice = camera;
//                    Surface surface = holder.getSurface();
//                    try {
//                        captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                        captureRequestBuilder.addTarget(surface);
//                        camera.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
//                            @Override
//                            public void onConfigured(CameraCaptureSession session) {
//                                if (cameraDevice == null) {
//                                    return;
//                                }
//
//                                try {
//                                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//                                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//                                    cameraCaptureSession = session;
//                                    captureRequestBuilder.build();
//                                    cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
//                                } catch (CameraAccessException e) {
//                                    Log.d("Camera", "Error starting camera preview: " + e.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onConfigureFailed(CameraCaptureSession session) {
//                                Log.d("Camera", "Configuration failed for camera session");
//                            }
//                        }, backgroundHandler);
//                    } catch (CameraAccessException e) {
//                        Log.d("Camera", "Error setting camera preview: " + e.getMessage());
//                    }
//                }
//
//                @Override
//                public void onDisconnected(CameraDevice camera) {
//                    closeCamera();
//                }
//
//                @Override
//                public void onError(CameraDevice camera, int error) {
//                    closeCamera();
//                }
//            }, backgroundHandler);
//        } catch (CameraAccessException e) {
//            Log.d("Camera", "Error accessing camera: " + e.getMessage());
//        }
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            camera.setPreviewDisplay(surfaceHolder0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}






