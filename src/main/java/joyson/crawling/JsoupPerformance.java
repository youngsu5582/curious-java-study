import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupPerformance {
    public static void main(String[] args) throws Exception {
        String url = "https://www.google.com/";

        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        // Jsoup으로 HTML 가져오기
        Document document = Jsoup.connect(url)
                .get();

        long endTime = System.currentTimeMillis(); // 종료 시간 기록

        System.out.println("Jsoup Content: ");
        System.out.println(document.body()
                .text());
        System.out.println("Jsoup Time Taken: " + (endTime - startTime) + "ms");
    }
}
