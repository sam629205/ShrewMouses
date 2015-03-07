package com.adolf.shrewmouses.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.Inflater;

import com.adolf.shrewmouses.R;
import com.adolf.shrewmouses.utils.Constants;
import com.adolf.shrewmouses.utils.Preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ImgeList extends LinearLayout {

	private Handler handler;
	private MyThread thread;
	private ImageView[] array_imge;
	private int[] ImgeId;
	private Context context;
	private boolean isEnd;
	private boolean isGameOver = false;
	private boolean isClick = false;
	private MyAnimationDrawable mad = null;
	private List<Integer> animList = new ArrayList<Integer>();
	private int animID;
	private long timespan = 1500;
	private SoundPool sp;
	private Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	private MyAnimationDrawable doubleAnim = null;
	private MyAnimationDrawable lidayeAnim = null;
	private PopupWindow popWindow;
	private View popView;
	
	public ImgeList(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		soundMap.put(0, sp.load(context, R.raw.double_score,1));
		soundMap.put(1, sp.load(context, R.raw.new_record,1));
		soundMap.put(2, sp.load(context, R.raw.lidaye,1));
		// 在构造函数中将Xml中定义的布局解析出来。
		LayoutInflater.from(context).inflate(R.layout.grid_imge, this, true);
		ImgeId = new int[] { R.id.imge1, R.id.imge2, R.id.imge3, R.id.imge4,
				R.id.imge5, R.id.imge6, R.id.imge7, R.id.imge8, R.id.imge9 };
		array_imge = new ImageView[9];
		for (int i = 0; i < ImgeId.length; i++) {
			ImageView imge = (ImageView) findViewById(ImgeId[i]);
			imge.setOnClickListener(ocl);
			array_imge[i] = imge;
		}
		animList.add(R.anim.imge1);
		animList.add(R.anim.imge2);
		animList.add(R.anim.imge3);
		animList.add(R.anim.imge4);
	}

	/**
	 * 开启线程
	 * */
	public void initialize() {

		handler = new Handler();
		thread = new MyThread();
		handler.postDelayed(thread, timespan);
	}

	/**
	 * 设置文本
	 * 
	 * @param count
	 *            击中次数
	 * @param remain
	 *            剩余次数
	 * */
	public void into() {
		if (MainActivity.remain != null && MainActivity.count != null) {

			MainActivity.remain
					.setText(Helper.remain + "/" + Helper.max_remain);
			MainActivity.count.setText(Helper.counts+"");
		}

	}

	int id = -1;

	OnClickListener ocl = new OnClickListener() {

		@Override
		public void onClick(View v) {

			for (int i = 0; i < ImgeId.length; i++) {
				if (ImgeId[i] == v.getId()) {
					id = i;
					break;
				}

			}
			if (animID==animList.get(animList.size()-1)) {
				isGameOver=true;
			}
			boolean m = isEnd;
			int m1 = count;
			int m2 = id;
			if (count == id && !isEnd) {
				array_imge[count].setBackgroundResource(R.drawable.susliks5);
				//双倍得分
				if (animID == animList.get(0)) {
					Helper.counts += 2;
					doubleAnim = new MyAnimationDrawable((AnimationDrawable) getResources()
							.getDrawable(R.anim.double_score)) {
						@Override
						void onAnimationEnd() {
						}
					};
					MainActivity.iv_double.setBackgroundDrawable(doubleAnim);
					doubleAnim.start();
					playSound(soundMap.get(0));
				}else {
					Helper.counts += 1;
				}
				if (Helper.counts%15==0&&timespan>800) {
					timespan = timespan - 100;
				}
				isEnd = true;
			} else if (animID!=animList.get(animList.size()-1)) {
				Helper.remain += 1;
			}
			into();
			isClick = true;

		}
	};
	private int count;

	private class MyThread implements Runnable {

		@Override
		public void run() {

			String[] co = MainActivity.count.getText().toString().split("/");
			String[] re = MainActivity.remain.getText().toString().split("/");
//			if (Integer.parseInt(co[0]) == Helper.max_counts) {
//				new AlertDialog.Builder(context)
//						.setTitle("游戏提示")
//						.setMessage("真厉害！点击确定继续挑战！")
//						.setPositiveButton("确定",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										Helper.counts = 0;
//										Helper.remain = 0;
//										into();
//										handler.postDelayed(thread, timespan);
//									}
//								}).show();
//
//				return;
			 if (isGameOver) {
				timespan = 1500;
				if (Preference.getInt(Constants.RECORD)<Helper.counts) {
					Preference.putInt(Constants.RECORD, Helper.counts);
					MainActivity.record.setText(Helper.counts+"");
					popView = View.inflate(context, R.layout.record_pop, null);
					((Button)popView.findViewById(R.id.btn_ok)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							popWindow.dismiss();
							Helper.counts = 0;
							Helper.remain = 0;
							into();
							handler.postDelayed(thread, timespan);
						}
					});
					popWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					popWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
					((TextView)popView.findViewById(R.id.tv_content)).setText("你太给力了，你得了"+Helper.counts+"分，创造了全新记录！");
					playSound(soundMap.get(1));
				}else {
					//打到李大爷了
					popView = View.inflate(context, R.layout.lidaye_pop, null);
					((Button)popView.findViewById(R.id.btn_ok)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							popWindow.dismiss();
							Helper.counts = 0;
							Helper.remain = 0;
							into();
							handler.postDelayed(thread, timespan);
						}
					});
					lidayeAnim = new MyAnimationDrawable((AnimationDrawable) getResources()
							.getDrawable(R.anim.lidaye)) {
						
						@Override
						void onAnimationEnd() {
						}
					};
					
					popWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					popWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
					((ImageView)popView.findViewById(R.id.iv_pop)).setBackgroundDrawable(lidayeAnim);
					lidayeAnim.start();
					((TextView)popView.findViewById(R.id.tv_content)).setText("你打到李大爷，你挂了！点击确定继续打瓜瓜。");
					playSound(soundMap.get(2));
				}
				isGameOver=false;
		return;
	}else if (Integer.parseInt(re[0]) == Helper.max_remain) {

				timespan = 1500;
				if (Preference.getInt(Constants.RECORD)<Helper.counts) {
					Preference.putInt(Constants.RECORD, Helper.counts);
					MainActivity.record.setText(Helper.counts+"");
					popWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
					((TextView)popView.findViewById(R.id.tv_content)).setText("你太给力了，你得了"+Helper.counts+"分，创造了全新记录！");
					playSound(soundMap.get(1));
				}else {
					new AlertDialog.Builder(context)
					.setTitle("游戏提示")
					.setMessage("好可惜哦，你挂了！点击确定继续打瓜瓜！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Helper.counts = 0;
									Helper.remain = 0;
									into();
									handler.postDelayed(thread, timespan);
								}
							}).show();
				}
				return;
			} else {

				if (isClick) {

					isClick = false;

				} else if (animID!=animList.get(animList.size()-1)) {
					Helper.remain += 1;

					into();
				}
				array_imge[count].setBackgroundResource(R.drawable.susliks0);
				handler.postDelayed(thread, timespan);
				play();
			}
		}

	}

	public void detelethread() {
		handler.removeCallbacks(thread);
	}

	/**
	 * 播放图片（伸出地鼠头）
	 * */
	private void play() {

		Random r = new Random();
		// 保证不会出现相邻俩个随机数相同
		while (true) {
			int random = r.nextInt(9);
			if (random != count) {
				count = random;
				break;
			}
		}
		Random random = new Random();
		int mNum = random.nextInt(animList.size());
		animID = animList.get(mNum);
		mad = new MyAnimationDrawable((AnimationDrawable) getResources()
				.getDrawable(animID)) {
			@Override
			void onAnimationEnd() {
				// 实现这个方法，结束后会调用

				isEnd = true;

			}
		};
		// 把这个动画“赐福”给某个ImageView
		array_imge[count].setBackgroundDrawable(mad);
		// 开始吧
		mad.start();
		isEnd = false;
		isClick = false;
		// draw = (AnimationDrawable) array_imge[count].getDrawable();
		//
		// draw.start();

	}
	public void playSound(int sound) {
		        AudioManager am = (AudioManager) context
		                .getSystemService(Context.AUDIO_SERVICE);
		        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		        float volumnRatio = volumnCurrent / audioMaxVolumn;
		
		        sp.play(sound, volumnRatio, volumnRatio, 1, 0,  1f);
		     }

	public ImgeList(Context context) {

		super(context);

	}

}
