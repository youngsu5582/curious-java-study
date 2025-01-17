package joyson.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskLog {
    private final int taskId;
    private final String threadName;
    private final String type;
    private final long startTime;
    private final long endTime;
    private final long executionTime;
    private final long cpuTime;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public TaskLog(final int taskId, final String threadName, final String type, final long startTime, final long endTime, final long executionTime, final long cpuTime) {
        this.taskId = taskId;
        this.threadName = threadName;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.executionTime = executionTime;
        this.cpuTime = cpuTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getType() {
        return type;
    }

    public String getThreadName() {
        return threadName;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    // 시간 포맷팅: 밀리초를 사람이 읽기 쉬운 형식으로 변환
    private static String formatTime(long millis) {
        long seconds = millis / 1000;
        long ms = millis % 1000;
        long ns = (millis * 1_000_000) % 1_000_000; // 나노초 단위 추정 (정확한 값은 JVM에서 제공되지 않음)
        return seconds + "s " + ms + "ms " + ns + "ns";
    }

    // 타임스탬프 포맷팅: UNIX 시간 → 읽기 쉬운 시간
    private static String formatTimestamp(long timestamp) {
        return dateFormatter.format(new Date(timestamp));
    }

    public String toCsv() {
        return taskId + "," + threadName + "," + type + ","
                + formatTimestamp(startTime) + "," + formatTimestamp(endTime) + ","
                + formatTime(executionTime) + "," + formatTime(cpuTime);
    }
}
