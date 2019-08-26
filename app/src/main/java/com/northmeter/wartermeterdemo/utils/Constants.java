package com.northmeter.wartermeterdemo.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author zz
 * @time 2016/5/11 10:05
 * @des 常量类
 */
public class Constants {

    //立户的常量字符串
    public static String EXTRA_BUILD_USER = "extra_build_user";
    public static String BUILD_USER = "build_user";

    //个人事务的常量字符串
    public static String EXTRA_TASK_BUILD_USER = "extra_task_build_user";
    public static String TASK_BUILD_USER = "task_build_user";
    public static String EXTRA_TASK_MAINTAIN = "extra_task_maintain";
    public static String TASK_MAINTAIN = "task_maintain";
    public static String EXTRA_TASK_REPAIR = "extra_task_repair";
    public static String TASK_REPAIR = "task_repair";
    public static String EXTRA_TASK_METER = "extra_task_meter";
    public static String TASK_METER = "task_meter";

    public static final int PHOTOGRAPH = 0x0000010;

    public static final int PHOTOGRAPH_ANALYSIS = 0x0000013;
    /**
     * 图片解析接口
     * 9/13
     */
    //public static final String ANALYSISNUM = "http://120.76.157.200:4567/water/getDigit";
    // public static final String ANALYSISNUM = "http://120.76.220.251:4567/water_ssd/getDigit";
    //public static final String ANALYSISNUM = "http://120.76.220.251:5678/water/mobile/getDigit"
            //todo 1/16
    //public static final String ANALYSISNUM = "http://120.76.220.251:4567/water/mobile/getDigit";
            //todo 2017 3/17
      public static final String ANALYSISNUM = "http://120.76.220.251:5678/water/mobile/getDigit";

    //https://cloud-meters.net/water/mobile/getDigit
    //https://cloud-meters.net/water/hardware/getDigit
    /**
     * 服务器IP地址
     */
    public static final String Host = "http://218.17.157.121:9147/";
    /**
     * 图片存储路径
     */
    public static final String SAVEPIC = Environment.getExternalStorageDirectory() + File.separator + "com.watermeter.android/detect/";

    public static final String METHOD_UPLOAD_CUSTOMER_INFO = "UpLoadCustomerInfo"; //注册用户接口方法名
    public static final String METHOD_LOGIN_CHECK = "LoginCheck";//登录接口方法名
    public static final String METHOD_GET_CUSTOMER_USE_RECORD = "GetCustomerUseRecord";//获取水表月份账单
    public static final String METHOD_NAME_DOWNLOADTASK = "DownLoadTask";//下载数据的方法名
    public static final String METHOD_NAME_UPLOADTASK = "UpLoadTask";//上传数据的方法名
    public static final String METHOD_NAME_GETALLUSERINFO = "GetAllUserInfo";//用户信息详情
    public static final String METHOD_NAME_GETPASSWORDPROBLEM = "GetPasswordProblem";//验证手机号获取密保
    public static final String METHOD_NAME_VERIFYPASSWORDPROBLEM = "VerifyPasswordProblem";//重置密码

    public static final String PARAM_CUSTOMER_TELPHONE = "CUSTOMER_TELPHONE"; //认证用户手机号码
    public static final String PARAM_CUSTOMER_PWD = "CUSTOMER_PWD"; //确认密码
    public static final String PARAM_CUSTOMER_ADDR = "CUSTOMER_ADDR"; //用户地址
    public static final String PARAM_CUSTOMER_WATERID = "CUSTOMER_WATERID"; //水表编号
    public static final String PARAM_CUSTOMER_GASID = "CUSTOMER_GASID"; //燃气编号
    public static final String PARAM_CUSTOMER_NAME = "CUSTOMER_NAME"; //燃气编号
    public static final String PARAM_PWD_PROBLEM_1 = "PasswordProblem1";//密保问题1
    public static final String PARAM_PWD_PROBLEM_2 = "PasswordProblem2";//密保问题2
    public static final String PARAM_PWD_PROBLEM_3 = "PasswordProblem3";//密保问题3
    public static final String PARAM_PWD_ANSWER_1 = "PasswordAnswer1";//密保答案1
    public static final String PARAM_PWD_ANSWER_2 = "PasswordAnswer2";//密保答案2
    public static final String PARAM_PWD_ANSWER_3 = "PasswordAnswer3";//密保答案3
    public static final String PARAM_PASSWORD = "PassWord";//重置密码
    public static final String PARAM_PASSWORDPROBLEM = "PasswordProblem";//重置密码 密保问题
    public static final String PARAM_PASSWORDANSWER = "PasswordAnswer";//重置密码 密保答案

    public static final String PARAM_LOGIN_NAME = "Login_name"; //登录用户名
    public static final String PARAM_LOGIN_PWD = "Login_pwd"; //登录密码
    public static final String PARAM_CUSTOMER_ID = "CUSTOMER_ID";//客户代码
    public static final String PARAM_WATER_CUSTOMERID = "WATER_CUSTOMERID";//水表子用户号
    public static final String PARAM_GAS_CUSTOMERID = "GAS_CUSTOMERID";//燃气表子用户号
    public static final String PARAM_TASKID = "TaskID";//任务ID
    public static final String PARAM_TASKTYPE = "TaskType";//任务类型
    public static final String PARAM_TASKRETURN = "TaskReturn";//任务反馈内容
    public static final String PARAM_TASKPIC1 = "TaskPic1";//任务图片1
    public static final String PARAM_TASKPIC2 = "TaskPic2";//任务图片2
    public static final String PARAM_TASKPIC3 = "TaskPic3";//任务图片3
    public static final String PARAM_TASKPIC4 = "TaskPic4";//任务图片4
    public static final String PARAM_TASKPIC1NAME = "TaskPic1Name";//任务图片名称1
    public static final String PARAM_TASKPIC2NAME = "TaskPic2Name";//任务图片名称2
    public static final String PARAM_TASKPIC3NAME = "TaskPic3Name";//任务图片名称3
    public static final String PARAM_TASKPIC4NAME = "TaskPic4Name";//任务图片名称4
    public static final String PARAM_CUSTOMERNAME = "CustomerNAME";//用户名称
    public static final String PARAM_CUSTOMERADDR = "CustomerADDR";//用户地址
    public static final String PARAM_CUSTOMERTEL = "CustomerTel";//用户手机
    public static final String PARAM_METERCODE = "MeterCode";//水表编号
    public static final String PARAM_TASKOPERATEDATE = "TaskOperateDate";//任务处理时间
    public static final String PARAM_LOGINNAME = "LoginName";//查询所有用户月份用水信息的登录名

    public static final String SEARCH_MONTH_MESSAGE = "search_month_message";//用水查询月份信息标题名
    public static final String SEARCH_MSG_USEDATE = "USEDATE";//用水信息结算月份
    public static final String SEARCH_MSG_STARTVALUE = "STARTVALUE";//用水信息上月表底
    public static final String SEARCH_MSG_ENDVALUE = "ENDVALUE";//用水信息本月表底
    public static final String SEARCH_MSG_USEVALUE = "USEVALUE";//用水信息结算使用值
    public static final String SEARCH_MSG_USEMONEY = "USEMONEY";//用水信息结算金额
    public static final String SEARCH_MSG_BASISPRICE = "BASISPRICE";//用水信息单价

    //查询所有用户月份用水信息
    public static final String INTENT_EXTRA_CLEARINGVALUETOTAL = "clearingValueTotal";
    public static final String INTENT_EXTRA_BASISPRICE = "basisPrice";
    public static final String INTENT_EXTRA_CUSTOMERADDRESS = "customerAddress";
    public static final String INTENT_EXTRA_ECMONEYSUMTOTAL = "ecMoneySumTotal";
    public static final String INTENT_EXTRA_EDATAITEMVALUE = "eDataItemValue";
    public static final String INTENT_EXTRA_SDATAITEMVALUE = "sDataItemValue";
    public static final String INTENT_EXTRA_CUSTOMERNAME = "username";
    public static final String INTENT_EXTRA_CUSTOMERID = "customerid";
    public static final String INTENT_EXTRA_MONTH_TITLE = "monthtitle";

    public static final String SP_NAME_KEY = "userName";
    public static final String SP_PASSWORD_KEY = "password";
    public static final String SP_ISMEMORY_KEY = "isMemory";
    public static final String LOGINEDACTIVITY = "loginedActivity";//登陆成功的activity
    public static final String SP_GET_CUSTOMER_NAME = "getCUSTOMER_NAME";//客户编号id
    public static final String SP_GET_CUSTOMER_ID = "getCUSTOMER_ID";//客户编号id
    public static final String SP_GET_CUSTOMER_ADDR = "getCUSTOMER_ADDR";//客户地址
    public static final String SP_GET_WATER_CUSTOMERID = "getWATER_CUSTOMERID";//水表子用户号

    public static final String JSON_RESPONSEXML = "RESPONSEXML";//json标题
    //+apkName；apk更新地址；
    public static final String UPDATESOFTADDRESS = Host+"APPAction/";
    public static final String VERSIONNUM = Host + "Service1.asmx/VersionNumber?";


    //    public static final String PARAM_LOGIN_NAME = "Login_name";//操作员账号
    public static final String PUT_EXTRA_TASK_NAME = "put_extra_task_name";//任务的用户名
    public static final String PUT_EXTRA_TASK_DETAILS = "put_extra_task_details";//任务详情
    public static final String PUT_EXTRA_TASK_ID = "put_extra_task_id";//任务ID
    public static final String PUT_EXTRA_TASK_ADDRESS = "put_extra_task_address";//用户地址
    public static final String PUT_EXTRA_TASK_METER_CODE = "put_extra_task_meter_code";//水表编号
    public static final String PUT_EXTRA_TASK_PHONE_NUMBER = "put_extra_task_phone_number";//手机号码
    public static final String PUT_EXTRA_TASK_CUSTOMER_ID = "put_extra_task_customer_id";//用户ID
    public static final String PUT_EXTRA_TASK_AREA_NAME = "put_extra_task_area_name";//区域


    public static final String TASK_TYPE_BUILDER_USER = "立户";//立户任务类型
    public static final String TASK_TYPE_MAINTAIN = "维护";//维护"任务类型
    public static final String TASK_TYPE_REPAIR = "维修";//维修任务类型
    public static final String TASK_TYPE_MERTER = "抄表";//抄表任务类型
    public static final String NOT_FINISH = "未完成";//任务是否完成
    public static final String NOT_UPLOAD = "未上传";//任务是否上传
    public static final String ALREADY_UPLOAD = "已上传";//任务是否上传
    public static final String ALREADY_FINISH = "已完成";//任务是否完成

    public static final String TAKEPHOTOSRC = "takePhotoFrom";
    public static final String ANALYSISSRC = "analysisRrc";

    public static final String SERVICE_BROADCAST_ACTION = "printService";
    public static final String ACTIVITY_BROADCAST_ACTION = "printActivity";

    public static final String SECLETBLUETOOTHMAC = "默认的设备mac";
    public static final String SECLETBLUETOOTHNAME = "默认的设备名";

    public static final String AD_LINKEDURL = "ad_linkedUrl";

    /**
     * 开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
     * 服务端生成charge 的方式可以参考ping++官方文档，地址 https://pingxx.com/guidance/server/import
     * <p>
     * 【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url 。
     * 该 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
     */
    //public  static String YOUR_URL ="http://218.244.151.190/demo/charge";
    //Service1.asmx/CreateCharge?amount=string&subject=string&body=string&client_ip=string HTTP/1.1
    public static String YOUR_URL = Host + "Service1.asmx/CreateCharge";


    public static String APP_ID_WX="wx4df958fd3b1fef08";
}
