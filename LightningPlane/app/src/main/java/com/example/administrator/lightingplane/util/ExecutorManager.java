package com.example.administrator.lightingplane.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 创建人: 张晓翼
 * 创建时间: 2019/3/11 22:40
 * 类描述: 构建线程池
 * 修改人: 张晓翼
 * 修改时间: 张晓翼
 * 修改备注:
 */
public class ExecutorManager {
    // 核心线程数目
    private final static int CORE_POOL_SIZE = 20;

    // 最大线程数
    private final static int MAXIMUM_POOL_SIZE = 30;

    // 闲置线程存活时间
    private final static long KEEP_ALIVE_TIME = 60;

    // 时间单位(秒)
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    // 线程队列
    private final static BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();

    public static ExecutorService getInstance() {
        return ExecutorManagerHolder.mExecutor;
    }

    private final static class ExecutorManagerHolder{
        private static ExecutorService mExecutor = Executors.newCachedThreadPool();
    }
}
