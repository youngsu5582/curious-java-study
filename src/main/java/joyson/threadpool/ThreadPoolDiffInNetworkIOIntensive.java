package joyson.threadpool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolDiffInNetworkIOIntensive {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread.sleep(5000); // 시작 전 대기 (테스트 환경 안정화)
        int taskCount = 100000; // 작업 개수
        runWithThreadPool(100, taskCount);
//        runWithoutThreadPool(taskCount);
    }

    // 스레드 풀 사용
    private static void runWithThreadPool(int poolSize, int taskCount) throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);
        List<Future<?>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            int taskId = i;
            futures.add(threadPool.submit(() -> performNetworkIO(taskId)));
        }

        for (Future<?> future : futures) {
            future.get(); // 모든 작업 완료 대기
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
            int taskId = i;
            Thread thread = new Thread(() -> {
                performNetworkIO(taskId);
                latch.countDown(); // 작업 완료 시 감소
            });
            threads.add(thread);
            thread.start();
        }

        latch.await(); // 모든 작업이 완료될 때까지 대기

        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time (Without ThreadPool): " + (endTime - startTime) + " ms");
    }

    // 네트워크 I/O 작업 (HTTP 요청)
    public static void performNetworkIO(int taskId) {
        final String urlStr = "https://jsonplaceholder.typicode.com/posts/1";
//        final String urlStr = "https://httpbin.org/delay/2";
//        System.out.println(Thread.currentThread()
//                .getName() + " running start : taskId : " + taskId + "time : " + System.currentTimeMillis());
        try {
            // HTTP 연결 생성
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 연결 시간 초과 설정 (5초)
            connection.setReadTimeout(5000); // 읽기 시간 초과 설정 (5초)

            // 응답 읽기
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // HTTP OK
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
//                    System.out.println("Task " + taskId + ": Response received (length: " + content.length() + ")");
                }
            } else {
//                System.err.println("Task " + taskId + ": Failed with HTTP code " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Task " + taskId + ": Error during network I/O");
            e.printStackTrace();
        }
    }
}
