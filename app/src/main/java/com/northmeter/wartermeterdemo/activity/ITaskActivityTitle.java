package com.northmeter.wartermeterdemo.activity;

import android.support.v4.app.Fragment;

import com.northmeter.wartermeterdemo.fragment.TaskBuildUserFragment;

/**
 * @author zz
 * @time 2016/08/11 11:01
 * @des 任务列表标题栏和多选状态标题栏的接口
 */
public interface ITaskActivityTitle {
    /**
     * 隐藏任务标题栏
     */
    void hideTasksTitle();

    /**
     * 显示任务标题栏
     */
    void showTasksTitle();

    /**
     * 隐藏多选状态标题栏
     */
    void hideMultiTitle();

    /**
     * 显示多选状态标题栏
     */
    void showMultiTitle();

    /**
     * 显示多选的选中数量
     */
    void showMultiAmount(int amount);

    /**
     * 取消按钮
     */
    void cancelButton();

    /**
     * 删除按钮
     */
    void deleteButton();


}
