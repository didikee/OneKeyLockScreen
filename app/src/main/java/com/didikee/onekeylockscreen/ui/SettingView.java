package com.didikee.onekeylockscreen.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by didik on 2017/3/8.
 */

public class SettingView extends FrameLayout {
    public static final int SWITCH_VIEW = 1;
    public static final int CHECKBOX = 2;
    public static final int RADIO_BUTTON = 3;
    public static final int NONE = 0;


    private int mActionViewType = NONE;
    private TextView mTitle;//一定会有
    private TextView mTitleInfo;//可能会有
    private Drawable mIconDrawable;//可能会有
    private View mActionView;//可能会有
    private boolean isShowDivider = true;//可能会有
    private boolean isFullLine = true;//是否铺满底部
    private final int minHeight = 36;//最小item高度36dp
    private int mHeight = -1;


    private boolean mChecked = false;//如果没有ActionView,那么恒为false


    public SettingView(Context context) {
        this(context, null);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 计算是否小于最小高度


        //左边的icon
        if (mIconDrawable !=null){
            //添加左边 icon

            //判断底部线是否铺满
            //获取是否铺满底部
            isFullLine = false;//假设
        }else {
            isFullLine = false;
        }

        //底部分隔线
        if (isShowDivider){
            addDivider(isFullLine);
        }

        //右边的ActionView
        int type = checkRightActionViewType();
        addRightActionView(getRightActionView(type));

        //中间的文字(单行或者双行)

        //底部的分隔线

        //item的事件给ActionView
        if (mActionViewType !=NONE){
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionView.performClick();
                }
            });
        }
    }

    /**
     * 添加底部分隔线
     * @param isFullLine 是否铺满
     */
    private void addDivider(boolean isFullLine) {
        FrameLayout.LayoutParams lineParams= new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lineParams.gravity = Gravity.BOTTOM ;
        int leftMargin = isFullLine ? 0 : mHeight;
        lineParams.setMargins(leftMargin,0,0,0);
        View line =new View(getContext());
        line.setBackgroundColor(Color.RED);
        this.addView(line,lineParams);
    }

    /**
     * 获取右边动作View 的类型
     * @param type {@link #NONE#CHECKBOX#SWITCH_VIEW#RADIO_BUTTON}
     * @return {@link #NONE#CHECKBOX#SWITCH_VIEW#RADIO_BUTTON}
     */
    private View getRightActionView(int type) {
        View actionView;
        switch (type) {
            case SWITCH_VIEW:
                actionView = new Switch(getContext());
                break;
            case CHECKBOX:
                actionView = new CheckBox(getContext());
                break;
            case RADIO_BUTTON:
                actionView = new RadioButton(getContext());
                break;
            default:
                actionView = null;
                break;
        }
        return actionView;
    }

    private void addRightActionView(View actionView) {
        if (actionView == null)return;
        FrameLayout.LayoutParams actionParams= new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
        this.addView(actionView,actionParams);
        addActionViewListener(actionView);
    }

    private void addActionViewListener(View actionView) {
        if (actionView instanceof CompoundButton){
            ((CompoundButton) actionView).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mChecked = isChecked;
                }
            });
        }
    }

//    private FrameLayout.LayoutParams getFrameLayoutParams(){
//        return
//    }

    protected int checkRightActionViewType() {
        return NONE;
    }

}
