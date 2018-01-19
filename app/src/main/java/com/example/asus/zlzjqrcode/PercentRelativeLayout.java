package com.example.asus.zlzjqrcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by asus on 2018/1/15.
 */

public class PercentRelativeLayout extends RelativeLayout {
    public PercentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }
    public PercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    public PercentRelativeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        // TODO Auto-generated method stub
        return new LayoutParams(getContext(), attrs);
    }
    //测量自己
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取自身的宽高
        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);
        for(int i = 0;i<this.getChildCount();i++){
            View child = this.getChildAt(i);
            //获取孩子view的布局属性
            ViewGroup.LayoutParams params = child.getLayoutParams();
            float widthPercent = 0;
            float heightPercent = 0;
            //含有自定义的属性，则获取百分百
            if(params instanceof PercentRelativeLayout.LayoutParams){
                widthPercent = ((PercentRelativeLayout.LayoutParams) params).getWidthPercent();
                heightPercent = ((PercentRelativeLayout.LayoutParams) params).getHeightPercent();
            }
            if(widthPercent == 0|| heightPercent == 0){
                continue;//百分百为0，跳出此次循环
            }
            //真实的宽高
            params.width = (int) (widthPercent*widthHint);
            params.height = (int) (heightPercent*heightHint);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }
    public static class LayoutParams extends RelativeLayout.LayoutParams{
        private float widthPercent;
        private float heightPercent;
        public float getWidthPercent() {
            return widthPercent;
        }
        public void setWidthPercent(float widthPercent) {
            this.widthPercent = widthPercent;
        }
        public float getHeightPercent() {
            return heightPercent;
        }
        public void setHeightPercent(float heightPercent) {
            this.heightPercent = heightPercent;
        }
        //构造函数里面获取自定义样式属性的值
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.percentRelativeLayout);
            widthPercent = array.getFloat(R.styleable.percentRelativeLayout_layout_widthPercent, widthPercent);
            heightPercent = array.getFloat(R.styleable.percentRelativeLayout_layout_heightPercent,heightPercent);
            array.recycle();
        }
        public LayoutParams(int w, int h) {
            super(w, h);
            // TODO Auto-generated constructor stub
        }
        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            // TODO Auto-generated constructor stub
        }
        public LayoutParams(MarginLayoutParams source) {
            super(source);
            // TODO Auto-generated constructor stub
        }
    }
}
