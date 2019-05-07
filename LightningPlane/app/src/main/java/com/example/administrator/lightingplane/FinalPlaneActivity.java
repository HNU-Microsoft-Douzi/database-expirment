package com.example.administrator.lightingplane;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class FinalPlaneActivity extends Activity {
    /** Called when the activity is first created. */
	FrameLayout frame;
	final static int PAUSE = 1;
	final static int STOP = 0;
	public static boolean backMusicFlag = true;
	public static boolean soundFlag = false;
	boolean flag = true;
	Fighting fighting = null;
	static MediaPlayer backMusic = null;
	static MediaPlayer shotMusic = null;
	static MediaPlayer bombMusic = null;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case PAUSE:
				Fighting.pause = true;
				Fighting.flag = false;
				
				Intent intent = new Intent(FinalPlaneActivity.this,Rank.class);
				intent.putExtra("score", Fighting.score);
				Fighting.num = 0;
				Fighting.score = 0;
				FinalPlaneActivity.this.startActivityForResult(intent, 1);
				
				FinalPlaneActivity.this.finish();
				
				break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //ȫ����ʾ����
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    		WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	//��ȡ��ǰ��Ļ������
        Display display = getWindowManager().getDefaultDisplay();
        backMusic = MediaPlayer.create(this, R.raw.back);
        shotMusic = MediaPlayer.create(this, R.raw.bullet);
        bombMusic = MediaPlayer.create(this, R.raw.bomb);
        backMusic.setLooping(true);
        SharedPreferences share = getSharedPreferences("test", Context.MODE_PRIVATE+Context.MODE_PRIVATE);
        backMusicFlag = share.getBoolean("backMusicFlag", true);
        soundFlag = share.getBoolean("soundFlag", true);
        
        frame = new FrameLayout(this);
        fighting = new Fighting(this, display.getWidth(), display.getHeight());
        frame.addView(fighting);

        setContentView(frame);
        
        Thread thread = new Thread(){
        	
        	public void run() {
        		while(flag){
        			if(fighting.round == 6){
        				Fighting.pause = true;
        				handler.sendEmptyMessage(PAUSE);
        				flag = false;
        			}else if(Fighting.plane.lives <= 0){
        				handler.sendEmptyMessage(PAUSE);
        				flag = false;
        			}
        		}
        	};
        };
        thread.start();
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	Log.i("wy", "ս����ͣ");
    	Fighting.pause = true;//ս����ͣ
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	Log.i("wy", "ս��ֹͣ");
    	Fighting.flag = false;//�߳���ֹ
    	fighting.num = 0;
    	fighting.round = 1;
    	fighting.score = 0;
    	backMusic.stop();
    	flag = false;
    }
}