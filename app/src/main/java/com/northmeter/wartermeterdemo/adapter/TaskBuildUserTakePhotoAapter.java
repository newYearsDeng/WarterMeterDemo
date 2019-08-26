package com.northmeter.wartermeterdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.TaskBuildUserActivity;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.OnItemClickListener;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * @author zz
 * @time 2016/09/06 14:43
 * @des 立户任务拍照的adapter
 */
public class TaskBuildUserTakePhotoAapter extends RecyclerView.Adapter<TaskBuildUserTakePhotoAapter.TakePhotoViewholder> {


    private final TaskBuildUserActivity mBuildUserActivity;
    private final ArrayList<String> mPhotosPathData;
    private OnItemClickListener mOnItemClickListener;

    public TaskBuildUserTakePhotoAapter(TaskBuildUserActivity taskBuildUserActivity, ArrayList<String> photoUri) {
        this.mBuildUserActivity = taskBuildUserActivity;
        this.mPhotosPathData = photoUri;
    }

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.mOnItemClickListener = OnItemClickListener;
    }

    @Override
    public TakePhotoViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mBuildUserActivity).inflate(R.layout.item_build_user_take_photo, parent, false);
        return new TakePhotoViewholder(view);
    }

    @Override
    public void onBindViewHolder(final TakePhotoViewholder holder, final int position) {
        Picasso.with(mBuildUserActivity)
                .load(new File(mPhotosPathData.get(position)))
                .resize(120, 120)
                .centerCrop()
                .into(holder.mIvPhoto);

//        holder.mS dvPhoto.setImageURI(mPhoto UriData.get(position));


        //recyclerView条目点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPhotosPathData.size();
    }

    class TakePhotoViewholder extends RecyclerView.ViewHolder {

        private final ImageView mIvPhoto;

        public TakePhotoViewholder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_item_photo);
        }
    }
}
