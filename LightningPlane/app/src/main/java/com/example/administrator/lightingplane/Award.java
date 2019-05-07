package com.example.administrator.lightingplane;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Award{
	Context context;
	int nowX;
	int nowY;
	int screenWidth;
	int screenHeight;
	int width;
	int height;
	Bitmap[] awardPics = new Bitmap[5];
	int style;//����������
	int STEPX = 5;
	int STEPY = -5;
	boolean state = false;
	
	public Award(Context context, int screenWidth, int screenHeight){
		this.context = context;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		awardPics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.addbomb);
		awardPics[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.addhealth);
		awardPics[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.addlife);
		awardPics[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.addpower);
		awardPics[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.addspeed);
		width = awardPics[0].getWidth();
		height = awardPics[1].getHeight();
	}
	
	public void move(Canvas canvas, Paint paint){
		impact();//��ײ���
		if(state){
			//�ƶ�
			if(nowX <= 0 || nowX >= screenWidth - width){
				STEPX = -STEPX;
			}
			nowY -= STEPY;
			nowX += STEPX;
			canvas.drawBitmap(awardPics[style], nowX, nowY, paint);
		}else{
			//����
			reset();
		}
		
		if(nowY > screenHeight){
			state = false;
		}
	}
	
	public void impact(){
		if(state && Fighting.plane.state == 1){
			if((nowX > Fighting.plane.nowX && nowX < (Fighting.plane.nowX + Fighting.plane.width) && nowY > Fighting.plane.nowY && nowY < (Fighting.plane.nowY + Fighting.plane.height))
					|| ((nowX + width) > Fighting.plane.nowX && (nowX + width) < (Fighting.plane.nowX + Fighting.plane.width) && nowY > Fighting.plane.nowY && nowY < (Fighting.plane.nowY + Fighting.plane.height))
					|| (nowX > Fighting.plane.nowX && nowX < (Fighting.plane.nowX + Fighting.plane.width) && (nowY + height) > Fighting.plane.nowY && (nowY + height) < (Fighting.plane.nowY + Fighting.plane.height))
					|| ((nowX+width) > Fighting.plane.nowX && (nowX+width) < (Fighting.plane.nowX + Fighting.plane.width) && (nowY + height) > Fighting.plane.nowY && (nowY + height) < (Fighting.plane.nowY + Fighting.plane.height))){
				state = false;
				switch(style){
				case 0:
					if(Fighting.plane.bomb < 5)
						Fighting.plane.bomb++;
					break;
				case 1:
					if(Fighting.plane.health<100){
						Fighting.plane.health += 10;
					}
					break;
				case 2:
					if(Fighting.plane.lives < 5){
						Fighting.plane.lives++;
					}
					break;
				case 3:
					if(Fighting.plane.bullets.get(0).damage < 50){
						for(Bullet bullet:Fighting.plane.bullets){
							bullet.damage += 10;
						}
					}
					if(Fighting.plane.shotStyle < 5)
						Fighting.plane.shotStyle ++;
					break;
				case 4:
					if(Fighting.plane.STEP < 30){
						Fighting.plane.STEP += 5;
					}
				}
			}
		}
	}
	
	public void reset(){
		Random random = new Random();
		nowX = Math.abs(random.nextInt()%(screenWidth-width/2) + 1);
		nowY = -Math.abs(random.nextInt()%(screenHeight));
		style = Math.abs(random.nextInt()%5);
		state = true;
	}
}
