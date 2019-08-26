package com.northmeter.wartermeterdemo.activity;

/**
 * @author zz
 * @time 2016/09/06 8:51
 * @des 立户详情列表的view
 */
public interface ITaskBuildUserActivity {
    /**
     * toast弹窗信息
     */
    void toastMsg(String toastStr);

    /**
     * 显示dialog
     */
    void showDialogProgress(int progress);
    void showDialogProgress0();

    /**
     * 关掉activity
     */
    void finishAvtivity();

    /**
     * 显示从数据找到保存好的图片
     * @param split
     */
    void showDBPicture(String[] split);

}
