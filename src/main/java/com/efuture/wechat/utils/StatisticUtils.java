package com.efuture.wechat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title: StatisticUtils
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/16
 */

public class StatisticUtils {
    private static final Logger logger = LoggerFactory.getLogger(StatisticUtils.class);
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private static final Map<String, AtomicInteger> methodCountMap = new ConcurrentHashMap<>(); //
    private static final AtomicInteger threadCount = new AtomicInteger(0); //线程安全的计数变量

    /**
     * 指定启动通知
     * @param methodName  方法名称
     * @param count       方法并发
     * @param total       全局并发
     */
    private static void onIncrementNotify(String methodName,int count,int total) {
        String tag = methodName+"";
        logger.info("run parallel before:--->当前时间:{} 执行方法:{} 并发数:{} 全局并发:{}",formatter.format(new Date()), tag,count,total);
    }

    /**
     * 执行处理通知
     * @param methodName  方法名称
     * @param count       方法并发
     * @param total       全局并发
     * @param elapsed     耗时时间
     */
    private static void onDecrementNotify(String methodName,int count,int total,Long elapsed) {
        String tag = methodName+"";
        if (elapsed > 300) {
            logger.warn("run parallel after:--->当前时间:{} 执行方法:{} 并发数:{} 全局并发:{} 耗时时间:{}",formatter.format(new Date()), tag,count,total,elapsed);
        } else {
            logger.info("run parallel after:--->当前时间:{} 执行方法:{} 并发数:{} 全局并发:{} 耗时时间:{}",formatter.format(new Date()), tag,count,total,elapsed);
        }
    }

    /**
     * 执行Map自增计数
     * @param methodName 方法名字
     * @param total      全局计数
     */
    private synchronized static void doIncrementMethod(String methodName,int total) {
        if (methodCountMap.containsKey(methodName)) {
            AtomicInteger aCounter = methodCountMap.get(methodName);
            int count = aCounter.incrementAndGet();

            // 执行统计信息
            onIncrementNotify(methodName,count,total);
        } else {
            methodCountMap.put(methodName, new AtomicInteger(1));

            onIncrementNotify(methodName,0,total);
        }
    }

    /**
     * 执行方法统计【包括全局和方法的】
     * @param methodName
     */
    public static void onIncrementMethod(String methodName) {
        //自增1,返回更新值
        int total = threadCount.incrementAndGet();
        if (!StringUtils.isEmpty(methodName)) {
            // 先判断避免线程阻塞
            if (methodCountMap.containsKey(methodName)) {
                AtomicInteger aCounter = methodCountMap.get(methodName);
                int count = aCounter.incrementAndGet();

                // 执行统计信息
                onIncrementNotify(methodName,count,total);
            } else {
                doIncrementMethod(methodName,total);
            }
        }
    }

    /**
     * 执行Map自减计数
     * @param methodName 方法名字
     * @param total      全局计数
     */
    private synchronized static void doDecrementMethod(String methodName,int total,Long elapsed) {
        if (methodCountMap.containsKey(methodName)) {
            AtomicInteger aCounter = methodCountMap.get(methodName);
            int mCount = aCounter.incrementAndGet();

            // 执行统计信息
            onDecrementNotify(methodName,mCount,total,elapsed);
        } else {
            methodCountMap.put(methodName, new AtomicInteger(0));
            // 初始化并通知
            onDecrementNotify(methodName,0,total,elapsed);
        }
    }

    /**
     * 统计计数结束
     * @param methodName 方法名字
     * @param elapsed    耗时时间
     */
    public static void onDecrementMethod(String methodName,Long elapsed) {
        //自减1,返回更新值
        int total = threadCount.decrementAndGet();
        if (!StringUtils.isEmpty(methodName)) {
            // 先判断避免线程阻塞
            if (methodCountMap.containsKey(methodName)) {
                AtomicInteger aCounter = methodCountMap.get(methodName);
                int count = aCounter.decrementAndGet();

                // 通知统计信息
                onDecrementNotify(methodName,count,total,elapsed);
            } else {
                // 执行初始化并通知
                doDecrementMethod(methodName,total,elapsed);
            }
        }
    }
}