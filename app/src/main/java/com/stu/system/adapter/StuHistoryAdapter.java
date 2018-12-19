package com.stu.system.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.tools.ScreenUtils;
import com.stu.system.R;
import com.stu.system.activity.ImgPreviewActivity;
import com.stu.system.activity.VideoPlayActivity;
import com.stu.system.bean.BaseBean;
import com.stu.system.bean.GetStuHisListBean;
import com.stu.system.common.Constants;
import com.stu.system.http.Api;
import com.stu.system.http.ApiLoader;
import com.stu.system.http.SimpleCallback;
import com.stu.system.util.DialogUtil;
import com.stu.system.util.DrawableUtils;
import com.stu.system.util.SPUtils;

import java.util.List;

public class StuHistoryAdapter extends CommonAdapter<GetStuHisListBean.ValueBean> {

    public StuHistoryAdapter(Context context, List<GetStuHisListBean.ValueBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public StuHistoryAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void setData(List<GetStuHisListBean.ValueBean> datas) {
        super.setData(datas);
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, final GetStuHisListBean.ValueBean bean, final int position) {

        TextView hisName = holder.getView(R.id.tv_his_name);
        TextView hisInfo = holder.getView(R.id.tv_his_info);
        TextView del = holder.getView(R.id.tv_del);
        CardView cv1 = holder.getView(R.id.cv1);
        SimpleDraweeView sdv_img1 = holder.getView(R.id.sdv_img1);
        //ImageView playVideo1 = holder.getView(R.id.iv_play_video1);

        CardView cv2 = holder.getView(R.id.cv2);
        SimpleDraweeView sdv_img2 = holder.getView(R.id.sdv_img2);
        ImageView playVideo2 = holder.getView(R.id.iv_play_video2);

        CardView cv3 = holder.getView(R.id.cv3);
        SimpleDraweeView sdv_img3 = holder.getView(R.id.sdv_img3);
        ImageView playVideo3 = holder.getView(R.id.iv_play_video3);

        CardView cv4 = holder.getView(R.id.cv4);
        SimpleDraweeView sdv_img4 = holder.getView(R.id.sdv_img4);
        ImageView playVideo4 = holder.getView(R.id.iv_play_video4);

        hisName.setText(bean.getTitle());

        int orangeColor = Color.parseColor("#FF6600");
        int tranOrangeColor = Color.parseColor("#FF6600") & 0x80FFFFFF;
        int whiteColor = Color.parseColor("#FFFFFF");
        int tranWhiteColor = Color.parseColor("#FFFFFF") & 0x80FFFFFF;
        GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, tranWhiteColor, SizeUtils.dp2px(10), SizeUtils.dp2px(1), tranOrangeColor);
        GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, whiteColor, SizeUtils.dp2px(10), SizeUtils.dp2px(1), orangeColor);
        StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        del.setBackground(selector);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelDialog(bean.getId(), position);
            }
        });
        if (!TextUtils.isEmpty(bean.getIntro())) {
            hisInfo.setText(bean.getIntro());
            hisInfo.setVisibility(View.VISIBLE);
        } else {
            hisInfo.setVisibility(View.GONE);
        }

        String url = SPUtils.getInstance().getString(Constants.HOST, "");
        String imgUrl1, imgUrl2, imgUrl3, videoThumbUrl, videoUrl;
        switch (bean.getImgcount()) {
            case 1:
                cv1.setVisibility(View.VISIBLE);
                if ("1".equals(bean.getVedio())) {
                    cv2.setVisibility(View.VISIBLE);
                    playVideo2.setVisibility(View.VISIBLE);
                } else {
                    cv2.setVisibility(View.GONE);
                }
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.GONE);

                imgUrl1 = url + "/" + bean.getPath() + "/" + bean.getId() + "_0.jpg";
                sdv_img1.setImageURI(Uri.parse(imgUrl1));
                intentPreviewImg(sdv_img1, "日志图片01", bean.getId() + "_0", imgUrl1);

                videoThumbUrl = url + "/" + bean.getPath() + "/" + bean.getId() + ".jpg";
                sdv_img2.setImageURI(Uri.parse(videoThumbUrl));

                videoUrl = url + "/" + bean.getPath() + "/" + bean.getId() + ".mp4";
                intentPreviewVideo(sdv_img2, bean.getId(), videoUrl);
                break;
            case 2:
                cv1.setVisibility(View.VISIBLE);
                cv2.setVisibility(View.VISIBLE);
                playVideo2.setVisibility(View.GONE);
                if ("1".equals(bean.getVedio())) {
                    cv3.setVisibility(View.VISIBLE);
                    playVideo3.setVisibility(View.VISIBLE);
                } else {
                    cv3.setVisibility(View.GONE);
                }
                cv4.setVisibility(View.GONE);

                imgUrl1 = url + "/" + bean.getPath() + "/" + bean.getId() + "_0.jpg";
                sdv_img1.setImageURI(Uri.parse(imgUrl1));
                intentPreviewImg(sdv_img1, "日志图片01", bean.getId() + "_0", imgUrl1);

                imgUrl2 = url + "/" + bean.getPath() + "/" + bean.getId() + "_1.jpg";
                sdv_img2.setImageURI(Uri.parse(imgUrl2));
                intentPreviewImg(sdv_img2, "日志图片02", bean.getId() + "_1", imgUrl2);

                videoThumbUrl = url + "/" + bean.getPath() + "/" + bean.getId() + ".jpg";
                sdv_img3.setImageURI(Uri.parse(videoThumbUrl));

                videoUrl = url + "/" + bean.getPath() + "/" + bean.getId() + ".mp4";
                intentPreviewVideo(sdv_img3, bean.getId(), videoUrl);
                break;
            case 3:
                cv1.setVisibility(View.VISIBLE);
                cv2.setVisibility(View.VISIBLE);
                playVideo2.setVisibility(View.GONE);
                cv3.setVisibility(View.VISIBLE);
                playVideo3.setVisibility(View.GONE);
                if ("1".equals(bean.getVedio())) {
                    cv4.setVisibility(View.VISIBLE);
                    playVideo4.setVisibility(View.VISIBLE);
                } else {
                    cv4.setVisibility(View.GONE);
                }

                imgUrl1 = url + "/" + bean.getPath() + "/" + bean.getId() + "_0.jpg";
                sdv_img1.setImageURI(Uri.parse(imgUrl1));
                intentPreviewImg(sdv_img1, "日志图片01", bean.getId() + "_0", imgUrl1);

                imgUrl2 = url + "/" + bean.getPath() + "/" + bean.getId() + "_1.jpg";
                sdv_img2.setImageURI(Uri.parse(imgUrl2));
                intentPreviewImg(sdv_img2, "日志图片02", bean.getId() + "_1", imgUrl2);

                imgUrl3 = url + "/" + bean.getPath() + "/" + bean.getId() + "_2.jpg";
                sdv_img3.setImageURI(Uri.parse(imgUrl3));
                intentPreviewImg(sdv_img3, "日志图片03", bean.getId() + "_2", imgUrl3);

                videoThumbUrl = url + "/" + bean.getPath() + "/" + bean.getId() + ".jpg";
                sdv_img4.setImageURI(Uri.parse(videoThumbUrl));

                videoUrl = url + "/" + bean.getPath() + "/" + bean.getId() + ".mp4";
                intentPreviewVideo(sdv_img4, bean.getId(), videoUrl);
                break;
            default:
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.GONE);
                break;
        }
    }

    private void intentPreviewImg(View v, final String title, final String name, final String url) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImgPreviewActivity.class);
                intent.putExtra("imgTitle", title);
                intent.putExtra("imgName", name);
                intent.putExtra("imgUrl", url);
                mContext.startActivity(intent);
            }
        });
    }

    private void intentPreviewVideo(View v, final String name, final String url) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra("videoName", name);
                intent.putExtra("videoUrl", url);
                mContext.startActivity(intent);
            }
        });
    }

    private void showDelDialog(final String hid, final int position) {
        final CustomDialog dialog = new CustomDialog(mContext,
                ScreenUtils.getScreenWidth(mContext) * 3 / 4,
                ScreenUtils.getScreenHeight(mContext) / 5,
                R.layout.picture_base_dialog, R.style.Theme_dialog);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_commit = (Button) dialog.findViewById(R.id.btn_commit);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        tv_title.setText("温馨提示");
        tv_content.setText("是否删除该条日志？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                delHis(hid, position);
            }
        });
        dialog.show();
    }

    private void delHis(final String hid, final int position) {
        DialogUtil.showProgressDialog((Activity) mContext, "");
        String url = SPUtils.getInstance().getString(Constants.HOST, "");

        ApiLoader.reqDelHis(url + Api.DEL_HIS, hid, new SimpleCallback<BaseBean>() {
            @Override
            public void onNext(BaseBean bean) {
                if (bean.getCode() == 1) {
                    mDatas.remove(position);
                    setData(mDatas);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCompleted() {
                DialogUtil.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                DialogUtil.dismissProgressDialog();
            }
        });
    }
}