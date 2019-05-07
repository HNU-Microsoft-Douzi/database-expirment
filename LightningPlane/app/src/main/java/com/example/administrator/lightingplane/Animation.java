package com.example.administrator.lightingplane;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {
	boolean isEnd = false;
	int num;
	List<Bitmap> list;
	
	
	public Animation(List<Bitmap> list){
		this.list = list;
		num = 0;
	}

	/**
	 * 开始绘制的位置
	 * @param canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void start(Canvas canvas,Paint paint, int x, int y){
		if(num<list.size()){
			canvas.drawBitmap(list.get(num), x, y, paint);
			num++;
		}else{
			num = 0;
			isEnd = true;
		}
	}
}
