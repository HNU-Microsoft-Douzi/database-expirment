package com.example.administrator.lightingplane.factory;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.administrator.lightingplane.Boss;
import com.example.administrator.lightingplane.R;
import com.example.administrator.lightingplane.util.ExecutorManager;

/**
 * 创建人: zhang376358913
 * 创建时间: 2019/5/12 15:16
 * 类描述:
 * 修改人: zhang376358913
 * 修改时间: zhang376358913
 * 修改备注: 不同关卡的Boss数据的常量类
 */
public class BossFactory {

    private Context mContext;

    public BossFactory(Context context) {
        this.mContext = context;
    }

    public void createFirstBoss(Boss boss) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (BossFactory.class) {
                boss.shotStyle = 2;
                boss.planePics[0] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.boss1);
                boss.width = boss.planePics[0].getWidth();
                boss.height = boss.planePics[0].getHeight();
                boss.moveStyle = 0; //左右水平移动
                boss.shotStyle = 1;//发射一枚子弹
                boss.changeBulletPic(R.drawable.boss_bullet0);
                boss.health = 1000;
            }
        });
    }

    public void createSecondBoss(Boss boss) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (BossFactory.class) {
                boss.moveStyle = 1;
                boss.shotStyle = 2;
                boss.planePics[0] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.boss2);
                boss.width = boss.planePics[0].getWidth();
                boss.height = boss.planePics[0].getHeight();
                boss.changeBulletPic(R.drawable.boss_bullet1);
                boss.health = 5000;
                boss.changeBulletDamage(30);
            }
        });
    }

    public void createThirdBoss(Boss boss) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (BossFactory.class) {
                boss.moveStyle = 1;
                boss.shotStyle = 3;
                boss.planePics[0] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.boss3);
                boss.width = boss.planePics[0].getWidth();
                boss.height = boss.planePics[0].getHeight();
                boss.changeBulletPic(R.drawable.boss_bullet2);
                boss.health = 20000;
                boss.changeBulletDamage(80);
            }
        });
    }

    public void createForthBoss(Boss boss) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (BossFactory.class) {
                boss.moveStyle = 1;
                boss.shotStyle = 4;
                boss.planePics[0] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.boss4);
                boss.width = boss.planePics[0].getWidth();
                boss.health = boss.planePics[0].getHeight();
                boss.changeBulletPic(R.drawable.boss_bullet3);
                boss.health = 50000;
                boss.changeBulletDamage(150);
            }
        });
    }

    public void createFifthBoss(Boss boss) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (BossFactory.class) {
                boss.moveStyle = 1;
                boss.shotStyle = 5;
                boss.planePics[0] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.boss5);
                boss.width = boss.planePics[0].getWidth();
                boss.height = boss.planePics[0].getHeight();
                boss.changeBulletPic(R.drawable.boss_bullet4);
                boss.health = 100000;
                boss.changeBulletDamage(300);
            }
        });
    }
}
