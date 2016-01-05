package org.linphone.newphone.tools;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
	/**
	 * 展示一个移动的动画。
	 * 
	 * @param v
	 *            加入动画的view组件
	 * @param time
	 *            动画完成时间
	 * @param x1
	 *            x轴起始位置
	 * @param x2
	 *            x轴结束位置
	 * @param y1
	 *            y轴起始位置
	 * @param y2
	 *            y轴结束位置
	 * @since 从最初版本添加。
	 */
	public static void setTranslateAnimation(View v, int time, int x1, int x2, int y1, int y2) {
		TranslateAnimation animation = new TranslateAnimation(x1, x2, y1, y2);
		animation.setDuration(time);
		v.setAnimation(animation);
	}

	/**
	 * 展示一个渐变的动画。
	 * 
	 * @param v
	 *            加入动画的view组件
	 * @param time
	 *            动画完成时间
	 * @param repeatCount
	 *            重复次数
	 * @param startOffset
	 *            动画开始时间
	 * @since 从最初版本添加。
	 */
	public static void setAlphaAnimation(View v, int time, int repeatCount, int startOffset) {
		AlphaAnimation myAnimation_Alpha = new AlphaAnimation(0f, 1.0f);
		myAnimation_Alpha.setDuration(time);
		myAnimation_Alpha.setRepeatCount(repeatCount);
		myAnimation_Alpha.setStartOffset(startOffset);
		v.setAnimation(myAnimation_Alpha);
	}

	/**
	 * 展示一个旋转的动画。
	 * 
	 * @param v
	 *            加入动画的view组件
	 * @param contect
	 *            上下文
	 * @since 从最初版本添加。
	 */
	public static void setRotateAnimation(final View v, Context contect, int animId) {
		Animation operatingAnim = AnimationUtils.loadAnimation(contect, animId);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		operatingAnim.setFillAfter(true);
		v.startAnimation(operatingAnim);
	}
}
