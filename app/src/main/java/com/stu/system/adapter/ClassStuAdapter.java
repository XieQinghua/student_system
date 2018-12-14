package com.stu.system.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stu.system.R;
import com.stu.system.activity.SaveHistoryActivity;
import com.stu.system.activity.StuHisListActivity;
import com.stu.system.bean.GetClassStuBean;
import com.stu.system.common.Constants;
import com.stu.system.util.SPUtils;

import java.util.List;

public class ClassStuAdapter extends CommonAdapter<GetClassStuBean.ValueBean> {

    public ClassStuAdapter(Context context, List<GetClassStuBean.ValueBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public ClassStuAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void setData(List<GetClassStuBean.ValueBean> datas) {
        super.setData(datas);
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, final GetClassStuBean.ValueBean bean, final int position) {
        SimpleDraweeView sdvStuImg = holder.getView(R.id.sdv_stu_img);
        TextView stuName = holder.getView(R.id.tv_stu_name);
        TextView edit = holder.getView(R.id.tv_edit);
        TextView getLog = holder.getView(R.id.tv_get_log);

        ViewGroup.LayoutParams para = sdvStuImg.getLayoutParams();
        para.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(29)) * 1 / 2;
        para.height = para.width * 3 / 4;
        //http://fuck.biegral.com/uploads/stuheadimg/b1aac8bc-300e-47a5-baad-c8d13776dd3f.jpg
        String imgUrl = SPUtils.getInstance().getString(Constants.HOST, "") +
                "/uploads/stuheadimg/" +
                bean.getSid() + ".jpg";
        sdvStuImg.setImageURI(Uri.parse(imgUrl));
        stuName.setText(bean.getSname());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加学生上课日志
                Intent intent = new Intent(mContext, SaveHistoryActivity.class);
                intent.putExtra("sid", bean.getSid());
                intent.putExtra("sname", bean.getSname());
                mContext.startActivity(intent);
            }
        });
        getLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取学生日志
                Intent intent = new Intent(mContext, StuHisListActivity.class);
                intent.putExtra("sid", bean.getSid());
                intent.putExtra("sname", bean.getSname());
                mContext.startActivity(intent);
            }
        });

    }
}