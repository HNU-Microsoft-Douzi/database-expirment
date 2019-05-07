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
	public int nowX;//��ǰλ�õ�X����
	public int nowY;//��ǰλ�õ�Y����
	public int width;//�ɻ��Ŀ�
	public int height;//�ɻ��ĸ�
	public int state;//0��������ը״̬��1������״̬��2���ɱ�����״̬
	public int health;//Ѫ��
	public int lives;//����
	public int STEP;//�ɻ����ƶ��ٶ�
	public Bitmap[] planePics = new Bitmap[3];//�ɻ�������
	public List<Bullet> bullets = new ArrayList<Bullet>();//�ɻ����ӵ���������
	public int bulletCount = 30;
	public Animation animation = null;//�ɻ�������ըʱ�Ķ�������
	public int moveStyle;
	
	public int shotFlag;//�ɻ������ӵ��ı�־λ
	public int shotInterval = 10;//�ɻ������ӵ��ļ��ʱ��shotInterval*50����
	public int shotStyle = 1;//�ɻ������ӵ���ģʽ,һ�η����ӵ��ĸ���
	
	public int screenWidth;//��Ļ�Ŀ�
	public int screenHeight;//��Ļ�ĸ�
	public Context context;
	List<Plane> enemys = new ArrayList<Plane>();
	Boss boss;
	int bomb = 3;//�ɻ��е�ը����
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
		//��ʼ��ը��ͼƬ
		bombPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
		bombPic = Bitmap.createScaledBitmap(bombPic, 10, 10, false);
		myLives = BitmapFactory.decodeResource(context.getResources(), R.drawable.myplane);
		myLives = Bitmap.createScaledBitmap(myLives, 10, 10, false);
		//��ʼ���ɻ��ӵ�
		//��ʼ���ӵ�ͼƬ
		Bitmap bulletPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.myzd1);
		//��ʼ���ӵ���ըͼƬ
		List<Bitmap> btDestroyPics = new ArrayList<Bitmap>();
		btDestroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blastz1));
		btDestroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blastz2));
		btDestroyPics.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blastz3));
		for(int i = 0; i < bulletCount; i++){
		Bullet bullet = new Bullet(bulletPic, btDestroyPics);
			bullets.add(bullet);
		}
		
		//��ʼ���ɻ���ըͼƬ
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
	 * ���ɻ�����Ŀ��
	 */
	public void setTarget(){
		for(Bullet bullet:bullets){
			bullet.enemys = enemys;
			bullet.boss = boss;
		}
	}
	
	
	/**
	 * �ƶ��ɻ��ͷɻ����ӵ�,�����ɻ��ı�ը
	 * @param canvas ����
	 * @param paint ����
	 * @param moveToX Ҫ�ƶ�����λ�õ�X����
	 * @param moveToY Ҫ�ƶ�����λ�õ�Y����
	 */
	public void move(Canvas canvas, Paint paint, int moveToX,int moveToY){
		//impact();//��ײ���
		if(health <= 0){
			if(!animation.isEnd)
				animation.start(canvas, paint, nowX, nowY);
			else{
				state = 2;
				animation.isEnd = false;
				//�ɻ���������0�����������������
				if(lives > 0)
					reset();
			}
		}else{
			//��Ѫ��
			paint.setStyle(Style.FILL);
			paint.setColor(Color.WHITE);
			Rect rect1 = new Rect(screenWidth/3, screenHeight-20, screenWidth*2/3, screenHeight-10);
			canvas.drawRect(rect1, paint);//��Ѫ��
			Rect rect = new Rect(screenWidth/3, screenHeight-20, screenWidth/3+(screenWidth/3)*health/100, screenHeight-10);
			paint.setColor(Color.RED);
			paint.setStyle(Style.FILL);
			canvas.drawRect(rect, paint);//��Ѫ��
			canvas.drawBitmap(bombPic, 5, screenHeight-bombPic.getHeight()-5, paint);//��ը��ͼ��
			canvas.drawText(":"+bomb, bombPic.getWidth()+10, screenHeight-5, paint);//��ը����
			for(int i = 1; i <= lives; i++){
				canvas.drawBitmap(myLives, screenWidth - myLives.getWidth()*i, screenHeight-5-myLives.getHeight(), paint);
			}
			
			
			int i = 0;//�ƶ�ʱ�ɻ���ʾ��ͼƬ�����е��±�
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
	 * �ɻ��е��ӵ��ƶ�������
	 */
	public void bulletsMove(Canvas canvas, Paint paint){
		
		//�ӵ��ƶ�
		for(int i = 0; i< bullets.size(); i++){
			bullets.get(i).move(canvas, paint, screenWidth, screenHeight);
		}
		
		//�ɻ����ڵ�����·����ӵ�,ÿ��shotInteval*50���뷢��һ��
		if(shotFlag == 0 && state == 1){
			if(bullets.get(0).belongTo && FinalPlaneActivity.soundFlag)
				FinalPlaneActivity.shotMusic.start();
			switch(shotStyle){
			case 1://����һö�ӵ�
				for(Bullet bullet:bullets){
					if(bullet.state==2){
						bullet.reset(nowX + width/2, nowY, 1);
						
						break;
					}
				}
				break;
			case 2://������ö�ӵ�
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
			case 3://������ö�ӵ�
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
//	 * ��ײ���
//	 * @param enemys �л�Ⱥ
//	 * @param boss ���
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
	 * ���÷ɻ�
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
