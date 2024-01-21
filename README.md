# Java Web Crawler
Add the following .jar file to lib/ in your project.

[From Google Drive](https://drive.google.com/file/d/1cDdOfBPQ469oOCnwYXveByFSG5TC73lW/view?usp=sharing)


## How to use
#### TestClass.java
```java
package nsl.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nsl.web.crawling.MainCrawler;

public class TestCrawler extends MainCrawler {

    public TestCrawler(String entryUrl) {
        super(entryUrl);
    }

    public TestCrawler(String entryURL, int threadPoolSize) {
        super(entryURL, threadPoolSize);
    }

    private String urlPatternToGetNextTarget = "...";

    @Override
    public List<String> findNextTargets(String html, String thisPageUrl) {
        // Implement how to find targets to process next
        System.out.println(thisPageUrl);
        if (!thisPageUrl.equals(urlPAtternToGetNextTarget)) {
            System.out.println("This is not the place to find next targets");
            return null;
        }
        System.out.println("Start to find targets to crawl next.");
        List<String> nextTargets = new LinkedList<>();
        Document fullDocument = Jsoup.parse(html);
        Element postListBody = fullDocument.getElementById("postListBody");
        Elements items = postListBody.getElementsByClass("item");
        for (Element item : items) {
            Element linkElem = item.getElementsByClass("link pcol2").get(0);
            String link = linkElem.attr("href");
            nextTargets.add([domain] + link);
        }
        System.out.println(nextTargets);
        return nextTargets;
    }

    AtomicLong al = new AtomicLong(0L);

    @Override
    public void processPage(String html, String thisPageUrl) {
        // Implement how to process each web page
        // You may make use of Jsoup to parse HTMLs
        BufferedWriter bw = null;
        try {
            long id = al.incrementAndGet();
            bw = new BufferedWriter(new FileWriter(new File("[url to store file]/" + id + ".html")));
            bw.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }    
}
```

#### Main.java
```java
public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass("https://www.google.com/");
        // testClass.setCookie(...);
        // testClass.setRequestProperty(...);
        try {
            testClass.run();
        } catch (InterruptedException | ExecutionException e) {
            // Process exceptions
        }
    }
}
```

## Documentation
References index.html in docs directory
