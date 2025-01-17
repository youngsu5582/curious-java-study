package joyson.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class SeleniumPerformance {
    public static void main(final String[] args) {
        // ChromeDriver 경로 설정 (본인의 환경에 맞게 수정 필요)
        final long setupStartTime = System.currentTimeMillis(); // 시작 시간 기록

        WebDriverManager.chromedriver().setup();
        System.out.println("Selenium Time Taken: " + (System.currentTimeMillis() - setupStartTime) + "ms");
        final ChromeDriver driver = new ChromeDriver();
        String chromeDriverProcessName = driver.getCapabilities().getCapability("goog:chromeOptions").toString();
        try {
            final long startTime = System.currentTimeMillis(); // 시작 시간 기록

            getSystemResourceUsage();
            // 페이지 로
            getSystemResourceUsage();

            // 대기 시간 설정 (JavaScript가 콘텐츠를 로드할 시간을 줌)
//            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // 동적 콘텐츠 가져오기

            final long endTime = System.currentTimeMillis(); // 종료 시간 기록

            System.out.println("Selenium Content: ");
            final WebElement bodyElement = driver.findElement(By.tagName("body"));
            bodyElement.getText();
            System.out.println("Selenium Time Taken: " + (endTime - startTime) + "ms");
        } finally {
            driver.quit();
        }
    }
    public static void getSystemResourceUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("CPU Load: " + osBean.getSystemLoadAverage());
        try {
            Method memoryUsageMethod = osBean.getClass().getMethod("getFreePhysicalMemorySize");
            Object memoryUsage = memoryUsageMethod.invoke(osBean);
            System.out.println("Free Memory: " + memoryUsage + " bytes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//131.0.6778.265
//131.0.6778.264
