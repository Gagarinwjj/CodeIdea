/**
 * Project Name:  Test
 * File Name:     TextExecutors.java
 * Package Name:  wjj
 * @Date:         2014年11月15日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

package wjj;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TextExecutors
 * @Function: 线程池测试
 * @Date: 2014年11月15日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class TextExecutors {

	public static void main(String[] args) {
		// cacheThread();
		// fixedThread();
		// scheduledThread();
		singleThread();

	}

	public static void cacheThread() {

		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {// 给定10个任务
			int index = i;

			// try {
			// Thread.sleep( 1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					System.out.println(index);// 值传递过来，不是最后一个值
				}
			});
		}
	}

	public static void fixedThread() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 10; i++) {
			int index = i;
			fixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					try {
						System.out.println(index);
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void scheduledThread() {
		ScheduledExecutorService scheduledThreadPool = Executors
				.newScheduledThreadPool(5);
		scheduledThreadPool.schedule(new Runnable() {

			@Override
			public void run() {
				System.out.println("delay 3 seconds");
			}
		}, 3, TimeUnit.SECONDS);

		scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out
						.println("delay 1 seconds, and excute every 3 seconds");
			}
		}, 1, 3, TimeUnit.SECONDS);
	}

	public static void singleThread() {
		ExecutorService singleThreadExecutor = Executors
				.newSingleThreadExecutor();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			singleThreadExecutor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						System.out.println(index);
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

}
