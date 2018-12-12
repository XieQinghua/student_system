package com.stu.system.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SizeUtils;
import com.stu.system.R;
import com.stu.system.base.BaseFragment;
import com.stu.system.common.Constants;
import com.stu.system.util.DrawableUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 添加学生
 */
public class StuAddFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_stu_name)
    EditText etStuName;
    @BindView(R.id.tv_stu_sex)
    TextView tvStuSex;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_stu_class)
    TextView tvStuClass;
    @BindView(R.id.tv_stu_birthday)
    TextView tvStuBirthday;
    @BindView(R.id.et_stu_address)
    EditText etStuAddress;
    @BindView(R.id.iv_add_pic)
    ImageView ivAddPic;
    @BindView(R.id.btn_save)
    Button btnSave;
    Unbinder unbinder;

    private View view;

    private List<String> sexList;
    private String sex, birthday;

    @Override
    protected View onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stu_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        //EventBus.getDefault().register(this);
        ivBack.setVisibility(View.INVISIBLE);
        tvTitle.setText(R.string.stu_add);

        final GradientDrawable checkedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.TRAN_MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.TRAN_MAIN_COLOR);
        final GradientDrawable uncheckedShape = DrawableUtils.getShape(GradientDrawable.RECTANGLE, Constants.MAIN_COLOR, SizeUtils.dp2px(25), 0, Constants.MAIN_COLOR);
        final StateListDrawable selector = DrawableUtils.getSelector(checkedShape, uncheckedShape);
        btnSave.setBackground(selector);

        tvStuSex.setOnClickListener(this);
        tvStuClass.setOnClickListener(this);
        tvStuBirthday.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tv_stu_sex:
                OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        sex = sexList.get(options1);
                        tvStuSex.setText(sex);
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .build();
                pvOptions.setPicker(sexList);
                pvOptions.show();
                break;
            case R.id.tv_stu_class:

                break;
            case R.id.tv_stu_birthday:
                TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        birthday = date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                        tvStuBirthday.setText(birthday);
                    }
                }).setTitleColor(Constants.MAIN_COLOR)
                        .build();
                pvTime.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
