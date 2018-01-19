package com.example.asus.zlzjqrcode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.base.BPApplication;

/**
 * Created by asus on 2018/1/10.
 */

public class StampView extends View {

    private static final int TEXT_SIZE = 35;  //文字大小
    private static final int TEXT_SIZE_2 = 30;  //文字大小
    private static final int OUTSIDE_RING_WIDTH = 10; //圆环宽度
    private static final float TEXT_ANGLE = 220f;  //文字扇形排列的角度
    private  int RADIUS = 150;  //印章半径
    private static final int SPACING = 25;  //图片与文字间距
    private static final float STAMP_ROTATE = 0f;  //印章旋转角度

    private static final int TEXT_COLOR = 0xFFb70012; //文字颜色
    private static final int CIRCLE_COLOR = 0xFFb70012; //圆环颜色
    private Context context;

    private String mText;
    private String getmText="官方认证";
    private Bitmap source;
    private int width,height;

    private final Paint mTextPaint = new Paint();
    private final Paint getmTextPaint=new Paint();
    private final Paint mCirclePaint = new Paint();


    public StampView(Context context) {
        super(context);
        init(context);
    }

    public StampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StampView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mTextPaint.setColor(TEXT_COLOR);
        getmTextPaint.setColor(TEXT_COLOR);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        getmTextPaint.setTextAlign(Paint.Align.LEFT);

        mCirclePaint.setColor(CIRCLE_COLOR);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);

        source = BitmapFactory.decodeResource(getResources(), R.drawable.xiufanghua_2);


    }

    @Override
    protected void onDraw(Canvas canvas) {
//         获取宽高
        width = this.getWidth();
        height = this.getWidth();
//        if(BPApplication.getInstance().getPx().equals("2")){
//            RADIUS=150;
//        }else {
//            RADIUS=120;
//        }

        Log.e("高度和宽度",RADIUS+"---");
         int radiuss = width / 3;
        mTextPaint.setTextSize(TEXT_SIZE);
        getmTextPaint.setTextSize(TEXT_SIZE_2);

        float textY = height / 2 - RADIUS + OUTSIDE_RING_WIDTH + TEXT_SIZE;
        mText = mText != null ? mText : "";
        // 把文字拆成字符数组
        char[] chs = mText.toCharArray();

        // 画圆环
        mCirclePaint.setStrokeWidth(OUTSIDE_RING_WIDTH);
        Log.e("高度和宽度",RADIUS - OUTSIDE_RING_WIDTH / 2+"");
        canvas.drawCircle(width / 2, height / 2, RADIUS - OUTSIDE_RING_WIDTH / 2, mCirclePaint);

        canvas.save();
        canvas.rotate(STAMP_ROTATE, width / 2, height / 2);

        // 中间圆形位图的半径
        int radius = RADIUS - OUTSIDE_RING_WIDTH - TEXT_SIZE - SPACING;
        canvas.drawText(getmText, width/2-(TEXT_SIZE_2*2), height/2+radius+TEXT_SIZE_2, getmTextPaint);
        Bitmap image = createCircleImage(source, radius * 2);
        canvas.drawBitmap(image, (width - image.getWidth()) / 2, (height - image.getHeight()) / 2,
                mCirclePaint);
        image.recycle();

        canvas.rotate(-TEXT_ANGLE / 2, width / 2, height / 2);
        // 每个文字间的角度间隔
        float spaceAngle = TEXT_ANGLE / (chs.length - 1);

        for(int i = 0; i < chs.length; i++) {
            String s = String.valueOf(chs[i]);
            canvas.drawText(s, width / 2, textY, mTextPaint);
            canvas.rotate(spaceAngle, width / 2, height / 2);
        }
        canvas.restore();
    }




    /**
     * 创建圆形位图
     * @param source 原图片位图
     * @param diameter 圆形位图的直径
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int diameter) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap clipBitmap;
        if(width > height) {
            int x = (width - height) / 2;
            int y = 0;
            clipBitmap = Bitmap.createBitmap(source, x, y, height, height);
        } else if(width < height) {
            int x = 0;
            int y = (height - width) / 2;
            clipBitmap = Bitmap.createBitmap(source, x, y, width, width);
        } else {
            clipBitmap = source;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(clipBitmap, diameter, diameter, true);
        Bitmap outputBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
//        source.recycle();
//        clipBitmap.recycle();
//        scaledBitmap.recycle();
        return outputBitmap;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
    }

    public void setCircleColor(int circleColor) {
        mCirclePaint.setColor(circleColor);
    }

    public void setBitmap(int id) {
        source = BitmapFactory.decodeResource(getResources(), id);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
