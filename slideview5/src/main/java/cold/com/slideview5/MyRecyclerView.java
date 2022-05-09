package cold.com.slideview5;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MyRecyclerView extends RecyclerView {

    private boolean flag = true;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
//        switch(e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (flag)
//                    System.out.println("---------------------------> ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("---------------------------> ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("---------------------------> ACTION_UP");
//        }
        if(flag)
            return true;
        return super.onInterceptTouchEvent(e);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch(event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if(flag)
//                System.out.println("---------------------------> ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("---------------------------> ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("---------------------------> ACTION_UP");
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
