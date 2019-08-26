package com.northmeter.wartermeterdemo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.northmeter.wartermeterdemo.utils.CarmeraUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lht on 2016/5/10.
 */
public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback, Camera.AutoFocusCallback {
    private static final String TAG = "CameraPreview";
    private int viewWidth = 0;
    private int viewHeight = 0;
    private float mPreviwRate = -1f;
    float previewRate;
    public  static Context context;
    /** 监听接口 */
    //拍照结束后的回调接口
    private OnCameraStatusListener listener;
    //获得拍照图片大小的接口
    private OnCameraPictureSizeListener pictureSizeListener;


    private SurfaceHolder holder;
    private Camera camera;
    private FocusView mFocusView;

    //创建一个PictureCallback对象，并实现其中的onPictureTaken方法
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        // 该方法用于处理拍摄后的照片数据
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 停止照片拍摄
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }
            // 调用结束事件
            if (null != listener) {

                listener.onCameraStopped(data);
            }
        }
    };

    // Preview类的构造方法
    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获得SurfaceHolder对象
        holder = getHolder();
        // 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
        holder.addCallback(this);
        // 设置SurfaceHolder对象的类型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setOnTouchListener(onTouchListener);
    }

    // 在surface创建时激发
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if(!CarmeraUtils.checkCameraHardware(getContext())) {
                Toast.makeText(getContext(), "摄像头打开失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            // 获得Camera对象
            Log.e(TAG, "==preparecamera==");
            camera = getCameraInstance();
            Log.e(TAG, "==cameraCreated==");
            if(camera==null){
                Toast.makeText(getContext(),"未开启摄像头权限", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//	updateCameraParameters();
        try {
            // 设置用于显示拍照摄像的SurfaceHolder对象
            camera.setPreviewDisplay(holder);
            Log.e(TAG, "==sset==");
        } catch (IOException e) {
            e.printStackTrace();
            // 释放手机摄像头
            camera.release();
            camera = null;
        }
        updateCameraParameters();
        if (camera != null) {
            Log.e(TAG, "==startPreview==");
            camera.startPreview();
        }
        //	setFocus();
    }

    // 在surface销毁时激发
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 释放手机摄像头
        if(camera!=null){
            camera.release();
            camera = null;
        }
    }

    // 在surface的大小发生改变时激发
    public void surfaceChanged(final SurfaceHolder holder, int format, int w,
                               int h) {


        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        updateCameraParameters();
        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();


        } catch (Exception e){
        }
    }

    /**
     * 点击显示焦点区域
     */
    OnTouchListener onTouchListener = new OnTouchListener() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int width = mFocusView.getWidth();
                int height = mFocusView.getHeight();
                mFocusView.setX(event.getX() - (width / 2));
                mFocusView.setY(event.getY() - (height / 2));
                mFocusView.beginFocus();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                focusOnTouch(event);
            }
            return true;
        }
    };
    public Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }
    private  float getScreenRate(Context context){
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H/W);
    }
    //相机参数的初始化设置

    private void setDispaly(Camera.Parameters parameters, Camera camera)
    {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8){
            camera.setDisplayOrientation(90);
        }
        else{
            parameters.setRotation(90);
        }

    }
    /**
     * 获取摄像头实例
     * @return
     */
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void updateCameraParameters() {
        if (camera != null) {
            Camera.Parameters p = camera.getParameters();
            if(p==null){
                return;
            }
            setParameters(p);
        }
    }

    /**
     * @param p
     */
    private void setParameters(Camera.Parameters p) {
        List<String> focusModes = p.getSupportedFocusModes();
        if (focusModes
                .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        //setDispaly(p,camera);
        p.set("orientation", "portrait");
        camera.setDisplayOrientation(90);
        long time = new Date().getTime();
        p.setGpsTimestamp(time);
        // 设置照片格式
        p.setPictureFormat(PixelFormat.JPEG);
        Camera.Size previewSize = findPreviewSizeByScreen(p);
        List<Camera.Size> sizeList = p.getSupportedPreviewSizes();
        for (Camera.Size size : sizeList) {
            Log.i(TAG,"预览尺寸 "+size.width+"*"+size.height);
        }
           Camera.Size bestSize = sizeList.get(0);
       /* for(int i = 0; i < sizeList.size(); i++){
            if(bestSize.width<sizeList.get(i).width)
                bestSize=sizeList.get(i);
            //if(sizeList.get(i).height==previewSize.height)
        }*/

        // TODO: 2017/1/12  修改选择和屏幕相同的预览尺寸
        final DisplayMetrics screenWH = CarmeraUtils.getScreenWH(getContext());
        for (Camera.Size size : sizeList) {
            if(size.height== screenWH.widthPixels&&size.width==screenWH.heightPixels){
                bestSize=size;
                break;
            }else if(bestSize.width<size.width){
                bestSize=size;
            }
        }
      /*  Camera.Size bestSize = sizeList.get(0);
        for(int i = 0; i < sizeList.size(); i++){
            if(bestSize.width<sizeList.get(i).width)
                bestSize=sizeList.get(i);
            //if(sizeList.get(i).height==previewSize.height)
        }*/
        DisplayMetrics screenWH1 = screenWH;
        Log.i(TAG,"屏幕尺寸 "+screenWH1.widthPixels+"*"+screenWH1.heightPixels);


//		p.setPreviewSize(bestSize.height, bestSize.width);
        Log.i(TAG,"选择的预览尺寸 "+bestSize.width+"*"+bestSize.height);
        p.setPreviewSize(bestSize.width, bestSize.height);// TODO: 2017/1/12
       // p.setPreviewSize(closelyPreSize.width,closelyPreSize.height);
//		List<Camera.Size> sizeList = p.getSupportedPreviewSizes();
//		Camera.Size bestSize = sizeList.get(0);
        List<Camera.Size> sizes = p.getSupportedPictureSizes();
        for (Camera.Size size : sizes) {
            Log.i(TAG,"图片尺寸 "+size.width+"*"+size.height);
        }
        Camera.Size realPicSize = null;
        //图片尺寸取中间的
        int i1 = (int) (sizes.size() / 2);
        Camera.Size size = sizes.get(i1);
       /* for (int i = 0; i < sizes.size(); i++) {
            if(sizes.get(i).width>size.width) {
              realPicSize = sizes.get(i);
             }
        }*/
        // TODO: 2017/1/13 方案一 ：取中间值
       // realPicSize=size;
       // Log.i(TAG,"选择的图片尺寸 "+bestSize.width+"*"+bestSize.height);
        // TODO: 2017/1/13 方案二 ：取比例接近屏幕的
        //realPicSize = getCloselySize(screenWH.widthPixels, screenWH.heightPixels, sizes);// TODO: 2017/1/13
         //// TODO: 2017/1/16 获取最大尺寸
         realPicSize=getLargeSupportedSize(sizes);
        Log.i(TAG,"选择的图片尺寸 "+realPicSize.width+"*"+realPicSize.height);
        //// TODO: 2017/1/12 bestsize 替换了 size
       // p.setPictureSize(size.width, size.height);
        // TODO: 2017/1/12  接近屏幕比的图片尺寸 
        p.setPictureSize(realPicSize.width, realPicSize.height);
        pictureSizeListener.onCameraGetSize(realPicSize.width,realPicSize.height);
        //p.setPictureSize(previewSize.width, previewSize.height);

//		p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//		p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//		camera.setDisplayOrientation(90);
        if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(90);
            p.setRotation(90);
        }
        camera.setParameters(p);
    }


    protected Camera.Size getCloselySize(int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> preSizeList) {
        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (surfaceWidth>surfaceHeight) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }
        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            // TODO: 2017/1/16  高度大于800 
            if ((deltaRatio < deltaRatioMin)) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    // 进行拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法
    public void takePicture() {
        if (camera != null) {
            try {
                camera.takePicture(null, null, pictureCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 设置监听事件
    public void setOnCameraStatusListener(OnCameraStatusListener listener) {
        this.listener = listener;
    }
    //设置监听事件
    public void setOnCameraGetPictureSizeListner(OnCameraPictureSizeListener onCameraGetPictureSizeListner){
        this.pictureSizeListener=onCameraGetPictureSizeListner;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    public void start() {
        if (camera != null) {
            camera.startPreview();
        }
    }

    public void stop() {
        if (camera != null) {
            camera.stopPreview();
        }
    }

    /**
     * 相机拍照监听接口
     */
    public interface OnCameraStatusListener {
        // 相机拍照结束事件
        void onCameraStopped(byte[] data);
    }

    /**
     * 获得相机拍照照片的参数的回调接口
     *
     */
    public interface OnCameraPictureSizeListener{
        void onCameraGetSize(int width,int height);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        viewWidth = MeasureSpec.getSize(widthSpec);
        viewHeight = MeasureSpec.getSize(heightSpec);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY));
    }



    /**
     * 将预览大小设置为屏幕大小
     * @param parameters
     * @return
     */
    private Camera.Size findPreviewSizeByScreen(Camera.Parameters parameters) {
        if (viewWidth != 0 && viewHeight != 0) {
            return camera.new Size(Math.max(viewWidth, viewHeight),
                    Math.min(viewWidth, viewHeight));
        } else {
//			Camera.Size size = new Camera.Size(CameraUtil.getScreenWH(getContext()).heightPixels,
//					CameraUtil.getScreenWH(getContext()).widthPixels);
            return null;
        }
    }

    /**
     * 获取最大尺寸
     * @param sizes
     * @return
     */
    private Camera.Size getLargeSupportedSize(List<Camera.Size> sizes) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    /**
     * 找到最合适的显示分辨率 （防止预览图像变形）
     * @param parameters
     * @return
     */
    private Camera.Size findBestPreviewSize(Camera.Parameters parameters) {

        // 系统支持的所有预览分辨率
        String previewSizeValueString = null;
        previewSizeValueString = parameters.get("preview-size-values");
        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }
        if (previewSizeValueString == null) { // 有些手机例如m9获取不到支持的预览大小 就直接返回屏幕大小
            return camera.new Size(CarmeraUtils.getScreenWH(getContext()).widthPixels,
                    CarmeraUtils.getScreenWH(getContext()).heightPixels);
        }
        float bestX = 0;
        float bestY = 0;
        float tmpRadio = 0;
        float viewRadio = 0;
        if (viewWidth != 0 && viewHeight != 0) {
            viewRadio = Math.min((float) viewWidth, (float) viewHeight)
                    / Math.max((float) viewWidth, (float) viewHeight);
        }
        String[] COMMA_PATTERN = previewSizeValueString.split(",");
        for (String prewsizeString : COMMA_PATTERN) {
            prewsizeString = prewsizeString.trim();
            int dimPosition = prewsizeString.indexOf('x');
            if (dimPosition == -1) {
                continue;
            }
            float newX = 0;
            float newY = 0;
            try {
                newX = Float.parseFloat(prewsizeString.substring(0, dimPosition));
                newY = Float.parseFloat(prewsizeString.substring(dimPosition + 1));
            } catch (NumberFormatException e) {
                continue;
            }
            float radio = Math.min(newX, newY) / Math.max(newX, newY);
            if (tmpRadio == 0) {
                tmpRadio = radio;
                bestX = newX;
                bestY = newY;
            } else if (tmpRadio != 0 && (Math.abs(radio - viewRadio)) < (Math.abs(tmpRadio - viewRadio))) {
                tmpRadio = radio;
                bestX = newX;
                bestY = newY;
            }
        }
        if (bestX > 0 && bestY > 0) {
            return camera.new Size((int) bestX, (int) bestY);
        }
        return null;
    }
    /**
     * 设置焦点和测光区域
     *
     * @param event
     */
    public void focusOnTouch(MotionEvent event) {
        int[] location = new int[2];
        RelativeLayout relativeLayout = (RelativeLayout)getParent();
        relativeLayout.getLocationOnScreen(location);
        Rect focusRect = CarmeraUtils.calculateTapArea(mFocusView.getWidth(),
                mFocusView.getHeight(), 1f, event.getRawX(), event.getRawY(),
                location[0], location[0] + relativeLayout.getWidth(), location[1],
                location[1] + relativeLayout.getHeight());
        Rect meteringRect = CarmeraUtils.calculateTapArea(mFocusView.getWidth(),
                mFocusView.getHeight(), 1.5f, event.getRawX(), event.getRawY(),
                location[0], location[0] + relativeLayout.getWidth(), location[1],
                location[1] + relativeLayout.getHeight());
        Camera.Parameters parameters = null;
        try {
            parameters = camera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(parameters==null){
            return;
        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));
            parameters.setFocusAreas(focusAreas);
        }
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));
            parameters.setMeteringAreas(meteringAreas);
        }
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
        }
        camera.autoFocus(this);
    }
    /**
     * 设置聚焦的图片
     * @param focusView
     */
    public void setFocusView(FocusView focusView) {
        this.mFocusView = focusView;
    }

    /**
     * 设置自动聚焦，并且聚焦的圈圈显示在屏幕中间位置
     */
/*	public void setFocus() {
		if(!mFocusView.isFocusing()) {
			try {
				camera.autoFocus(this);
				mFocusView.setX((CameraUtil.getWidthInPx(getContext())-mFocusView.getWidth()) / 2);
				mFocusView.setY((CameraUtil.getHeightInPx(getContext())-mFocusView.getHeight()) / 2);
				mFocusView.beginFocus();
			} catch (Exception e) {
			}
		}
	}*/
    /**
     * 通过设置Camera打开闪光灯
     * @param
     */
    public  void turnLightOn() {
        if (camera== null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        ;
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            } else {
            }
        }
    }
    /**
     * 通过设置Camera关闭闪光灯
     * @param
     */
    public  void turnLightOff() {
        if (camera == null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
            } else {
            }
        }
    }

}
