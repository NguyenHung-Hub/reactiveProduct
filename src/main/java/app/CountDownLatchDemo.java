package app;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(2);
		
		Runnable task1 = () ->{
			for (int i = 0; i < 10; i++) {
				System.out.print(i + " ");
			}
			System.out.println();
			latch.countDown();
		};
		
		Runnable task2 = () ->{
			int tong = 0;
			for (int i = 0; i < 10; i++) {
				tong += i;
			}
			
			System.out.println("Tong: " + tong);
			latch.countDown();
		};
		
		Runnable task3 = () -> {
			System.out.println("Task 3");
		};
		
		Thread thread1 = new Thread(task1);
		Thread thread2 = new Thread(task2);
		Thread thread3 = new Thread(task3);
		
		thread1.start();
		thread2.start();
		
		latch.await();
		
		thread3.start();
		
	}
}
