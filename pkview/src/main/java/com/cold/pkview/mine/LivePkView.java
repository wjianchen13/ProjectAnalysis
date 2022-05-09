package com.cold.pkview.mine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

/**
 * name: LivePkView
 * desc: pk界面
 * author:
 * date: 2017-06-20 17:27
 * remark:
 */
public class LivePkView extends LinearLayout {

	/**
	 * 上头像
	 */
	private ImageView imgvTop;

	/**
	 * 下头像
	 */
	private ImageView imgvBottom;

	/**
	 * 上赢标志
	 */
	private ImageView imgvTopWin;

	/**
	 * 下赢标志
	 */
	private ImageView imgvBottomWin;

	/**
	 * 上进度
	 */
	private ProgressBar pbTop;

	/**
	 * 下进度
	 */
	private ProgressBar pbBottom;

	/**
	 * 上火力
	 */
	private TextView tvFireTop;

	/**
	 * 下火力
	 */
	private TextView tvFireBottom;

	/**
	 * pk剩余时间
	 */
	private TextView tv_time;

	/**
	 * 增加火力动画左
	 */
	private TextView tv_fire_add_left;

	/**
	 * 增加火力动画右
	 */
	private TextView tv_fire_add_right;

	/**
	 * 上下文
	 */
	private Context mContext;

	/**
	 * 己方数据
	 */
	private LivePkBean mCurrentAnchor;

	/**
	 * 对方数据
	 */
	private LivePkBean mChallenger;

	/**
	 * 倒计时器
	 */
	private Runnable mTimeRunnable;

	/**
	 * pk剩余时间
	 */
	public int limitTime;

	/**
	 * 倒计时用
	 */
	private Handler mHandler;

	/**
	 * 上边添加火力动画进行标志
	 */
	private boolean isTopRunning;

	/**
	 * 下边添加火力动画进行标志
	 */
	private boolean isBottomRunning;

	/**
	 * ==============================================================================================================
	 * 测试用
	 * start
	 **/
	private boolean debug = true;
	private LivePkInfo livePkInfo; // 测试用

	public long getTopExp() {
		if(livePkInfo != null) {
			return livePkInfo.getAnchorLeft().getCurrentExp();
		} else {
			return 0;
		}
	}

	public long getBottomExp() {
		if(livePkInfo != null) {
			return livePkInfo.getAnchorRight().getCurrentExp();
		} else {
			return 0;
		}
	}

	public LivePkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public void reset() {
		livePkInfo = null;
	}
	/**
	 * 测试用
	 * end
	 * ==============================================================================================================
	 **/

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
		LayoutInflater.from(mContext).inflate(R.layout.mine_pk, this, true);

		imgvTop = (ImageView) findViewById(R.id.imgv_top);
		imgvBottom = (ImageView) findViewById(R.id.imgv_bottom);
		imgvTopWin = (ImageView) findViewById(R.id.imgv_top_win);
		imgvTopWin.setVisibility(View.GONE);
		imgvBottomWin = (ImageView) findViewById(R.id.imgv_bottom_win);
		imgvBottomWin.setVisibility(View.GONE);
		pbTop = (ProgressBar) findViewById(R.id.pb_top);
		pbBottom = (ProgressBar) findViewById(R.id.pb_bottom);
		tvFireTop = (TextView) findViewById(R.id.tv_fire_top);
		tvFireBottom = (TextView) findViewById(R.id.tv_fire_bottom);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_fire_add_left = (TextView) findViewById(R.id.tv_fire_add_left);
		tv_fire_add_right = (TextView) findViewById(R.id.tv_fire_add_right);

//		//设置描边
//		tv_time.setStrokeColorAndWidth(getResources().getColor(R.color.black_alph60), 3);

		CircleImageDrawable circleImageDrawable = new CircleImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.img_user_icon));
		imgvTop.setImageDrawable(circleImageDrawable);
		imgvBottom.setImageDrawable(circleImageDrawable);

		imgvBottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (mChallenger != null) {
//					AppUtil.jumpToAnchorRoom(mContext, mChallenger.getAnchorId());
//				}
			}
		});
//		setVisibility(View.GONE);
	}

	/**
	 * 增加pk火力值
	 * @param
	 * @return
	 */
	public void addPkFire(LivePkInfo livePkInfo) {
		this.livePkInfo = livePkInfo;
		if(livePkInfo == null) {
			return ;
		}
		limitTime = livePkInfo.getLimitTime();
		if (mHandler != null) {
			mHandler.removeCallbacks(mTimeRunnable);
		}

		if (mHandler == null) mHandler = new Handler();
		if (mTimeRunnable == null) mTimeRunnable = new Runnable() {
			@Override
			public void run() {
				if (limitTime > 0) {
					tv_time.setText(getTime(limitTime--));
					if(mHandler != null) mHandler.postDelayed(this, 60000);
				}
			}
		};

		mHandler.postDelayed(mTimeRunnable, 1000);
		if(!debug) {
			tv_time.setText(getTime(livePkInfo.getLimitTime()));
		}
		if(livePkInfo.getStatus() == 1) {

			setVisible(1);
		}
		if (livePkInfo.getAnchorLeft() != null){
			int progress = (int) (livePkInfo.getAnchorLeft().getCurrentExp() * 100 / livePkInfo.getTotalExp());
//			if(progress < 3)
//				progress = 3;
			pbTop.setProgress(progress);
			tvFireTop.setText(livePkInfo.getAnchorLeft().getCurrentExp() + "/" +livePkInfo.getTotalExp());
			ImageHelper.loadCircleImage(getContext(),livePkInfo.getAnchorLeft().getHeadUrl(), imgvTop, R.drawable.img_user_icon);

			if (mCurrentAnchor != null && livePkInfo.getAnchorLeft().getCurrentExp() - mCurrentAnchor.getCurrentExp() > 0) {
				//播放增加火力动画
				playAddAnimate(true, livePkInfo.getAnchorLeft().getCurrentExp() - mCurrentAnchor.getCurrentExp());
			}

			mCurrentAnchor = livePkInfo.getAnchorLeft();
		}

		if (livePkInfo.getAnchorRight() != null) {
			pbBottom.setProgress((int) (livePkInfo.getAnchorRight().getCurrentExp() * 100 / livePkInfo.getTotalExp()));
			tvFireBottom.setText(livePkInfo.getAnchorRight().getCurrentExp()+  "/" +livePkInfo.getTotalExp());
			ImageHelper.loadCircleImage(getContext(),livePkInfo.getAnchorRight().getHeadUrl(), imgvBottom,R.drawable.img_user_icon);

			if(mChallenger != null && livePkInfo.getAnchorRight().getCurrentExp() - mChallenger.getCurrentExp() > 0) {
				//播放增加火力动画
				playAddAnimate(false, livePkInfo.getAnchorRight().getCurrentExp() - mChallenger.getCurrentExp());
			}

			mChallenger = livePkInfo.getAnchorRight();
		}

		if (livePkInfo.getStatus() == 2) {
			if (livePkInfo.getAnchorLeft() != null && livePkInfo.getAnchorLeft().getAnchorId() == livePkInfo.getWinerId()) {
				playWinAnimate(true);
			} else if (livePkInfo.getAnchorRight() != null && livePkInfo.getAnchorRight().getAnchorId() == livePkInfo.getWinerId()) {
				playWinAnimate(false);
			}else {
				if (mHandler == null) mHandler = new Handler();
				mHandler.postDelayed(new Runnable(){
					@Override
					public void run(){
						setVisible(2);
					}
				}, 10000);
			}
		}

		if (livePkInfo.getStatus() == 3 ) {
			if (mHandler == null) mHandler = new Handler();
			mHandler.postDelayed(new Runnable(){
				@Override
				public void run(){
					setVisible(2);
				}
			}, 10000);
		}
	}

	/**
	 * 播放增加火力动画
	 * @param isCurrentAnchor 是否是当前主播
	 * @return
	 */
	private void playAddAnimate(boolean isCurrentAnchor, long addExp) {
		final boolean currentAnchor = isCurrentAnchor;
		final TextView targeTextView;
		final float animateY = dip2px(24);//Y轴初始位置
		if (isCurrentAnchor) {
			if(isTopRunning) {
				return;
			}
			targeTextView = tv_fire_add_left;
			isTopRunning = true;
			targeTextView.setText("+" + addExp);
		} else {
			if(isBottomRunning) {
				return;
			}
			isBottomRunning = true;
			targeTextView = tv_fire_add_right;
			targeTextView.setText("+" + addExp);
		}
		ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(targeTextView, "translationY", -dip2px(8), -animateY);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(targeTextView, "alpha", 0f, 0.90f, 0.98f, 1f, 0.98f, 0.90f, 0f);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(1200);

		animatorSet.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				targeTextView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if(currentAnchor){
					isTopRunning = false;
				} else {
					isBottomRunning = false;
				}
				targeTextView.setVisibility(View.INVISIBLE);
			}
		});
		animatorSet.playTogether(translationYAnimator, alphaAnimator);
		animatorSet.start();
	}

	/**
	 * 播放pk胜利动画
	 * @param isCurrentAnchor 是否是当前主播
	 * @return
	 */
	private void playWinAnimate(boolean isCurrentAnchor) {
		if (LivePkView.this.getVisibility() != View.VISIBLE) {
			return;
		}
		final ImageView targeImageView;
		if (isCurrentAnchor) {
			targeImageView = imgvTopWin;
		} else {
			targeImageView = imgvBottomWin;
		}

		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targeImageView, "scaleX", 1.6F, 1);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targeImageView, "scaleY", 1.6F, 1);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(targeImageView, "alpha", 0, 1);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(250);
		animatorSet.setInterpolator(new DecelerateInterpolator());//由快变慢

		animatorSet.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				targeImageView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (mHandler == null) {
					mHandler = new Handler();
				}
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						setVisible(2);
						limitTime = 0;
					}
				}, 2000);
			}
		});

		animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
		animatorSet.start();
	}

	/**
	 * 清除定时器
	 * @param:
	 * @return: void
	 */
	public  void clear(){
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
	}

	/**
	 * 根据剩余时间获取倒计时显示格式信息
	 * @param: limitTime 剩余时间
	 * @return: 显示时间信息
	 */
	private String getTime(int limitTime) {
		int time = limitTime;
		long hour = time / 60;
		String shour = hour > 9 ? "" + hour : "0" + hour;
		long min = time % 60;
		String smin = min > 9 ? "" + min : "0" + min;
		return "" + shour + ":" + smin;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(float dpValue) {
		return (int) (dpValue * getDensity(mContext) + 0.5f);
	}

	/**
	 * 返回屏幕密度
	 */
	private float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	private void setVisible(int animEnum) {
		switch (animEnum) {
			case 1:
				if (getVisibility() == View.VISIBLE) {
					return;
				}
				AnimatorSet set = new AnimatorSet();

				ObjectAnimator objectAnitor = getObjectAnitor(this, 300, 210, 0, 0, 1, false);
				this.setTag(objectAnitor);
				objectAnitor.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationStart(Animator animation) {
						super.onAnimationStart(animation);
						//有PK消息数据才显示PK界面
						if (limitTime > 0) {
							setVisibility(View.VISIBLE);
						} else {
							setVisibility(View.GONE);
						}
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						LivePkView.this.setTag(null);
						super.onAnimationEnd(animation);
						//有PK消息数据才显示PK界面
						if (limitTime <= 0) {
							setVisibility(View.GONE);
						}
					}
				});
				set.playTogether(objectAnitor);
				set.start();
				break;

			case 2:
				if (getVisibility() == View.GONE) {
					return;
				}
				AnimatorSet set2 = new AnimatorSet();

				ObjectAnimator objectAnitor2 = getObjectAnitor(this, 300, 0, 210, 1, 0, false);
				this.setTag(objectAnitor2);
				objectAnitor2.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						LivePkView.this.setTag(null);
						super.onAnimationEnd(animation);
						livePkInfo = null;
						imgvTopWin.setVisibility(View.GONE);
						imgvBottomWin.setVisibility(View.GONE);
						pbTop.setProgress(0);
						pbBottom.setProgress(0);
						setVisibility(View.GONE);
					}
				});
				set2.playTogether(objectAnitor2);
				set2.start();
				break;
			default:
				break;
		}
	}

	public static ObjectAnimator getObjectAnitor(View targetView, int time, int translationYfrom, int translationYTo, int alphaStart, int alphaEnd, boolean direction) {
		PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat(direction ? "translationY" : "translationX", translationYfrom, translationYTo);
		PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", alphaStart, alphaEnd);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(targetView, translationY, alpha);
		objectAnimator.setDuration(time);
		return objectAnimator;
	}
}
