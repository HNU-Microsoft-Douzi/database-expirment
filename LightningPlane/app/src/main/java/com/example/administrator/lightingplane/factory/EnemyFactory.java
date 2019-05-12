package com.example.administrator.lightingplane.factory;

import android.content.Context;

import com.example.administrator.lightingplane.Plane;
import com.example.administrator.lightingplane.R;
import com.example.administrator.lightingplane.event.OnAwardCreateEvent;
import com.example.administrator.lightingplane.util.ExecutorManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

/**
 * 创建人: zhang376358913
 * 创建时间: 2019/5/12 15:17
 * 类描述:
 * 修改人: zhang376358913
 * 修改时间: zhang376358913
 * 修改备注: 敌机的数据常量类
 */
public class EnemyFactory {

    private Context mContext;

    Random random = new Random();

    public EnemyFactory(Context context) {
        this.mContext = context;
    }

    private void createSecondEnemy(Plane enemy) {
        enemy.moveStyle = Math.abs(random.nextInt() % 2);
        enemy.health = 40;
        enemy.planeStyleIndex = 1;
        enemy.changeBulletDamage(20);
        enemy.changeBulletPic(R.drawable.enemy_bullet_1);
    }

    private void createThirdEnemy(Plane enemy) {
        enemy.moveStyle = Math.abs(random.nextInt() % 2);
        enemy.shotStyle = 1;
        enemy.health = 200;
        enemy.planeStyleIndex = 2;
        enemy.changeBulletDamage(50);
        enemy.changeBulletPic(R.drawable.enemy_bullet_2);
    }

    private void createForthEnemy(Plane enemy) {
        enemy.moveStyle = Math.abs(random.nextInt() % 2);
        enemy.shotStyle = 1;
        enemy.health = 1000;
        enemy.planeStyleIndex = 3;
        enemy.changeBulletDamage(100);
        enemy.changeBulletPic(R.drawable.enemy_bullet_3);
    }

    private void createFifthEnemy(Plane enemy) {
        enemy.moveStyle = Math.abs(random.nextInt() % 2);
        enemy.shotStyle = 1;
        enemy.health = 5000;
        enemy.planeStyleIndex = 4;
        enemy.changeBulletDamage(200);
        enemy.changeBulletPic(R.drawable.enemy_bullet_4);
    }

    public void createFirstEnemies(List<Plane> enemies) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (EnemyFactory.class) {
                for (Plane enemy : enemies) {
                    enemy.planeStyleIndex = 0;
                    enemy.changeBulletPic(R.drawable.enemy_bullet_0);
                    if (random.nextInt(5) == 1) {
                        createSecondEnemy(enemy);
                        if (random.nextBoolean()) EventBus.getDefault().post(new OnAwardCreateEvent());
                    }
                }
            }
        });
    }

    public void createSecondEnemies(List<Plane> enemies) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (EnemyFactory.class) {
                for (Plane enemy : enemies) {
                    createSecondEnemy(enemy);
                    if (random.nextInt(5) == 1) {
                        createThirdEnemy(enemy);
                        if (random.nextBoolean()) EventBus.getDefault().post(new OnAwardCreateEvent());
                    }
                }
            }
        });
    }

    public void createThirdEnemies(List<Plane> enemies) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (EnemyFactory.class) {
                for (Plane enemy : enemies) {
                    createThirdEnemy(enemy);
                    if (random.nextInt(5) == 1) {
                        createForthEnemy(enemy);
                        if (random.nextBoolean()) EventBus.getDefault().post(new OnAwardCreateEvent());
                    }
                }
            }
        });

    }

    public void createForthEnemies(List<Plane> enemies) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (EnemyFactory.class) {
                for (Plane enemy : enemies) {
                    createForthEnemy(enemy);
                    if (random.nextInt(5) == 1) {
                        createFifthEnemy(enemy);
                        if (random.nextBoolean()) EventBus.getDefault().post(new OnAwardCreateEvent());
                    }
                }
            }
        });
    }

    public void createFifthEnemies(List<Plane> enemies) {
        ExecutorManager.getInstance().execute(() -> {
            synchronized (EnemyFactory.class) {
                for (Plane enemy : enemies) {
                    createFifthEnemy(enemy);
                    if (random.nextInt(10) == 5) {
                        EventBus.getDefault().post(new OnAwardCreateEvent());
                    }
                }
            }
        });
    }
}
