package com.example.administrator.lightingplane;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 奖励对象
 */
public class Award{
	Context context;
	int nowX;
	int nowY;
	int screenWidth;
	int screenHeight;
	int width;
	int height;
	Bitmap[] awardPics = new Bitmap[5];
	int style;//奖励的类型
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
		impact();//碰撞检测
		if(state){
			//移动
			if(nowX <= 0 || nowX >= screenWidth - width){
				STEPX = -STEPX;
			}
			nowY -= STEPY;
			nowX += STEPX;
			canvas.drawBitmap(awardPics[style], nowX, nowY, paint);
		}else{
			//重置
			reset();
		}

		if(nowY > screenHeight){
			state = false;
		}
	}

	public void impact(){
		if(state && FightingView.plane.state == 1){
			if((nowX > FightingView.plane.nowX && nowX < (FightingView.plane.nowX + FightingView.plane.width) && nowY > FightingView.plane.nowY && nowY < (FightingView.plane.nowY + FightingView.plane.height))
					|| ((nowX + width) > FightingView.plane.nowX && (nowX + width) < (FightingView.plane.nowX + FightingView.plane.width) && nowY > FightingView.plane.nowY && nowY < (FightingView.plane.nowY + FightingView.plane.height))
					|| (nowX > FightingView.plane.nowX && nowX < (FightingView.plane.nowX + FightingView.plane.width) && (nowY + height) > FightingView.plane.nowY && (nowY + height) < (FightingView.plane.nowY + FightingView.plane.height))
					|| ((nowX+width) > FightingView.plane.nowX && (nowX+width) < (FightingView.plane.nowX + FightingView.plane.width) && (nowY + height) > FightingView.plane.nowY && (nowY + height) < (FightingView.plane.nowY + FightingView.plane.height))){
				state = false;
				switch(style){
					case 0:
						if(FightingView.plane.bomb < 5)
							FightingView.plane.bomb++;
						break;
					case 1:
						if(FightingView.plane.health<100){
							FightingView.plane.health += 10;
						}
						break;
					case 2:
						if(FightingView.plane.lives < 5){
							FightingView.plane.lives++;
						}
						break;
					case 3:
						if(FightingView.plane.bullets.get(0).damage < 50){
							for(Bullet bullet:FightingView.plane.bullets){
								bullet.damage += 10;
							}
						}
						if(FightingView.plane.shotStyle < 5)
							FightingView.plane.shotStyle ++;
						break;
					case 4:
						if(FightingView.plane.STEP < 30){
							FightingView.plane.STEP += 5;
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
