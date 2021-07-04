package com.java.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者模式：传统版
 * 题目：一个初始值为0的变量，两个线程对其交替操作，一个加1,一个减1，来5轮
 * 1    线程  操作  资源类
 * 2    判断  干活  通知
 * 3    防止虚假唤醒机制
 * =====================================================================================================================
 * 什么是虚假唤醒？
 * 多线程判断用while，不能用if(两个线程以上则无法控制唤醒某个特定线程)
 */
public class ProdConsumer_TraditionDemo {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }, "BB").start();
    }
}

class ShareData { // 资源类

    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try {
            // 1、判断
            while (number != 0) {
                // 等待，不能生产
                condition.await();
            }
            // 2、干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            // 3、通知唤醒
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try {
            // 1、判断
            while (number == 0) {
                // 等待，不能生产
                condition.await();
            }
            // 2、干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            // 3、通知唤醒
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}