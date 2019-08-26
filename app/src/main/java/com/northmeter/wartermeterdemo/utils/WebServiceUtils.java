package com.northmeter.wartermeterdemo.utils;


import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * @author zz
 * @time 2016/5/25 10:11
 * @des 获取后台WebService数据工具类
 */
public class WebServiceUtils {
    public static final int TIME_OUT = -1;
    public static final int ERROR = 0;
    public static final int SUCCESS = 1;

    public interface CallBack {
        String result(String result);
    }

    public static void getWebServiceInfo(final String methodName, final String[] paramNames, final String[] paramValues, final CallBack callBack) {
        // 用于子线程与主线程通信的Handler
        final Handler mHandler = new Handler(MyApplication.getContext().getMainLooper()) {//TODO  可能需要用Looper.loop;将它包住
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int what = msg.what;
                switch (what) {
                    case TIME_OUT:
                        //连接超时
                        System.out.println("连接超时  ----->>>  "+msg.obj);
                        callBack.result("连接异常");
                        break;
                    case ERROR:
                        //数据异常
                        System.out.println("数据异常--->>>" + msg.obj);
                        ToastUtil.showShort(MyApplication.getContext(),"后台访问异常，请稍候重试");
                        callBack.result("数据异常");
                        break;
                    case SUCCESS:
                        // 将返回值回调到callBack的参数中
                        callBack.result((String) msg.obj);
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 命名空间
                String nameSpace = "http://BDZPF981008.org/";
                // 调用的方法名称
//                String methodName = "GetCustomerInfo";
                // EndPoint
               // String endPoint = "http://218.17.157.121:9147/Service1.asmx";
                String endPoint = Constants.Host+"Service1.asmx";
                // SOAP Action
                final String soapAction = nameSpace + methodName;//nameSpace+methodName
                // 建立webservice连接对象，并设置超时时长
                final HttpTransportSE transport = new HttpTransportSE(endPoint, 5 * 1000);
                //transport.debug = true;// 是否是调试模式
                // 设置连接参数
                SoapObject soapObject = new SoapObject(nameSpace, methodName);
//                soapObject.addProperty(paramNames, paramValues);
                for (int i = 0; i < paramNames.length; i++) {
                    soapObject.addProperty(paramNames[i], paramValues[i]);
                }
                // 设置返回参数
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);// soap协议版本必须用SoapEnvelope.VER11（Soap V1.1）
                envelope.dotNet = true;// 注意：这个属性是对dotnetwebservice协议的支持,如果dotnet的webservice需要设置成true
                envelope.bodyOut = soapObject;//千万注意！！
                envelope.setOutputSoapObject(soapObject);// 设置请求参数
                try {
                    transport.call(soapAction, envelope);// 调用WebService
                } catch (Exception e) {
                    mHandler.sendMessage(mHandler.obtainMessage(TIME_OUT, e.getMessage()));
                    return;
                }
                if (envelope.bodyIn instanceof SoapFault) {
                    SoapFault error = (SoapFault) envelope.bodyIn;
                    // 将异常的消息利用Handler发送到主线程
                    mHandler.sendMessage(mHandler.obtainMessage(ERROR, error.toString()));
                } else {
                    SoapObject object = (SoapObject) envelope.bodyIn;// 获取返回的数据
                    // 将结果利用Handler发送到主线程
                    mHandler.sendMessage(mHandler.obtainMessage(SUCCESS, object.getProperty(0).toString()));
                }
            }
        }).start();
    }


}
