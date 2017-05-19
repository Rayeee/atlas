package com.eparty.ccp.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TaskManager {
    private TaskManager(){}
    //任务池
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 添加任务到任务队列
     * @author chuijian.kong
     * @date 2015年4月13日 下午5:24:53
     * @version 1.0
     */
    public static <T> FutureTask<T> addFutureTask(Callable<T> task) {
        //创建Future任务
        FutureTask<T> futureTask = new FutureTask<T>(task);
        //添加到任务队列
        executor.execute(futureTask);
        return futureTask;
    }

    /**
     * 添加任务到任务队列
     * @author chuijian.kong
     * @date 2015年4月13日 下午5:24:53
     * @version 1.0
     */
    public static void addTask(Runnable task) {
        //添加到任务队列
        executor.execute(task);
    }

}
