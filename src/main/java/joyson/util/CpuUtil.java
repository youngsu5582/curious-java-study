package joyson.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CpuUtil {

    public static void analyzeCpuAllocation(final List<TaskLog> taskLogs) {
        // 1. Task 유형별 평균 CPUTime
        final Map<String, Double> avgCpuTimeByType = taskLogs.stream()
                .collect(Collectors.groupingBy(
                        TaskLog::getType,
                        Collectors.averagingLong(TaskLog::getCpuTime)
                ));

        System.out.println("=== Average CPU Time by Task Type ===");
        avgCpuTimeByType.forEach((type, avgCpuTime) ->
                System.out.printf("Task Type: %s, Average CPU Time: %.2f ms\n", type, avgCpuTime)
        );

        // 2. Task별 CPUTime 분포
        System.out.println("\n=== Task-wise CPU Time Distribution ===");
        taskLogs.stream()
                .sorted(Comparator.comparingLong(TaskLog::getCpuTime)
                        .reversed())
                .forEach(log -> System.out.printf("TaskID: %d, Type: %s, CPU Time: %d ms\n",
                        log.getTaskId(), log.getType(), log.getCpuTime()));

        // 3. Thread별 CPUTime 분포
        final Map<String, Long> cpuTimeByThread = taskLogs.stream()
                .collect(Collectors.groupingBy(
                        TaskLog::getThreadName,
                        Collectors.summingLong(TaskLog::getCpuTime)
                ));

        System.out.println("\n=== Total CPU Time by Thread ===");
        cpuTimeByThread.forEach((thread, totalCpuTime) ->
                System.out.printf("Thread: %s, Total CPU Time: %d ms\n", thread, totalCpuTime)
        );
    }
}
