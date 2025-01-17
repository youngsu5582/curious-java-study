package joyson.threadpool;

import joyson.util.CpuUtil;
import joyson.util.TaskLog;
import joyson.util.TimeUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static joyson.threadpool.ThreadPoolDiffInCpuIntensive.performCPU;
import static joyson.threadpool.ThreadPoolDiffInIOIntensive.performIO;
import static joyson.threadpool.ThreadPoolDiffInNetworkIOIntensive.performNetworkIO;
import static joyson.util.TimeUtil.measureTaskLog;

public class ThreadPoolComparison {

    public static void main(final String[] args) {
        final int taskCount = 500; // 작업 개수
        final int threadPoolSize = 100; // 스레드 풀 크기

        // 실행 시간 데이터를 저장할 리스트
        final List<TaskLog> singleThreadPoolLogs = new ArrayList<>();
        final List<TaskLog> multipleThreadPoolLogs = new ArrayList<>();

        System.out.println("=== Running with Single Thread Pool ===");
        TimeUtil.measureExecutionTime("Single Thread Pool",
                () -> runWithSingleThreadPool(threadPoolSize * 3, taskCount, singleThreadPoolLogs));

        System.out.println("\n=== Running with Multiple Thread Pools ===");
        TimeUtil.measureExecutionTime("Multiple Thread Pools",
                () -> runWithMultipleThreadPools(threadPoolSize, taskCount, multipleThreadPoolLogs));

        // 데이터를 파일로 저장 (시작 시간 기준 정렬)
        singleThreadPoolLogs.sort(Comparator.comparingLong(TaskLog::getStartTime));
        multipleThreadPoolLogs.sort(Comparator.comparingLong(TaskLog::getStartTime));
        CpuUtil.analyzeCpuAllocation(multipleThreadPoolLogs);
        saveDataToFile("multiple_thread_pool_logs.csv", multipleThreadPoolLogs);

        CpuUtil.analyzeCpuAllocation(singleThreadPoolLogs);
        saveDataToFile("single_thread_pool_logs.csv", singleThreadPoolLogs);
    }

    // 1. 하나의 스레드 풀에서 모든 작업 실행
// 주요 변경 포함
    private static void runWithSingleThreadPool(final int threadPoolSize, final int taskCount, final List<TaskLog> logs) throws ExecutionException, InterruptedException {
        final ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolSize);
        final List<Future<TaskLog>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;

            if (taskId % 3 == 0) {
                futures.add(threadPool.submit(() ->
                        measureTaskLog("FileIO Task", taskId, () -> performIO(taskId))
                ));
            } else if (taskId % 3 == 1) {
                futures.add(threadPool.submit(() ->
                        measureTaskLog("NetworkIO Task", taskId, () -> performNetworkIO(taskId))
                ));
            } else {
                futures.add(threadPool.submit(() ->
                        measureTaskLog("CPU Task", taskId, () -> performCPU(taskId))
                ));
            }
        }

        for (final Future<TaskLog> future : futures) {
            logs.add(future.get());
        }

        threadPool.shutdown();
    }

    // 2. 각 작업별로 별도의 스레드 풀 사용
    private static void runWithMultipleThreadPools(final int threadPoolSize, final int taskCount, final List<TaskLog> logs)
            throws InterruptedException, ExecutionException {
        final ExecutorService fileIOPool = Executors.newFixedThreadPool(threadPoolSize);
        final ExecutorService networkIOPool = Executors.newFixedThreadPool(threadPoolSize);
        final ExecutorService cpuPool = Executors.newFixedThreadPool(threadPoolSize);

        final List<Future<TaskLog>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;

            if (taskId % 3 == 0) { // FileIO
                futures.add(fileIOPool.submit(() -> measureTaskLog("FileIO Task", taskId, () -> performIO(taskId))));
            } else if (taskId % 3 == 1) { // NetworkIO
                futures.add(networkIOPool.submit(() -> measureTaskLog("NetworkIO Task", taskId, () -> performNetworkIO(taskId))));
            } else { // CPU
                futures.add(cpuPool.submit(() -> measureTaskLog("CPU Task", taskId, () -> performCPU(taskId))));
            }
        }

        for (final Future<TaskLog> future : futures) {
            logs.add(future.get());
        }

        fileIOPool.shutdown();
        networkIOPool.shutdown();
        cpuPool.shutdown();
    }

    // CSV 파일로 데이터 저장
    private static void saveDataToFile(final String filename, final List<TaskLog> logs) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("TaskID,Thread,Type,StartTime,EndTime,ExecutionTime,CPUTime(ms)\n"); // CSV 헤더
            for (final TaskLog log : logs) {
                writer.write(log.toCsv() + "\n");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
