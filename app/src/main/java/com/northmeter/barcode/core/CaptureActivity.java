package com.northmeter.barcode.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.northmeter.barcode.camera.CameraManager;
import com.northmeter.barcode.executor.ResultHandler;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.BuildUserActivity;
import com.northmeter.wartermeterdemo.activity.TaskBuildUserActivity;
import com.northmeter.wartermeterdemo.activity.TaskMaintainActivity;
import com.northmeter.wartermeterdemo.activity.TaskMeterActivity;
import com.northmeter.wartermeterdemo.activity.TaskRepairActivity;
import com.northmeter.wartermeterdemo.activity.TaskScanBuildUserActivity;
import com.northmeter.wartermeterdemo.activity.TaskScanMaintainActivity;
import com.northmeter.wartermeterdemo.activity.TaskScanMeterActivity;
import com.northmeter.wartermeterdemo.activity.TaskScanRepairActivity;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

public final class CaptureActivity extends AutoLayoutActivity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private TextView statusView;
    private LinearLayout common_title_TV_left;
    private TextView common_title_TV_right;
    private Result lastResult;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private TitleView title;
    //private Button from_gallery;
    private final int from_photo = 010;
    static final int PARSE_BARCODE_SUC = 3035;
    static final int PARSE_BARCODE_FAIL = 3036;
    String photoPath;
    ProgressDialog mProgress;
    private String mStrBuildUser;
    private String mStrTaskBuildUser;
    private String mStrTaskMaintain;
    private String mStrTaskRepair;
    private String mStrTaskMeter;


    // Dialog dialog;

    enum IntentSource {

        ZXING_LINK, NONE

    }

    Handler barHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    //viewfinderView.setRun(false);
                    showDialog((String) msg.obj);
                    break;
                case PARSE_BARCODE_FAIL:
                    //showDialog((String) msg.obj);
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    new AlertDialog.Builder(CaptureActivity.this).setTitle("提示").setMessage("扫描失败！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(@Nullable Bundle icicle) {
        super.onCreate(icicle);

        //获取传递过来的字符串
        mStrBuildUser = getIntent().getStringExtra(Constants.EXTRA_BUILD_USER);
        mStrTaskBuildUser = getIntent().getStringExtra(Constants.EXTRA_TASK_BUILD_USER);
        mStrTaskMaintain = getIntent().getStringExtra(Constants.EXTRA_TASK_MAINTAIN);
        mStrTaskRepair = getIntent().getStringExtra(Constants.EXTRA_TASK_REPAIR);
        mStrTaskMeter = getIntent().getStringExtra(Constants.EXTRA_TASK_METER);

        // Window window = getWindow();
        // window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        statusView = (TextView) findViewById(R.id.status_view);
        common_title_TV_left = (LinearLayout) findViewById(R.id.common_title_TV_left);
//		common_title_TV_right = (TextView) findViewById(R.id.common_title_TV_right);
        //title = (TitleView) findViewById(R.id.decode_title);
        //from_gallery = (Button) findViewById(R.id.from_gallery);
        // 为标题和底部按钮添加监听事件
        setListeners();
    }

    public void showDialog(final String msg) {

        //根据传递过来不同的类型，跳转到不同的页面
        if (Constants.BUILD_USER.equals(mStrBuildUser)) {//立户界面
            Intent intent = new Intent(MyApplication.getContext(), BuildUserActivity.class);
            intent.putExtra("qr_code", msg);
            startActivity(intent);
            finish();
        } else if (Constants.TASK_BUILD_USER.equals(mStrTaskBuildUser)) {//立户任务界面
            Intent intent = new Intent(MyApplication.getContext(), TaskScanBuildUserActivity.class);
            intent.putExtra("qr_code", msg);
            startActivity(intent);
            finish();
        } else if (Constants.TASK_MAINTAIN.equals(mStrTaskMaintain)) {//维护任务界面
            Intent intent = new Intent(MyApplication.getContext(), TaskScanMaintainActivity.class);
            intent.putExtra("qr_code", msg);
            startActivity(intent);
            finish();
        }else if (Constants.TASK_REPAIR.equals(mStrTaskRepair)) {//维修任务界面
            Intent intent = new Intent(MyApplication.getContext(), TaskScanRepairActivity.class);
            intent.putExtra("qr_code", msg);
            startActivity(intent);
            finish();
        }else if (Constants.TASK_METER.equals(mStrTaskMeter)) {//抄表任务界面
            Intent intent = new Intent(MyApplication.getContext(), TaskScanMeterActivity.class);
            intent.putExtra("qr_code", msg);
            startActivity(intent);
            finish();
        }


//		if (mProgress != null && mProgress.isShowing()) {
//			mProgress.dismiss();
//		}
//		if(msg.startsWith("http")){
//			new AlertDialog.Builder(CaptureActivity.this).setTitle(getString(R.string.memo)).
//			setMessage(String.format(getString(R.string.barcode_tow_dimen_success), msg)).
//			setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					//	String testUrl = "http://www.on-con.com/oncon/pages/download.jsp";
//						Intent intent = new Intent();
//						intent.setAction("android.intent.action.VIEW");
//						Uri content_url = Uri.parse(msg);
//						intent.setData(content_url);
//						startActivity(intent);
//						finish();
//				}
//			}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
//						restartPreviewAfterDelay(0L);
//					}
//				}
//			}).show();
//			}else{
//				new AlertDialog.Builder(CaptureActivity.this).setTitle(getString(R.string.memo)).
//				setMessage(String.format(getString(R.string.barcode_one_dimen_success), msg)).
//				setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						//获取剪贴板管理服务
//						ClipboardManager cm =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//						//将文本数据复制到剪贴板
//						cm.setText(msg);
//						//读取剪贴板数据
//						//cm.getText();
//
//					}
//				}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
//							restartPreviewAfterDelay(0L);
//						}
//					}
//				}).show();
//			}

    }

    public void setListeners() {
        common_title_TV_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });

//		common_title_TV_right.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				StringUtils.showPictures(CaptureActivity.this, from_photo);
//			}
//		});
    }

    public String parsLocalPic(String path) {
        String parseOk = null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        // 缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader2 = new QRCodeReader();
        Result result;
        try {
            result = reader2.decode(bitmap1, hints);
            Log.i("steven", "result:" + result);
            parseOk = result.getText();

        } catch (NotFoundException e) {
            parseOk = null;
        } catch (ChecksumException e) {
            parseOk = null;
        } catch (FormatException e) {
            parseOk = null;
        }
        return parseOk;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("steven", "data.getData()" + data);
        if (data != null) {
            mProgress = new ProgressDialog(CaptureActivity.this);
            mProgress.setMessage("正在扫描...");
            mProgress.setCancelable(false);
            mProgress.show();
            final ContentResolver resolver = getContentResolver();
            if (requestCode == from_photo) {
                if (resultCode == RESULT_OK) {
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            String result = parsLocalPic(photoPath);
                            if (result != null) {
                                Message m = Message.obtain();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result;
                                barHandler.sendMessage(m);
                            } else {
                                Message m = Message.obtain();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = "扫描失败！";
                                barHandler.sendMessage(m);
                            }
                            Looper.loop();
                        }
                    }).start();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = null;
        lastResult = null;
        resetStatusView();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        decodeFormats = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        if (mProgress != null) {
            mProgress.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 这里初始化界面，调用初始化相机
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }

    // 解析二维码
    public void handleDecode(Result rawResult, Bitmap barcode) {
        inactivityTimer.onActivity();
        lastResult = rawResult;

        ResultHandler resultHandler = new ResultHandler(parseResult(rawResult));

        boolean fromLiveScan = barcode != null;
        if (barcode == null) {
            Log.d("steven", "rawResult.getBarcodeFormat().toString():" + rawResult.getBarcodeFormat().toString());
            Log.d("steven", "resultHandler.getType().toString():" + resultHandler.getType().toString());
            Log.d("steven", "resultHandler.getDisplayContents():" + resultHandler.getDisplayContents());
        } else {

            Log.d("steven", "rawResult.getBarcodeFormat().toString():" + rawResult.getBarcodeFormat().toString());
            Log.d("steven", "resultHandler.getType().toString():" + resultHandler.getType().toString());
            Log.d("steven", "resultHandler.getDisplayContents():" + resultHandler.getDisplayContents());

            showDialog(resultHandler.getDisplayContents().toString());
        }
    }

    // 初始化照相机，CaptureActivityHandler解码
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton("确定", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        statusView.setText(R.string.msg_default_status);
        statusView.setVisibility(View.VISIBLE);

        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

}
