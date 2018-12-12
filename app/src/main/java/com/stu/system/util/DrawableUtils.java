package com.stu.system.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {
    /**
     * 是否按下，如一个按钮触摸或者点击。
     */
    public static final int[] STATE_PRESSED = new int[]{android.R.attr.state_pressed};
    /**
     * 被选中，它与focus state并不完全一样，如一个list view 被选中的时候，它里面的各个子组件可能通过方向键，被选中了。
     */
    public static final int[] STATE_SELECTED = new int[]{android.R.attr.state_selected};
    /**
     * 被checked了，如：一个RadioButton可以被check了。
     */
    public static final int[] STATE_CHECKED_TRUE = new int[]{android.R.attr.state_checked};
    public static final int[] STATE_CHECKED_FALSE = new int[]{-android.R.attr.state_checked};

    /**
     * 组件是否能被check。如：RadioButton是可以被check的。
     */
    public static final int[] STATE_CHECKABLE = new int[]{android.R.attr.state_checkable};

    /**
     * 定义一个shape资源
     *
     * @param strokeWidth 边框线的宽度
     * @param roundRadius 圆角
     * @param strokeColor 边框线颜色
     * @param solidColor  内部填充颜色
     * @return
     */
    public static GradientDrawable getShape(int shape, int solidColor, int roundRadius, int strokeWidth, int strokeColor) {
        //创建drawable
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(shape);
        gd.setColor(solidColor);
        if (roundRadius != 0) {
            gd.setCornerRadius(roundRadius);
        }
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    /**
     * 获得selector
     *
     * @param pressedDrawable 点击时的Drawable
     * @param normalDrawable  不点击时的Drawable
     * @param pressedState    点击效果的类型  int state[]{android.R.attr.state_pressed}
     * @param normalState
     * @return
     */
    public static StateListDrawable getSelector(int[] pressedState, Drawable pressedDrawable, int[] normalState, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //有状态的必须写在上面
        stateListDrawable.addState(pressedState, pressedDrawable);
        //没有状态的必须写在下面
        stateListDrawable.addState(normalState, normalDrawable);
        return stateListDrawable;
    }

    public static StateListDrawable getSelector(Drawable pressDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, normalDrawable);
        //设置默认状态
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }
}
