package joyson.threadpool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolDiffInIOIntensive {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread.sleep(10000);
        int taskCount = 2000;
//        runWithThreadPool(8, taskCount);
//        runWithThreadPool(100, taskCount);
        runWithoutThreadPool(taskCount);
    }

    // 스레드 풀 사용
    private static void runWithThreadPool(int poolSize, int taskCount) throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);
        List<Future<?>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            int taskId = i;
            futures.add(threadPool.submit(() -> performIO(taskId)));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time (ThreadPool): " + (endTime - startTime) + " ms");

        threadPool.shutdown();
        cleanOutputDirectory();
    }

    // 스레드 직접 생성
    private static void runWithoutThreadPool(int taskCount) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(taskCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            int taskId = i;
            Thread thread = new Thread(() -> {
                performIO(taskId);
                latch.countDown();
            });
            threads.add(thread);
            thread.start();
        }

        latch.await(); // 모든 스레드가 작업 완료할 때까지 대기

        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time (Without ThreadPool): " + (endTime - startTime) + " ms");
        cleanOutputDirectory();
    }

    // I/O 작업 (파일 쓰기 시뮬레이션)
    public static void performIO(int taskId) {
        final String directoryName = "output";
        final String fileName = directoryName + "/task_" + taskId + ".txt";

        // 디렉토리 존재 여부 확인 및 생성
        createDirectory(directoryName);

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < 50000; i++) { // 파일에 10000줄 쓰기
                writer.write("Task " + taskId + " - Line " + i + "\n");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanOutputDirectory() {
        final File outputDir = new File("output");
        if (outputDir.exists() && outputDir.isDirectory()) {
            final File[] files = outputDir.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private static synchronized void createDirectory(final String directoryName) {
        final File directory = new File(directoryName);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory);
            } else {
                System.err.println("Failed to create directory: " + directory);
            }
        }
    }
}
