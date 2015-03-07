package com.adolf.shrewmouses.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public abstract class MyAnimationDrawable extends AnimationDrawable {
    Handler finishHandler;      // �жϽ�����Handler
    public MyAnimationDrawable(AnimationDrawable ad) {
        // ������Լ���ÿһ֡�ӽ�ȥ
        for (int i = 0; i < ad.getNumberOfFrames(); i++) {

            this.addFrame(ad.getFrame(i), ad.getDuration(i));
        }
    }
    @Override
    public void start() {
        super.start();
        /**
         * �����ø����start()
         * Ȼ�������̣߳�������onAnimationEnd()
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
     * ���������ö����ĳ���ʱ�䣨֮�����onAnimationEnd()��
     */
    public int getTotalDuration() {
        int durationTime = 0;
        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            durationTime += this.getDuration(i);
        }
        return durationTime;
    }
    /**
     * ����ʱ���õķ�����һ��Ҫʵ��
     */
    abstract void onAnimationEnd();
}


////�½����ǵ����ʵ��
//MyAnimationDrawable mad = new MyAnimationDrawable(
// (AnimationDrawable) getResources().getDrawable(R.drawable.anim1)) {
//     @Override
//     void onAnimationEnd() {
//         // ʵ���������������������
//     }
// };
////������������͸�����ĳ��ImageView
//iv.setBackgroundDrawable(mad);
////��ʼ��
//mad.start();


