package com.cold.projectanalysis.paintorview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.paintorview.bubble.LiveUtil;
import com.cold.projectanalysis.paintorview.bubble.PhiveBubbleEffect;
import com.cold.projectanalysis.paintorview.bubble.PhiveUtil;

public class PaintorViewActivity extends AppCompatActivity {

    private PhivePaintorView mPhivePaintorView;
    /**
     * 气泡
     */
    protected PhiveBubbleEffect mPhiveBubbleEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintor_view);
        mPhivePaintorView = (PhivePaintorView) findViewById(R.id.trv_paintor);

        mPhivePaintorView.getCandyPaintor().setVisib(LiveUtil.AnimEnum.AE_VISIABLE);
        mPhivePaintorView.start();
        mPhivePaintorView.getCandyPaintor().start();

        mPhiveBubbleEffect = new PhiveBubbleEffect(this);
        if (mPhiveBubbleEffect != null) {
            mPhiveBubbleEffect.setVisibleAnim(PhiveUtil.AnimEnum.AE_IN_VISIABLE, PhiveUtil.PhiveAnimEnum.LAE_CLEAR_VI, 0);
            mPhiveBubbleEffect.start();

        }
        mPhivePaintorView.getPhiveBubbleEffectPaintor().setVisib(LiveUtil.AnimEnum.AE_VISIABLE);
        mPhivePaintorView.getCandyPaintor().start();
    }

}
