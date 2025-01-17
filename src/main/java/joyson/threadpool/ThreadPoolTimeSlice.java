package joyson.threadpool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTimeSlice {
    public static void main(String[] args) {
        // 결과를 저장할 파일
        String fileName = "./result.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            // 2개의 스레드로 구성된 스레드 풀 생성
            ExecutorService executor = Executors.newFixedThreadPool(2);

            // 5개의 작업 제출
            for (int i = 1; i <= 5; i++) {
                int taskId = i;
                executor.submit(() -> {
                    long startTime = System.currentTimeMillis();
                    String threadName = Thread.currentThread()
                            .getName();

                    // 5초간 작업 수행
                    while (System.currentTimeMillis() - startTime < 5000) {
                        System.out.println("Task " + taskId + " is running in thread " + threadName + " at " + System.currentTimeMillis());
                    }

                });
            }

            // 스레드 풀 종료
            executor.shutdown();
        } catch (IOException e) {
            // 예외 출력
            System.err.println("파일 쓰기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
