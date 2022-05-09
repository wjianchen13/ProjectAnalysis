package com.cold.pkview.old;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cold.pkview.R;


public class LivePkView extends LinearLayout {

	private ImageView iv_head_left;//左头像
	private ImageView iv_head_right;//右头像
	private ImageView iv_win_right;//右赢
	private ImageView iv_win_left;//左赢
	private ProgressBar pb_left;//左进度
	private ProgressBar pb_right;//右进度
	private TextView tv_fire_left;//左火力
	private TextView tv_fire_right;//右火力
	private StrokeTextView tv_time;//pk剩余时间
	private TextView tv_fire_add_right;//增加火力动画右
	private TextView tv_fire_add_left;//增加火力动画左
	private Context mContext;
	private LivePkBean mCurrentAnchor;
	private LivePkBean mChallenger;
	private Runnable mTimeRunnable; //倒计时器
	public int limitTime;
	private Handler mHandler;

	public LivePkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public LivePkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public LivePkView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		setOrientation(LinearLayout.VERTICAL);
		LayoutInflater.from(mContext).inflate(R.layout.live_pk, this, true);

		iv_head_left = (ImageView) findViewById(R.id.iv_head_left);
		iv_head_right = (ImageView) findViewById(R.id.iv_head_right);
		iv_win_left = (ImageView) findViewById(R.id.iv_win_left);
		iv_win_right = (ImageView) findViewById(R.id.iv_win_right);
		pb_left = (ProgressBar) findViewById(R.id.pb_left);
		pb_right = (ProgressBar) findViewById(R.id.pb_right);
		tv_fire_left = (TextView) findViewById(R.id.tv_fire_left);
		tv_fire_right = (TextView) findViewById(R.id.tv_fire_right);
		tv_fire_add_left = (TextView) findViewById(R.id.tv_fire_add_left);
		tv_fire_add_right = (TextView) findViewById(R.id.tv_fire_add_right);
		tv_time = (StrokeTextView) findViewById(R.id.tv_time);

		//设置描边
		tv_time.setStrokeColorAndWidth(getResources().getColor(R.color.black_alph60), 3);

		//圆角图片
		CircleImageDrawable circleImageDrawable = new CircleImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.img_user_icon));
		iv_head_left.setImageDrawable(circleImageDrawable);
		iv_head_right.setImageDrawable(circleImageDrawable);

		iv_head_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (mChallenger != null) {
//					AppUtil.jumpToAnchorRoom(mContext, mChallenger.getAnchorId());
//				}
			}
		});
	}
	int i = 0;
	/**
	 * 增加pk火力值
	 * @param
	 */
	public void addPkFire(LivePkInfo livePkInfo) {
		if (livePkInfo != null) {
			limitTime = livePkInfo.getLimitTime();
			if (mHandler != null) {
				mHandler.removeCallbacks(mTimeRunnable);
			}

//			if(livePkInfo.getStatus()==2){
//				Toast.makeText(mContext,"PK结束.........!",Toast.LENGTH_SHORT).show();
//			}

			if (mHandler == null) mHandler = new Handler();
			if (mTimeRunnable == null) mTimeRunnable = new Runnable() {

				@Override
				public void run() {
					//如果一分钟之内没收到送礼信息，更新时间
					if (limitTime > 0) {
						tv_time.setText(limitTime-- + "分后截止");	
						if(mHandler != null) mHandler.postDelayed(this, 60000);
					}
				}
			};

			mHandler.postDelayed(mTimeRunnable, 60000);

			tv_time.setText(livePkInfo.getLimitTime() + "分后截止");

			if (livePkInfo.getAnchorLeft() != null){
				pb_left.setProgress((int) (livePkInfo.getAnchorLeft().getCurrentExp() * 100 / livePkInfo.getTotalExp()));
				tv_fire_left.setText(livePkInfo.getAnchorLeft().getCurrentExp() + "/" +livePkInfo.getTotalExp()+ "火力");
				ImageHelper.loadCircleImage(getContext(),livePkInfo.getAnchorLeft().getHeadUrl(), iv_head_left, R.drawable.img_user_icon);

				if (mCurrentAnchor != null && livePkInfo.getAnchorLeft().getCurrentExp() - mCurrentAnchor.getCurrentExp() > 0) {
					//播放增加火力动画
					playAddAnimate(true, livePkInfo.getAnchorLeft().getCurrentExp() - mCurrentAnchor.getCurrentExp());
				}
				mCurrentAnchor = livePkInfo.getAnchorLeft();
			}

			if (livePkInfo.getAnchorRight() != null) {
				pb_right.setProgress((int) (livePkInfo.getAnchorRight().getCurrentExp() * 100 / livePkInfo.getTotalExp()));
				tv_fire_right.setText(livePkInfo.getAnchorRight().getCurrentExp()+  "/" +livePkInfo.getTotalExp()+ "火力");
				ImageHelper.loadCircleImage(getContext(),livePkInfo.getAnchorRight().getHeadUrl(), iv_head_right,R.drawable.img_user_icon);

				if (mChallenger != null && livePkInfo.getAnchorRight().getCurrentExp() - mChallenger.getCurrentExp() > 0) {
					//播放增加火力动画
					playAddAnimate(false, livePkInfo.getAnchorRight().getCurrentExp() - mChallenger.getCurrentExp());
				}

				mChallenger = livePkInfo.getAnchorRight();
			}

//			if(i>3){
//				livePkInfo.setStatus(2);
//
//			}
//			i++;
			if (livePkInfo.getStatus() == 2) {
				//播放赢动画
				if (livePkInfo.getAnchorLeft() != null && livePkInfo.getAnchorLeft().getAnchorId() == livePkInfo.getWinerId()) {
					playWinAnimate(true);
				} else if (livePkInfo.getAnchorRight() != null && livePkInfo.getAnchorRight().getAnchorId() == livePkInfo.getWinerId()) {
					playWinAnimate(false);
				}else {
					if (mHandler == null) mHandler = new Handler();
					mHandler.postDelayed(new Runnable(){
						@Override
						public void run(){
							LivePkView.this.setVisibility(View.INVISIBLE);
						}
					}, 10000);
				}
			}

			if (livePkInfo.getStatus() == 3 ) {
				if (mHandler == null) mHandler = new Handler();
				mHandler.postDelayed(new Runnable(){
					@Override
					public void run(){
						LivePkView.this.setVisibility(View.INVISIBLE);
					}
				}, 10000);
			}
		}
	}

	/**
	 * 播放增加火力动画
	 * @param isCurrentAnchor 是否是当前主播
	 */
	private void playAddAnimate(boolean isCurrentAnchor, long addExp) {

		final TextView targeTextView;
		final float animateY = dip2px(14);//Y轴初始位置
		//		ApplicationUtil.showToast(mContext, "位置：" + animateY);
		//		int originalX = 0;
		//		int originalY = 0;
		//		int targetX= 0;
		//		int targety= 0;

		if (isCurrentAnchor) {
			targeTextView = tv_fire_add_left;
			targeTextView.setText(" +" + addExp);
			//			targetX = 80;
			//			targetY = -30;
		} else {
			targeTextView = tv_fire_add_right;
			targeTextView.setText(addExp + "+ ");
			//			targetX = -80;
			//			targetY = -30;
		}

		//		ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(targeTextView, "translationX", originalX, targetX);
		//		ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(targeTextView, "translationY", originalY, targetY);
		ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(targeTextView, "translationY", 0, -animateY);

		//		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targeTextView, "scaleX", 1.3F);
		//		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targeTextView, "scaleY", 1.3F);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(targeTextView, "alpha", 0, 1, 0);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(1000);

		animatorSet.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				//				targeTextView.setTranslationX(0);
				//				targeTextView.setTranslationY(0);
				//				targeTextView.setScaleX(1);
				//				targeTextView.setScaleY(1);
				targeTextView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				targeTextView.setVisibility(View.INVISIBLE);
			}
		});

		//		animatorSet.playTogether(translationXAnimator, translationYAnimator, scaleXAnimator, scaleYAnimator, alphaAnimator);
		animatorSet.playTogether(translationYAnimator, alphaAnimator);
		animatorSet.start();
	}

	/**
	 * 播放赢动画
	 * @param isCurrentAnchor 是否是当前主播
	 */
	private void playWinAnimate(boolean isCurrentAnchor) {
//		Toast.makeText(mContext,"来来！胜利图标",Toast.LENGTH_SHORT).show();
		if (LivePkView.this.getVisibility() != View.VISIBLE) return;
		final ImageView targeImageView;
		if (isCurrentAnchor) targeImageView = iv_win_left;
		else targeImageView = iv_win_right;

//		Toast.makeText(mContext,"显示PK胜利图标",Toast.LENGTH_SHORT).show();

		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targeImageView, "scaleX", 1.6F, 1);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targeImageView, "scaleY", 1.6F, 1);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(targeImageView, "alpha", 0, 1);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(250);
		animatorSet.setInterpolator(new DecelerateInterpolator());//由快变慢

		animatorSet.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				//				targeImageView.setScaleX(1);
				//				targeImageView.setScaleY(1);
				targeImageView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {

				if (mHandler == null) mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						targeImageView.setVisibility(View.INVISIBLE);
						LivePkView.this.setVisibility(View.INVISIBLE);
						//清空上一个PK时间限制，避免PK动画的时候出现错误，因为有引用这个属性判断
						limitTime = 0;
					}
				}, 10000);
			}
		});

		animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
		animatorSet.start();
	}
	
	public  void clear(){
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(float dpValue) {
		return (int) (dpValue * getDensity(mContext) + 0.5f);
	}

	/**
	 * 返回屏幕密度
	 */
	public float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}
}
