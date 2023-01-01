# Java Web Crawler
Add the following .jar file to lib/ in your project.

[From Google Drive](https://drive.google.com/file/d/1txWQaJpzN1W9WynsYtRHPPXlHCNxMgcB/view?usp=sharing)


## How to use
#### TestClass.java
```java
import com.nsl.web.crawling.MainCrawler;
import com.nsl.web.net.HttpsRequset;

public class TestClass extends MainCrawler {
    
    public TestClass(String url) {
        super(url);
    }
    
    public TestClass(String url, int threadPoolSize) {
        super(url, threadPoolSize);
    }
    
    @Override
    public void processPage(String html, String thisPageURL) {
        // Implement how to process each web page
    }
    
    @Override
    public List<String> findNextTargets(String html, String thisPageURL) {
        // Implement how to find targets to process next
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
        } catch (InterruptedException | ExecutionException | IOException e) {
            // Process exceptions
        }
    }
}
```

## Issues to fix
- There seems to exist bugs in execution processing each web page concurrently. I have to fix concurrent issue.
