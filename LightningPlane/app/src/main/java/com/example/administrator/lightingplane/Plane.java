package com.example.administrator.lightingplane;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class Plane {
	public int nowX;//当前位置的X坐标
	public int nowY;//当前位置的Y坐标
	public int width;//飞机的宽
	public int height;//飞机的高
	public int state;//0：死亡爆炸状态，1：生存状态，2：可被重置状态
	public int health;//血量
	public int lives;//命数
	public int STEP;//飞机的移动速度
	public Bitmap[] planePics = new Bitmap[3];//飞机的样子
	public List<Bullet> bullets = new ArrayList<Bullet>();//飞机的子弹对象数组
	public int bulletCount = 30;
	public Animation animation = null;//飞机死亡爆炸时的动画对象
	public int moveStyle;

	public int shotFlag;//飞机发射子弹的标志位
	public int shotInterval = 10;//飞机发射子弹的间隔时间shotInterval*50毫秒
	public int shotStyle = 1;//飞机发射子弹的模式,一次发射子弹的个数

	public int screenWidth;//屏幕的宽
	public int screenHeight;//屏幕的高
	public Context context;
	List<Plane> enemys = new ArrayList<Plane>();
	Boss boss;
	int bomb = 3;//飞机中的炸弹数
	Bitmap bombPic;
	Bitmap myLives;

	public Plane(Context context, int screenWidth, int screenHeight, Bitmap[] planePics){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		nowX = screenWidth/2;
		nowY = screenHeight*2/3;
		STEP = 10;
		health = 100;
		lives = 5;
		state = 1;
		this.planePics = planePics;
		width = planePics[1].getWidth();
		height = planePics[1].getHeight();
		//this.bullets = bullets;
		this.context = context;
		//初始化炸弹图片
		bombPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
		bombPic = Bitmap.createScaledBitmap(bombPic, 10, 10, false);
		myLives = BitmapFactory.decodeResource(context.getResources(), R.drawable.myplane);
		myLives = Bitmap.createScaledBitmap(myLives, 10, 10, false);
		//初始化飞机子弹
		//初始化子弹图片
		Bitmap bulletPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.myzd1);
		//初始化子弹爆炸图片
		List<Bitmap> btDestroyPics = new ArrayList<Bitmap>();
		btDestroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blastz1));
		btDestroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blastz2));
		btDestroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blastz3));
		for(int i = 0; i < bulletCount; i++){
			Bullet bullet = new Bullet(bulletPic, btDestroyPics);
			bullets.add(bullet);
		}

		//初始化飞机爆炸图片
		List<Bitmap> destroyPics = new ArrayList<Bitmap>();
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts1));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts2));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts3));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts4));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts5));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts6));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts7));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts8));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts9));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts10));
		destroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blasts11));
		animation = new Animation(destroyPics);

	}

	/**
	 * 给飞机设置目标
	 */
	public void setTarget(){
		for(Bullet bullet:bullets){
			bullet.enemys = enemys;
			bullet.boss = boss;
		}
	}


	/**
	 * 移动飞机和飞机的子弹,包括飞机的爆炸
	 * @param canvas 画布
	 * @param paint 画笔
	 * @param moveToX 要移动到的位置的X坐标
	 * @param moveToY 要移动到的位置的Y坐标
	 */
	public void move(Canvas canvas, Paint paint, int moveToX,int moveToY){
		//impact();//碰撞检测
		if(health <= 0){
			if(!animation.isEnd)
				animation.start(canvas, paint, nowX, nowY);
			else{
				state = 2;
				animation.isEnd = false;
				//飞机命数大于0的情况下死亡后重置
				if(lives > 0)
					reset();
			}
		}else{
			//画血条
			paint.setStyle(Style.FILL);
			paint.setColor(Color.WHITE);
			Rect rect1 = new Rect(screenWidth/3, screenHeight-20, screenWidth*2/3, screenHeight-10);
			canvas.drawRect(rect1, paint);//画血槽
			Rect rect = new Rect(screenWidth/3, screenHeight-20, screenWidth/3+(screenWidth/3)*health/100, screenHeight-10);
			paint.setColor(Color.RED);
			paint.setStyle(Style.FILL);
			canvas.drawRect(rect, paint);//画血量
			canvas.drawBitmap(bombPic, 5, screenHeight-bombPic.getHeight()-5, paint);//画炸弹图标
			canvas.drawText(":"+bomb, bombPic.getWidth()+10, screenHeight-5, paint);//画炸弹数
			for(int i = 1; i <= lives; i++){
				canvas.drawBitmap(myLives, screenWidth - myLives.getWidth()*i, screenHeight-5-myLives.getHeight(), paint);
			}


			int i = 0;//移动时飞机显示的图片集合中的下标
			if(Math.abs(moveToX-nowX)<STEP){
				nowX = moveToX;
				i = 0;
			}
			else if(moveToX > nowX && moveToX < screenWidth && moveToX > 0){
				nowX += STEP;
				i = 2;
			}
			else if(moveToX < nowX && moveToX < screenWidth && moveToX > 0){
				nowX -= STEP;
				i = 1;
			}

			if(Math.abs(moveToY-nowY)<STEP){
				nowY = moveToY;
			}
			else if(moveToY > nowY && moveToY < screenHeight && moveToY > 0)
				nowY += STEP;
			else if(moveToY < nowY && moveToY < screenHeight && moveToY > 0)
				nowY -= STEP;
			canvas.drawBitmap(planePics[i], nowX, nowY, paint);
		}
		bulletsMove(canvas,paint);
	}


	/**
	 * 飞机中的子弹移动和重置
	 */
	public void bulletsMove(Canvas canvas, Paint paint){

		//子弹移动
		for(int i = 0; i< bullets.size(); i++){
			bullets.get(i).move(canvas, paint, screenWidth, screenHeight);
		}

		//飞机存在的情况下发射子弹,每个shotInteval*50毫秒发射一次
		if(shotFlag == 0 && state == 1){
			if(bullets.get(0).belongTo && FinalPlaneActivity.soundFlag)
				FinalPlaneActivity.shotMusic.start();
			switch(shotStyle){
				case 1://发射一枚子弹
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX + width/2, nowY, 1);

							break;
						}
					}
					break;
				case 2://发射两枚子弹
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX-bullet.width + width/2, nowY, 1);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX+bullet.width + width/2, nowY, 1);
							break;
						}
					}
					break;
				case 3://发射三枚子弹
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX + width/2, nowY, 1);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX-bullet.width + width/2, nowY, 2);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX+bullet.width + width/2, nowY, 3);

							break;
						}
					}
					break;

				case 4:
					for(int i = 1; i <= 5; i++){
						for(Bullet bullet:bullets){
							if(bullet.state==2){
								bullet.reset(nowX + width/2, nowY, i);
								break;
							}
						}
					}
					break;

				case 5:
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX + width/2, nowY, 1);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX-bullet.width + width/2, nowY, 1);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX+bullet.width + width/2, nowY, 1);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX-2*bullet.width + width/2, nowY, 1);
							break;
						}
					}
					for(Bullet bullet:bullets){
						if(bullet.state==2){
							bullet.reset(nowX+2*bullet.width + width/2, nowY, 1);
							break;
						}
					}
					break;
			}

			shotFlag++;
		}else if(shotFlag < shotInterval){
			shotFlag++;
		}else{
			shotFlag = 0;
		}
	}


//	/**
//	 * 碰撞检测
//	 * @param enemys 敌机群
//	 * @param boss 大怪
//	 */
//	public void impact(){
//		for(Plane enemy:enemys){
//			if(enemy.state == 1){
//				if((nowX > enemy.nowX && nowX < (enemy.nowX + enemy.width) && nowY > enemy.nowY && nowY < (enemy.nowY + enemy.health))
//						|| ((nowX+width) > enemy.nowX && (nowX+width) < (enemy.nowX + enemy.width) && (nowY + height) > enemy.nowY && (nowY + height) < (enemy.nowY + enemy.health))){
//					health -= 10;
//					enemy.health -= 10;
//				}
//			}
//		}
//
//		if(boss.state == 1){
//			if((nowX > boss.nowX && nowX < (boss.nowX + boss.width) && nowY > boss.nowY && nowY < (boss.nowY + boss.health))
//					|| ((nowX+width) > boss.nowX && (nowX+width) < (boss.nowX + boss.width) && (nowY + height) > boss.nowY && (nowY + height) < (boss.nowY + boss.health))){
//				health -= 10;
//			}
//		}
//	}
	/**
	 * 重置飞机
	 */
	public void reset(){
		nowX = screenWidth/2-width/2;
		nowY = screenHeight*2/3;
		health = 100;
		shotInterval = 10;
		STEP = 10;
		state = 1;
		lives--;
		bomb = 3;
		shotStyle = 1;
	}
}
