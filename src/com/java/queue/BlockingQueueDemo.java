package com.java.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列 --> BlockingQueue是个接口，继承Queue，而Queue又继承Collection
 * 当阻塞队列是空时，从队列中获取元素的操作将会被阻塞；当阻塞队列是满时，往队列里添加元素的操作将会被阻塞。
 * 在多线程领域：所谓阻塞，在某些情况下会挂起线程(即阻塞)，一旦添加满足，被挂起的线程又会自动被唤醒。
 * =====================================================================================================================
 * BlockingQueue接口的实现
 * ArrayBlockingQueue：一个基于数组结构的有界阻塞队列，此队列按FIFO(先进先出)原则对元素进行排序
 * LinkedBlockingQueue：一个基于链表结构的有界(但大小默认值为Integer.MAX_VALUE，注意：Integer.MAX_VALUE的值超过21亿)阻塞队列，吞吐量通常高于ArrayBlockQueue
 * PriorityBlockingQueue：支持优先级排序的无界阻塞队列
 * DelayQueue：使用优先级队列实现的延迟无界阻塞队列
 * SynchronousQueue：不存储元素的阻塞队列，也即单个元素的队列
 * LinkedTransferQueue：由链表结构组成的无界阻塞队列
 * LinkedBlockingDeque：由链表结构组成的双向阻塞队列
 * =====================================================================================================================
 * 阻塞队列有没有好的一面？
 * 好处是我们不需要关系什么时候需要阻塞线程，什么时候需要唤醒线程，因为这一切BlockingQueue都给你一手包办了。
 * 在concurrent包发布以前，在多线程环境下，我们每个程序员都必须去自己控制这些细节，尤其还要兼顾效率和线程安全，而这会给我们的程序带来不小的复杂度。
 * =====================================================================================================================
 * 阻塞队列用在哪？
 * 生产者消费者模式：传统版、阻塞队列版
 * 线程池
 * 消息中间件
 * =====================================================================================================================
 * 不得不阻塞，你如何管理？
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("===================异常组=====================");
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        // 当阻塞队列满时，再往队列里add插入元素会抛 java.lang.IllegalStateException: Queue full
//        System.out.println(blockingQueue.add("x"));

        System.out.println(blockingQueue.element()); // 检查第一个元素

        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        // 当阻塞队列空时，再往队列里remove移除元素会抛 java.util.NoSuchElementException
//        System.out.println(blockingQueue.remove());

        System.out.println("===================布尔值组=====================");
        blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        // 插入方法，成功true失败false
        System.out.println(blockingQueue.offer("x"));

        System.out.println(blockingQueue.peek()); // 检查

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        // 移除方法，成功返回队列的元素，队列里面没有就返回null
        System.out.println(blockingQueue.poll());

        System.out.println("===================阻塞组=====================");
        blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");
        // 当阻塞队列满时，生产者线程继续往队列里put元素，队列会一直阻塞生产线程直到put数据or响应中断退出
//        blockingQueue.put("x");

        blockingQueue.take();
        blockingQueue.take();
        blockingQueue.take();
        // 当阻塞队列空时，消费者线程试图从队列里take元素，队列会一直阻塞消费者线程直到队列可用
//        blockingQueue.take();

        System.out.println("===================超时组=====================");
        blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("b", 2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("c", 2L, TimeUnit.SECONDS));
        // 当阻塞队列满时，队列会阻塞生产者线程一定时间，超过限时后生产者线程会退出
        System.out.println(blockingQueue.offer("x", 2L, TimeUnit.SECONDS));

        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
        // 当阻塞队列空时，队列会阻塞消费者线程一定时间，超过限时后消费者线程会退出
        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
    }
}
