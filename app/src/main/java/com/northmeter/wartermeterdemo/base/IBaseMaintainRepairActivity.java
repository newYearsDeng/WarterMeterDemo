package com.northmeter.wartermeterdemo.base;

/**
 * @author zz
 * @time 2016/08/06 9:16
 * @des 维修维护的接口
 */
public interface IBaseMaintainRepairActivity {
    void showFailUploadMsg(String failString);
    void showSucceedUploadMsg(String succeedString);
    void showDialogProgress(int progress);
    void showDialogProgress0();
    void finishActivity();

    /**
     * 显示从数据找到保存好的图片
     * @param split
     */
    void showDBPicture(String[] split);
}

