package com.example.administrator.lightingplane;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * 子弹实体类
 */
public class Bullet {
	public int damage; // 子弹能造成的伤害
	public int style; // 子弹种类
	
	public int nowX; // 子弹的当前位置
	public int nowY;
	
	public Bitmap bulletPic; // 子弹图片
	
	public int width; // 子弹宽度
	public int height; // 子弹高度
	public int step; // 每次移动的像素个数
	public int state;//�ӵ���״̬,0��������ը״̬��1������״̬��2���ɱ�����״̬
	
	public Animation animation;
	List<Plane> enemys;
	Boss boss;
	
	public boolean 	belongTo = true;//��ǰ�ӵ�����˭��trueʱ�������Ƿɻ���false���ڵл�
	
	public Bullet(Bitmap bulletPic, List<Bitmap> destroyPics){
		this.bulletPic = bulletPic;
		animation = new Animation(destroyPics);
		damage = 10;
		style = 0;
		step = 10;
		state = 2;
		width = bulletPic.getWidth();
		height = bulletPic.getHeight();
	}
	
	/**
	 * �ƶ��ӵ�,�ӵ�״̬Ϊ0ʱ��ը��1ʱ�ƶ�
	 * @param canvas ����
	 * @param paint ����
	 * @param screenWidth ��Ļ�Ŀ�
	 * @param screenHeight ��Ļ�ĸ�
	 */
	public void move(Canvas canvas, Paint paint, int screenWidth, int screenHeight){
		impact();//��ײ���
		
		//�ƶ��ӵ���״̬Ϊ0ʱ��ը��Ϊ1ʱ�ƶ�
		if(state == 0){
			if(animation.num < animation.list.size())
				animation.start(canvas, paint, nowX, nowY);
			else{
				state = 2;
				animation.isEnd = false;
			}
		}else if(state == 1){
			switch(style){
			case 1://ֱ�������ƶ�
				nowY -= step;
				break;
			case 2://б�������ƶ�
				nowY -= step;
				nowX -= step/3;
				break;
			case 3://б�������ƶ�
				nowY -= step;
				nowX += step/3;
				break;
			case 4:
				nowY -= step;
				nowX += step/2;
				break;
			case 5:
				nowY -= step;
				nowX -=step/2;
				break;
			}
			canvas.drawBitmap(bulletPic, nowX, nowY, paint);
		}
		
		//�ӵ�������Ļʱ��״̬��2
		if(nowY < 0 || nowY > screenHeight || nowX < 0 || nowX > screenWidth){
			state = 2;
		}
	}
	
	
	/**
	 * �ӵ�����ײ��⣬��л���boss��ײ,��ײʱ�����ӵ���״̬Ϊ0��ͬʱ��ȥ�л���boss����ӦѪ��
	 */
	public void impact(){
		for(Plane enemy:enemys){
			if(enemy.state == 1 && state == 1){
				if((nowX > enemy.nowX && nowX < (enemy.nowX + enemy.width) && nowY > enemy.nowY && nowY < (enemy.nowY + enemy.height))
						|| ((nowX+width) > enemy.nowX && (nowX+width) < (enemy.nowX + enemy.width) && (nowY + height) > enemy.nowY && (nowY + height) < (enemy.nowY + enemy.height))){
					state = 0;
					enemy.health -= damage;
					if(enemy.health <= 0 && FinalPlaneActivity.soundFlag){
						FinalPlaneActivity.bombMusic.start();
					}
					
					if(belongTo){
						Fighting.num++;
						Fighting.score+=10;
						Log.i("wy", "���ел�!�л�Ѫ��:"+enemy.health+",����л���:"+Fighting.num);
					}else{
						Log.i("wy", "������!Ѫ��:"+enemy.health);
					}
				}
			}
		}
		
		if(belongTo){
			if(boss.state == 1 && state == 1){
				if((nowX > boss.nowX && nowX < (boss.nowX + boss.width) && nowY > boss.nowY && nowY < (boss.nowY + boss.height))
						|| ((nowX+width) > boss.nowX && (nowX+width) < (boss.nowX + boss.width) && (nowY + height) > boss.nowY && (nowY + height) < (boss.nowY + boss.height))){
					boss.health -= damage;
					if(boss.health <= 0 && FinalPlaneActivity.soundFlag){
						FinalPlaneActivity.bombMusic.start();
					}
					state = 0;
					Log.i("wy", "����boss!Ѫ��:"+boss.health);
				}
			}
		}
	}
	
	/**
	 * �����ӵ�
	 * @param planeX �ɻ���ǰ��λ�õ�X����
	 * @param planeY �ɻ���ǰ��λ�õ�Y����
	 * @param style �ӵ����е�ģʽ
	 */
	public void reset(int planeX,int planeY,int style){
		state = 1;
		if(belongTo){
			nowX = planeX - width/2;
			nowY = planeY + height;
		}
		else{
			nowX = planeX - width/2;
			nowY = planeY + 40;
		}
		this.style = style;
	}
}
