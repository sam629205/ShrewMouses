package com.adolf.shrewmouses.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public abstract class MyAnimationDrawable extends AnimationDrawable {
    Handler finishHandler;      // 判断结束的Handler
    public MyAnimationDrawable(AnimationDrawable ad) {
        // 这里得自己把每一帧加进去
        for (int i = 0; i < ad.getNumberOfFrames(); i++) {

            this.addFrame(ad.getFrame(i), ad.getDuration(i));
        }
    }
    @Override
    public void start() {
        super.start();
        /**
         * 首先用父类的start()
         * 然后启动线程，来调用onAnimationEnd()
         */
        finishHandler = new Handler();
        finishHandler.postDelayed(
            new Runnable() {
                public void run() {
                    onAnimationEnd();
                }
            }, (getTotalDuration()*2));
    }
    /**
     * 这个方法获得动画的持续时间（之后调用onAnimationEnd()）
     */
    public int getTotalDuration() {
        int durationTime = 0;
        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            durationTime += this.getDuration(i);
        }
        return durationTime;
    }
    /**
     * 结束时调用的方法，一定要实现
     */
    abstract void onAnimationEnd();
}


////新建我们的类的实例
//MyAnimationDrawable mad = new MyAnimationDrawable(
// (AnimationDrawable) getResources().getDrawable(R.drawable.anim1)) {
//     @Override
//     void onAnimationEnd() {
//         // 实现这个方法，结束后会调用
//     }
// };
////把这个动画“赐福”给某个ImageView
//iv.setBackgroundDrawable(mad);
////开始吧
//mad.start();


