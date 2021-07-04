package com.java.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 不存储元素的阻塞队列，也即单个元素的队列
 * 每一个put操作必须要等待一个take操作，否则不能继续添加元素，反之亦然
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        BlockingQueue blockingQueue = new SynchronousQueue();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "\t put 1");
                blockingQueue.put("1");

                System.out.println(Thread.currentThread().getName() + "\t put 2");
                blockingQueue.put("2");

                System.out.println(Thread.currentThread().getName() + "\t put 3");
                blockingQueue.put("3");
            } catch (Exception e) {
                e.getStackTrace();
            }
        }, "AAA").start();

        new Thread(() -> {
            try {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + blockingQueue.take());

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + blockingQueue.take());

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + blockingQueue.take());
            } catch (Exception e) {
                e.getStackTrace();
            }
        }, "BBB").start();
    }
}
