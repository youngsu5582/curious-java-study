package joyson.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class TimeUtil {

    public interface TimedTask {
        void execute() throws Exception;
    }

    public static void measureExecutionTime(final String taskName, final TimedTask task) {
        System.out.println(taskName + "Start (" + System.currentTimeMillis() + ")");
        final long startTime = System.currentTimeMillis();
        try {
            task.execute();
        } catch (final Exception e) {
            System.err.println("Error during task execution: " + e.getMessage());
        }
        final long endTime = System.currentTimeMillis();
        System.out.println(taskName + " Time Taken: " + (endTime - startTime) + "ms");
    }

    private static final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static final boolean cpuTimeSupported = threadMXBean.isThreadCpuTimeSupported();

    // CPU 시간 계산 함수
    private static long calculateCpuTime(final long startCpuTime, final long endCpuTime) {
        return (startCpuTime != -1 && endCpuTime != -1) ? (endCpuTime - startCpuTime) / 1_000_000 : -1; // 나노초 → 밀리초
    }

    public static TaskLog measureTaskLog(final String taskName, final int taskId, final TimedTask task) {
        final long startTime = System.currentTimeMillis();
        final long startCpuTime = cpuTimeSupported ? threadMXBean.getCurrentThreadCpuTime() : -1; // 시작 CPU 시간 측정
        final Thread currentThread = Thread.currentThread();

//        System.out.println(taskName + " started on " + currentThread.getName() + " at " + startTime);

        try {
            task.execute();
        } catch (final Exception e) {
            System.err.println("Error during " + taskName + " execution: " + e.getMessage());
        }

        final long endTime = System.currentTimeMillis();
        final long endCpuTime = cpuTimeSupported ? threadMXBean.getCurrentThreadCpuTime() : -1; // 종료 CPU 시간 측정
        final long executionTime = endTime - startTime;
        final long cpuTime = calculateCpuTime(startCpuTime, endCpuTime);

//        System.out.println(taskName + " completed in " + executionTime + "ms (CPU Time: " + cpuTime + "ms)");

        // TaskLog 반환
        return new TaskLog(taskId, currentThread.getName(), taskName, startTime, endTime, executionTime, cpuTime);
    }
}
