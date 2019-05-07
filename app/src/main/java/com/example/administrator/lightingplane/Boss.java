package com.example.administrator.lightingplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class Boss extends Plane {

	public int STEPX = 3;//
	int maxHealth = 1;
	public Boss(Context context, int screenWidth, int screenHeight,
			Bitmap[] planePics) {
		super(context, screenWidth, screenHeight, planePics);
		// TODO Auto-generated constructor stub
		shotStyle = 1;
		health = 1000;
		lives = 1;
		state = 2;

		for(Bullet bullet:bullets){
			bullet.state = 2;
			bullet.belongTo = false;
			bullet.step = -10;
			bullet.bulletPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.bossbullet);
		}
	}
	
	
	@Override
	public void move(Canvas canvas, Paint paint, int moveToX, int moveToY) {
		// TODO Auto-generated method stub
		impact();
		if(health <= 0){
			if(!animation.isEnd)
				animation.start(canvas, paint, nowX, nowY);
			else{
				state = 2;
				animation.isEnd = false;
			}
		}else{
			Rect rect = new Rect(0, 0, screenWidth*health/maxHealth, 10);
			paint.setColor(Color.RED);
			paint.setStyle(Style.FILL);
			canvas.drawRect(rect, paint);
			
			switch(moveStyle){
			case 0://boss�ƶ��켣
				if(nowX <= 0 || nowX >= screenWidth - width){
					STEP = -STEP;
				}
				nowX += STEP;
				break;
			case 1://boos�ƶ��켣
				if(nowX <= 0 || nowX >= screenWidth - width){
					STEP = -STEP;
				}
				nowX += STEP;
				
				if(nowY <= 0 || nowY >= 60){
					STEPX = -STEPX;
				}
				nowY += STEPX;
				break;
			}
			
			canvas.drawBitmap(planePics[0], nowX, nowY, paint);
		}
		
		//�ӵ��ƶ�
		bulletsMove(canvas,paint);
	}
	
	

	public void impact() {
		for(Plane enemy:enemys){
			if(enemy.state == 1){
				if((nowX > enemy.nowX && nowX < (enemy.nowX + enemy.width) && nowY > enemy.nowY && nowY < (enemy.nowY + enemy.height))
						|| ((nowX + width) > enemy.nowX && (nowX + width) < (enemy.nowX + enemy.width) && nowY > enemy.nowY && nowY < (enemy.nowY + enemy.height))
						|| (nowX > enemy.nowX && nowX < (enemy.nowX + enemy.width) && (nowY + height) > enemy.nowY && (nowY + height) < (enemy.nowY + enemy.height))
						|| ((nowX+width) > enemy.nowX && (nowX+width) < (enemy.nowX + enemy.width) && (nowY + height) > enemy.nowY && (nowY + height) < (enemy.nowY + enemy.height))){
					health -= 10;
					enemy.health -= 10;
					if(enemy.health <= 0 && FinalPlaneActivity.soundFlag){
						FinalPlaneActivity.bombMusic.start();
					}
				}
			}
		}
	}
	
	@Override
	public void reset(){
		nowY = 1;
		nowX = (screenWidth - width)/2;
		state = 1;
		health = 1;
	}

}
