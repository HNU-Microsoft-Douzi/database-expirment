package com.example.administrator.lightingplane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class Fighting extends SurfaceView implements Callback, Runnable {
	//��Ļ�Ŀ�
	private int screenWidth;
	//��Ļ�ĸ�
	private int screenHeight;
	//����
	Canvas canvas = null;
	//����
	Paint paint = null;
	SurfaceHolder holder = null;
	//����ͼƬ
	Bitmap backGround[] = new Bitmap[2];
	//��ǰ����ͼƬ1�ĸ߶�
	int bg1 = 0;
	//��ǰ����ͼƬ2�ĸ߶�
	int bg2 = 0;
	//�߳����б�־λ
	public static boolean flag = false;
	//��ͣ��־λ
	public static boolean pause = false;
	Thread thread;

	public static Plane plane = null;	//���Ƿɻ�����
	int enemyCount = 20;//�л�����
	List<Plane> enemys = new ArrayList<Plane>();//�л���
	Boss boss;
	Award award;//����
		
	Context context;
	//������ɻ���Ҫ�ƶ�����λ�õ�X��Y����
	int moveToX = 0;
	int moveToY = 0;
	
	int enemyFlag = 0;//�л����ñ�־λ
	int enemyInterval = 20;//�л�����ʱ����
	boolean bombFlag = false;
	
	public static int score = 0;//�ܷ�
	public static int num;//����л�������
	public int round = 1;//�ؿ�
	
	Random random = new Random();
	public int bossFlag = 20;//����bossFlag���л���boss�Ż����
	
	Bitmap pauseIcon = null;
	Bitmap playIcon = null;
	
	
	Fighting(Context context,int screenWidth, int screenHeight) {
		super(context);
		this.context = context;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		holder = getHolder();
		paint = new Paint();
		//���ñ���ͼƬ�Ե�λ��
		bg1 = 0;
		//���ñ���ͼƬ2��λ��
		bg2 = -screenHeight;
		holder.addCallback(this);
		//��ʼ��ս������
		init();
		//��ȡ����
		setFocusable(true);
	}

	
	private void init(){
		getBackGround(context);
		//��ʼ���ɻ�ͼƬ
		Bitmap[] temp = new Bitmap[3];
		temp[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.my);
		temp[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.my_l);
		temp[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.my_2);
		temp[0] = Bitmap.createScaledBitmap(temp[0], temp[0].getWidth()/2, temp[0].getHeight()/2, true);
		temp[1] = Bitmap.createScaledBitmap(temp[1], temp[1].getWidth()/2, temp[1].getHeight()/2, true);
		temp[2] = Bitmap.createScaledBitmap(temp[2], temp[2].getWidth()/2, temp[2].getHeight()/2, true);
		
		//��ʼ��boss
		Bitmap[] temp1 = new Bitmap[3];
		temp1[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss1);
		temp1[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss2);
		temp1[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss3_t);
		boss = new Boss(context, screenWidth, screenHeight, temp1);
		
		//��ʼ���л�
		Bitmap[] temp2 = new Bitmap[3];
		temp2[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy13);
		temp2[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy13_t);
		temp2[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy14_t);
		for(int i = 0; i < enemyCount; i++){
			Enemy enemy = new Enemy(context, screenWidth, screenHeight, temp2);
			enemy.moveStyle = 1;
			enemys.add(enemy);
		}
		
		//��ʼ���ɻ�
		plane = new Plane(context, screenWidth, screenHeight, temp);
		plane.enemys = enemys;
		plane.boss = boss;
		plane.setTarget();//���ɻ�����Ŀ��
		plane.shotInterval = 3;//���÷ɻ�����ٶ�
		for(Plane enemy:enemys){
			enemy.enemys.add(plane);
			enemy.setTarget();
		}
		boss.enemys.add(plane);
		boss.setTarget();
		boss.shotStyle = 5;
		boss.moveStyle = 1;
		award = new Award(context, screenWidth, screenHeight);
		award.state = true;
		
		if(FinalPlaneActivity.backMusicFlag){
			FinalPlaneActivity.backMusic.start();
		}
		
		pauseIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
		pauseIcon = Bitmap.createScaledBitmap(pauseIcon, 30, 30, false);
		playIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.play);
		playIcon = Bitmap.createScaledBitmap(playIcon, 30, 30, false);
	}
	
	/**
	 * ��ȡ����ͼƬ
	 * @param context ��ǰ������
	 */
	private void getBackGround(Context context){
		backGround[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
		backGround[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.background3);
		backGround[0] = Bitmap.createScaledBitmap(backGround[0], screenWidth, screenHeight, false);
		backGround[1] = Bitmap.createScaledBitmap(backGround[1], screenWidth, screenHeight, false);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag){
			if(!pause){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				draw();
			}
		}
	}
	
	private void draw(){
		canvas = holder.lockCanvas();
		drawBackGround(canvas);
		
		//����ʮ��С�л��������boss
		if(num == bossFlag){
				boss.reset();//boss����
				boss.state = 2;//������ʱboss����ʾ
			
			
			switch(round){
			case 1:
				boss.moveStyle = 1;
				boss.shotStyle = 2;
				boss.planePics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss1);
				boss.width = boss.planePics[0].getWidth();
				boss.height = boss.planePics[0].getHeight();
				boss.moveStyle = 0;//����ˮƽ�ƶ�
				boss.shotStyle = 1;//����һö�ӵ�
				boss.health = 1000;
				break;
			case 2:
				boss.moveStyle = 1;
				boss.shotStyle = 2;
				boss.planePics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss2);
				boss.width = boss.planePics[0].getWidth();
				boss.height = boss.planePics[0].getHeight();
				//�ı�л��ķ���ģʽ
				for(Plane enemy:enemys){
					int enemyMoveStyle = Math.abs(random.nextInt()%2);
					enemy.moveStyle = enemyMoveStyle;
					enemy.health = 20;
				}
				boss.health = 3000;
				break;
			case 3:
				boss.moveStyle = 1;
				boss.shotStyle = 3;
				boss.planePics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss3_t);
				boss.width = boss.planePics[0].getWidth();
				boss.height = boss.planePics[0].getHeight();
				//�ı�л��ķ���ģʽ
				for(Plane enemy:enemys){
					enemy.shotStyle = 1;
					enemy.health = 30;
				}
				boss.health = 6000;
				break;
			case 4:
				boss.moveStyle = 1;
				boss.shotStyle = 4;
				boss.planePics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss4);
				boss.width = boss.planePics[0].getWidth();
				boss.health = boss.planePics[0].getHeight();
				//�ı�л��ķ���ģʽ
				for(Plane enemy:enemys){
					enemy.shotStyle = 1;
					enemy.health = 40;
				}
				boss.health = 10000;
				break;
			case 5:
				boss.moveStyle = 1;
				boss.shotStyle = 5;
				boss.planePics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss5);
				boss.width = boss.planePics[0].getWidth();
				boss.height = boss.planePics[0].getHeight();
				//�ı�л��ķ���ģʽ
				for(Plane enemy:enemys){
					enemy.shotStyle = 1;
					enemy.health = 50;
				}
				boss.health = 15000;
				break;
			default :round ++;
			}

			boss.maxHealth = boss.health;
		}else if(num > bossFlag){
			boss.state = 1;
			if(boss.state == 1)
				boss.move(canvas, paint, moveToX, moveToY);//��boss��boss����ʱ�Ὣ����л���������0
			if(boss.health <= 0){
				round++;
				bossFlag+=20;
				num = 0;
				boss.state = 2;
			}
		}
		
		//ս�������󣬳�ʼ������λ��,���ел���ը,�ӵ���ʧ
		if(plane.health <= 0){
			moveToX = screenWidth/2;
			moveToY = screenHeight * 2 / 3;
			for(Plane enemy:enemys){
				if(enemy.state == 1 && enemy.height >= 0){
					enemy.health = 0;
				}
				for(Bullet bullet:enemy.bullets){
					bullet.state = 2;
				}
				for(Bullet bullet:boss.bullets){
					bullet.state = 2;
				}
			}
		}
		
		drawEnemy(canvas);//���л�
		plane.move(canvas, paint, moveToX - plane.width/2, moveToY - plane.height/2);//��ս��
		award.move(canvas, paint);
		canvas.drawText("����:"+score, 10, 20, paint);
		canvas.drawText("�ؿ�"+round, screenWidth - 80, 20, paint);
		if(pause){
			canvas.drawBitmap(playIcon, screenWidth - 30, 0, paint);
		}
		else{
			canvas.drawBitmap(pauseIcon, screenWidth - 30, 0, paint);
		}
		//ը��
		if(bombFlag){
			for(Plane enemy:enemys){
				if(enemy.state == 1 && enemy.height >= 0){
					enemy.health = 0;
				}
				for(Bullet bullet:enemy.bullets){
					bullet.state = 2;
				}
				for(Bullet bullet:boss.bullets){
					bullet.state = 2;
				}
			}
			bombFlag = false;
			plane.bomb--;
		}
		holder.unlockCanvasAndPost(canvas);
	}
	
	
	private void drawEnemy(Canvas canvas){
		if(canvas != null){
			for(Plane enemy:enemys){
				if(enemy.state == 1)
					enemy.move(canvas, paint, moveToX, moveToY);
			}
			
			if(enemyFlag == 0){
				for(Plane enemy:enemys){
					if(enemy.state == 2){
						enemy.reset();
						break;
					}
				}
				enemyFlag++;
			}else if(enemyFlag < enemyInterval){
				enemyFlag++;
			}else{
				enemyFlag = 0;
			}
		}
		
	}
	/**
	 * ���±���
	 * @param canvas ����
	 */
	private void drawBackGround(Canvas canvas){
		if(canvas != null){
			if(bg1 >= screenHeight)
				bg1 = -screenHeight;
			if(bg2 >= screenHeight)
				bg2 = -screenHeight;
			canvas.drawBitmap(backGround[0], 0, bg1, paint);
			canvas.drawBitmap(backGround[1], 0, bg2, paint);
			bg1+=2;
			bg2+=2;
		}
		
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
				
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX() > 0 && event.getX() < 30 && event.getY() > screenHeight - 10 && event.getY() < screenHeight && plane.bomb > 0){
				bombFlag = true;
			}else{
				moveToX = (int) event.getX();
				moveToY = (int) event.getY();
			}
		}else{
			moveToX = (int) event.getX();
			moveToY = (int) event.getY();
			if(moveToX > screenWidth - 30 && moveToY < 30){
				if(pause){
					pause = false;
				}else{
					pause = true;
				}
			}
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag = true;
		pause = false;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag = false;
	}

}
