package joyson.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ThreadPoolDiffInCpuIntensive {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 테스트: 스레드 풀과 스레드 직접 생성의 성능 비교
//        runWithThreadPool(4, 20); // 스레드 풀 사용
        runWithThreadPool(8, 30); // 스레드 풀 사용
//        runWithThreadPool(12, 30); // 스레드 풀 사용
        System.out.println("Second");
//        runWithoutThreadPool(20); // 스레드 풀 없이 직접 생성
    }

    // 스레드 풀 사용
    private static void runWithThreadPool(int poolSize, int taskCount) throws InterruptedException, ExecutionException {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            final int finalI = i;
            futures.add(threadPool.submit(() -> performCPU(finalI)));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time (ThreadPool): " + (endTime - startTime) + " ms");

        threadPool.shutdown();
    }

    // 스레드 직접 생성
    private static void runWithoutThreadPool(int taskCount) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(taskCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                final int num = finalI;
                performCPU(finalI);
                latch.countDown();
            });
            threads.add(thread);
            thread.start();
        }

        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기

        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time (Without ThreadPool): " + (endTime - startTime) + " ms");
    }

    public static void performCPU(int taskId) {
        final int DATA_SIZE = 10_000; // 10KB 메모리
        final int ITERATIONS = 9_000_000; // 반복 횟수
        final Random RANDOM = new Random();

        int[] data = new int[DATA_SIZE]; // 10KB 배열 생성

        // 배열에 랜덤값 저장
        for (int i = 0; i < DATA_SIZE; i++) {
            data[i] = RANDOM.nextInt();
        }

        // 랜덤 메모리 접근 작업
        for (int i = 0; i < ITERATIONS; i++) {
            int index = RANDOM.nextInt(DATA_SIZE);
            data[index] = (int) (data[index] + Math.tan(data[index]));
        }
    }
}
