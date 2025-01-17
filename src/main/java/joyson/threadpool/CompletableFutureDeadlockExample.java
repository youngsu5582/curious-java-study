package joyson.threadpool;

import java.util.concurrent.*;
public class CompletableFutureDeadlockExample {

    public static void main(String[] args) {
        // 고정된 크기의 스레드 풀 (9개의 스레드로 제한)
        ExecutorService executor = Executors.newFixedThreadPool(9);

        try {
            // 9개의 부모 작업 생성
            CompletableFuture<?>[] parentFutures = new CompletableFuture[9];

            for (int i = 0; i < 9; i++) {
                final int parentId = i;
                parentFutures[i] = CompletableFuture.runAsync(() -> {
                    System.out.println(Thread.currentThread().getName() + " - Parent Task " + parentId + " started");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // 부모 작업이 자식 작업 생성
                    CompletableFuture<Void> childFuture = CompletableFuture.runAsync(() -> {
                        System.out.println(Thread.currentThread().getName() + " - Child Task of Parent " + parentId + " started");
                        try {
                            Thread.sleep(1000); // 자식 작업 시뮬레이션
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println(Thread.currentThread().getName() + " - Child Task of Parent " + parentId + " completed");
                    }, executor);

                    // 부모 작업이 자식 작업 완료를 기다림
                    childFuture.join();
                    System.out.println(Thread.currentThread().getName() + " - Parent Task " + parentId + " completed");
                }, executor);
            }

            // 모든 부모 작업 완료를 기다림
            CompletableFuture.allOf(parentFutures).join();

        } finally {
            executor.shutdown();
        }
    }
}
