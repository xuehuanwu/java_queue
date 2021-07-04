package com.java.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者消费者模式：阻塞队列版
 * volatile/CAS/atomicInteger/blockQueue/线程交互/原子引用
 * =====================================================================================================================
 * 线程交互版本
 * V1.0     sync            wait                notify
 * V2.0     lock            await               signal
 * V3.0     标志位(true)     prod->consumer     标志位(false)
 * =====================================================================================================================
 * 有可能消费者会打印到生产者的前面，因为线程读值和取值的不确定性
 */
public class ProdConsumer_BlockQueueDemo {

    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
            try {
                myResource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Prod").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
            System.out.println();
            try {
                myResource.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Consumer").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();

        System.out.println("5秒时间到，大老板main线程叫停，活动结束");
        myResource.stop();
    }
}

class MyResource {

    // volatile保证可见性
    private volatile boolean flag = true; // 默认开启，进行生产+消费
    // AtomicInteger保证原子性
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    // 传接口不传类
    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd() throws Exception {
        String str = null;
        boolean value; // 默认false
        while (flag) {
            str = atomicInteger.incrementAndGet() + ""; // 原子版+1
            value = blockingQueue.offer(str, 2L, TimeUnit.SECONDS); // 2秒生产一个
            if (value) {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + str + "成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + str + "失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t 大老板叫停了，表示flag=false，生产动作结束");
    }

    public void myConsumer() throws Exception {
        String str = null;
        while (flag) {
            str = blockingQueue.poll(2L, TimeUnit.SECONDS); // 2秒消费一个
            if (null == str || str.equalsIgnoreCase("")) {
                flag = false;
                System.out.println(Thread.currentThread().getName() + "\t 超过2秒没有取到蛋糕，消费退出");
                return; // 退出while循环
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费队列" + str + "成功");
        }
    }

    public void stop() {
        this.flag = false;
    }
}