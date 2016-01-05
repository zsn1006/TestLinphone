package org.linphone.newphone.tools;

import android.content.Context;

/**
 * Density方法
 * @author ty
 * @time 2015/5/12
 */
public class MethodDensity {

	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
    /** 
     * 根据手机的分辨率从 sp 的单位 转成为 px 
     */
    public static int sp2px(Context context, float spValue) { 
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 

    /** 
     * 根据手机的分辨率从 px  的单位 转成为 sp 
     */
    public static int px2sp(Context context, float pxValue) { 
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    }
}
