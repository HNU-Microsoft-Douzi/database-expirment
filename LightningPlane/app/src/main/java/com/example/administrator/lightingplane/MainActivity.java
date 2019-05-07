package com.example.administrator.lightingplane;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	Button btnStart = null;
	Button btnRank = null;
	Button btnSet = null;
	Button btnOut = null;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        btnStart = (Button) findViewById(R.id.btn_Start);
        btnRank = (Button) findViewById(R.id.btn_Rank);
        btnSet = (Button) findViewById(R.id.btn_Set);
        btnOut = (Button) findViewById(R.id.btn_Out);
        btnStart.setOnClickListener(this);
        btnRank.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        btnOut.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == btnStart){
			Intent intent = new Intent(this, FinalPlaneActivity.class);
			startActivity(intent);
		}else if(view == btnRank){
			Intent intent = new Intent(this, Rank.class);
			startActivity(intent);
		}else if(view == btnSet){
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
		}else if(view == btnOut){
			Fighting.flag = false;
			Fighting.pause = true;
			finish();
		}
	}
	
}
