package com.cold.pressview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cold.pressview.R;

/**
 * name: PressView
 * desc: 实现点击文字和图片都有按下效果
 * author:
 * date: 2017-06-21 16:12
 * remark:
 */
public class PressView extends RelativeLayout {

    private Integer imageid = 0;
    private Integer textid = 0;

    private View image;
    private View text;

    public PressView(Context context) {
        super(context);
    }

    public PressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.attachid);
//        imageid = ta.getInteger(R.styleable.attachid_imageid, 0);
//        textid = ta.getInteger(R.styleable.attachid_textid, 0);
//        image = findViewById(imageid);
//        text = findViewById(textid);
    }

    public PressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("----------------> (image != null):" + (image != null));
                System.out.println("----------------> (text != null):" + (text != null));
                if(image != null) {
                    image.setPressed(true);
                }
                if(text != null) {
                    text.setPressed(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(image != null) {
                    image.setPressed(false);
                }
                if(text != null) {
                    text.setPressed(false);
                }
                break;
        }

        return super.onTouchEvent(event);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        image = getChildAt(0);
        text = getChildAt(1);

    }
}
