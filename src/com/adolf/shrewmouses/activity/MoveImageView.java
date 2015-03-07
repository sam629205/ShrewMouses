package com.adolf.shrewmouses.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MoveImageView extends ImageView { 
	  
    public MoveImageView(Context context) { 
        super(context); 
    } 
  
    public MoveImageView(Context context, AttributeSet attrs) { 
        super(context, attrs, 0); 
    } 
  
    public MoveImageView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
  
    public void setLocation(int x, int y) { 
        this.setFrame(x, y - this.getHeight(), x + this.getWidth(), y); 
    } 
  
    // ÒÆ¶¯ 
    public boolean autoMouse(MotionEvent event,int w,int h) { 
        boolean rb = false; 
        switch (event.getAction()) { 
        case MotionEvent.ACTION_DOWN: 
        	Log.i("TAG", event.getX()+"---X");
           	Log.i("TAG", event.getY()+"---Y");
            this.setLocation((int) event.getX()+w, (int) event.getY()+h); 
            rb = true; 
            break; 
        } 
        return rb; 
    } 
}
