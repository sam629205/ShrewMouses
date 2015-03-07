package com.adolf.shrewmouses.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.shrewmouses.R;
import com.adolf.shrewmouses.utils.Constants;
import com.adolf.shrewmouses.utils.Preference;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

public class MainActivity extends Activity {

	public static TextView count;
	public static TextView remain;
	public static TextView record;
	public static ImageView iv_double;
	private ImgeList imgeview;
	private MoveImageView  hammer;
	int screenHeight;// 屏幕高度
	int screenWidth;// 屏幕宽度
	private SoundPool sp;
	private Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置成全屏模式
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//友盟更新
    	UmengUpdateAgent.setDefault();
    	UmengUpdateAgent.forceUpdate(MainActivity.this);
    	UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
    						
    			@Override
    			public void onClick(int arg0) {
    			if (arg0==UpdateStatus.NotNow) {
    						finish();
    				}
    							
    			}
    		});
		imgeview = (ImgeList) findViewById(R.id.imgelist);
		hammer=(MoveImageView)findViewById(R.id.hammer);
		count = (TextView) findViewById(R.id.count2);
		remain = (TextView) findViewById(R.id.remain2);
		record = (TextView) findViewById(R.id.record2);
		iv_double = (ImageView) findViewById(R.id.iv_score);
		imgeview.into();	
		imgeview.initialize();
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		soundMap.put(0, sp.load(MainActivity.this, R.raw.music_bg,1));
		playSound(soundMap.get(0));
		record.setText(Preference.getInt(Constants.RECORD)+"");

		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		
		imgeview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.i("TAG",(screenWidth)+"--sW");
				Log.i("TAG",(screenHeight)+"--sH");
				int w=((screenWidth-imgeview.getWidth())/2);
				int h=((screenHeight-imgeview.getHeight())/2);
				hammer.autoMouse(event,w,h);
				return true;
			}
		});
		
	}
	


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//hammer.autoMouse(event,w,h);
		return true;
	}


	public void playSound(int sound) {
        AudioManager am = (AudioManager) MainActivity.this
                .getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = volumnCurrent / audioMaxVolumn;
        //0不循环 -1循环
        sp.play(sound, volumnRatio, volumnRatio, 1, -1,  1f);
     }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
