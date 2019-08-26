package com.northmeter.wartermeterdemo.model;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.bean.GetCustomerInfoBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import java.util.List;

/**
 * @author zz
 * @time 2016/09/08 9:30
 * @des 注册的model
 */
public class RegistModel implements IRegistModel {
    private static final String RESCODE_SUCCESS = "1";//成功的返回码
    private static final String RESCODE_FAIL = "0";//失败的返回码
    private static final String NOT_ACTIVATE_STATE = "未激活";
    private static final String ALREADY_ACTIVATE_STATE = "成功";
    private static final String NO_FIND_STATE = "不存在";
    private static final String ENTRY_PARAM1_FAIL = "参数格式错误";
    private static final String ENTRY_PARAM2_FAIL = "手机号码不正确";
    private static final String CONNECT_FAIL = "连接异常";
//    private boolean isActivate = false;//是否激活
//    private boolean isNotFind = false;//手机号码是否存在，默认存在

    @Override
    public void networkVerifyPhone(String[] paramNames, String[] paramValues, final RegistCallback registCallback) {
        //访问网络
        WebServiceUtils webServiceUtils = new WebServiceUtils();
        webServiceUtils.getWebServiceInfo("GetCustomerInfo", paramNames,
                paramValues, new WebServiceUtils.CallBack() {
                    @Override
                    public String result(final String result) {
                        LoggerUtil.json(result);
                        if (CONNECT_FAIL.equals(result)) {
                            registCallback.showToast("访问服务器异常，请稍后重试");
                            registCallback.dismissDialog();
                            return result;
                        }
                        //解析json字符串
                        Gson gson = new Gson();
                        GetCustomerInfoBean getCustomerInfoBean = gson.fromJson(result.toString(), GetCustomerInfoBean.class);
                        final List<GetCustomerInfoBean.RESPONSEXMLBean> infos = getCustomerInfoBean.getRESPONSEXML();

                        final GetCustomerInfoBean.RESPONSEXMLBean responseCode = infos.get(0);//第一次返回的状态码

                        if (RESCODE_FAIL.equals(responseCode.getRESCODE())) {
                            registCallback.showToast("无法访问后台数据");
                        } else if (RESCODE_SUCCESS.equals(responseCode.getRESCODE())) {
                            //手机信息匹配成功

                            if (NOT_ACTIVATE_STATE.equals(responseCode.getRESMSG())) {
                                //未激活
//                                isActivate = false;//修改激活标记
//                                isNotFind = false;//修改存在的标记
                                registCallback.setFlag(false,false);
                                registCallback.setUserInfo(infos);
                                registCallback.showToast("该手机号还没注册，可以完成注册");
                            } else if (NO_FIND_STATE.equals(responseCode.getRESMSG())) {
                                //不存在
//                                isActivate = false;//修改激活标记
//                                isNotFind = true;//修改存在的标记
                                registCallback.setFlag(false,true);
                                registCallback.showToast("该手机号未在后台登记录入，不能注册");
                            } else if (ALREADY_ACTIVATE_STATE.equals(responseCode.getRESMSG())) {
                                //已激活
//                                isActivate = true;//修改激活标记
//                                isNotFind = false;//修改存在的标记
                                registCallback.setFlag(true,false);
                                registCallback.showToast("该手机号已经注册过了");
                            } else if (ENTRY_PARAM1_FAIL.equals(responseCode.getRESMSG())) {
                                //输入的手机号码有误
//                                isActivate = false;//修改激活标记
//                                isNotFind = true;//修改存在的标记
                                registCallback.setFlag(false,true);
                                registCallback.showToast("输入手机号码有误，请检查后重新输入");
                            } else if (ENTRY_PARAM2_FAIL.equals(responseCode.getRESMSG())) {
                                //输入的参数有误
//                                isActivate = false;//修改激活标记
//                                isNotFind = true;//修改存在的标记
                                registCallback.setFlag(false,true);
                                registCallback.showToast("输入的手机格式有误，请检查后重新输入");
                            } else {
//                                isActivate = false;//修改激活标记
//                                isNotFind = true;//修改存在的标记
                                registCallback.setFlag(false,true);
                                registCallback.showToast("无法注册，请检查后重试");
                            }
                        }
                        registCallback.dismissDialog();
                        return result;
                    }
                });
    }

    @Override
    public void networkConfirmRegist(String[] paramNames, String[] paramValues, final RegistCallback registCallback) {

        //解析接口
        WebServiceUtils.getWebServiceInfo(Constants.METHOD_UPLOAD_CUSTOMER_INFO,
                paramNames, paramValues, new WebServiceUtils.CallBack() {
                    @Override
                    public String result(String result) {
                        LoggerUtil.json(result);
                        //解析json
                        final GetCustomerInfoBean.RESPONSEXMLBean responseCode = getJsonString(result);

                        if (RESCODE_FAIL.equals(responseCode.getRESCODE())) {
                            registCallback.showToast("注册失败");
                        } else if (RESCODE_SUCCESS.equals(responseCode.getRESCODE())) {
                            registCallback.showToast("注册成功");
//                            isCheck = false;//修改验证标记
                            registCallback.setIsCheck(false);
                            registCallback.finishView();
                        }
                        return result;
                    }
                });
    }

    /**
     * 获得Json字符串
     *
     * @param result
     * @return
     */
    private GetCustomerInfoBean.RESPONSEXMLBean getJsonString(String result) {
        Gson gson = new Gson();
        GetCustomerInfoBean getCustomerInfoBean = gson.fromJson(result.toString(), GetCustomerInfoBean.class);
        final List<GetCustomerInfoBean.RESPONSEXMLBean> infos = getCustomerInfoBean.getRESPONSEXML();
        return infos.get(0);
    }

    public interface RegistCallback {
        void dismissDialog();

        void showToast(String toastStr);

        void setUserInfo(List<GetCustomerInfoBean.RESPONSEXMLBean> infos);

        void finishView();

        void setIsCheck(boolean isCheck);

        void setFlag(boolean isActivate,boolean isNotFind);
    }
}
