package joyson.crawling;

import joyson.util.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Optional;


public class JsoupDeep {
    public static void main(final String[] args) {

        TimeUtil.measureExecutionTime("jsoup deep", () -> {
            final var baseUrl = "https://riverstone.co.kr";
            final var document = getDocument("https://riverstone.co.kr/category/women/25/?page=3");

            final var documents = parseDocument(document, "ul.prdList a")
                    .stream()
                    .map(element -> element.attr("href"))
                    .map(url -> getDocument(url, baseUrl))
                    .toList();

            final var result1 = findFirst(documents);
//            final var result2 = findSecond(documents);
        });
    }

    private static List<String> findFirst(final List<Document> documents) {
        return documents.stream()
                .map(document -> document.select(".xans-product > table > tbody"))
                .map(element -> Optional.ofNullable(element)
                        .map(Elements::text)
                        .orElse(""))
                .toList();
    }

    private static List<String> findSecond(final List<Document> documents) {
        return documents.stream()
                .map(document -> document.select("#infoArea_fixed > div.xans-element-.xans-product.xans-product-detaildesign > table > tbody"))
                .map(element -> Optional.ofNullable(element)
                        .map(Elements::text)
                        .orElse(""))
                .toList();
    }


    private static Document getDocument(final String url) {
        try {
            return Jsoup.connect(url)
                    .get();
        } catch (final Exception e) {
            throw new IllegalStateException("Get Failed");
        }
    }

    private static Document getDocument(final String url, final String baseUrl) {
        try {
            return Jsoup.connect(baseUrl + url)
                    .get();
        } catch (final Exception e) {
            throw new IllegalStateException("Get Failed");
        }
    }

    private static Elements parseDocument(final Document document, final String selector) {
        return document.select(selector);
    }
}

// .xans-product > table > tbody - 51291ms
// #infoArea_fixed > div.xans-element-.xans-product.xans-product-detaildesign > table > tbody - 50122ms
