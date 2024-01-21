package com.nsl.web.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.nsl.web.data.DataContainer;
import com.nsl.web.net.HttpsRequest;
import com.nsl.web.net.HttpsRequestHtml;

/**
 * This class represents a process of crawling.
 * The instance is instantiated at the beginning of a crawling,
 * and destroyed after end of the crawling.
 */
public class Process {
    private final MainCrawler crawler;
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Set<String> failed = ConcurrentHashMap.newKeySet();
    private final String cookies;
    private final Map<String, String> requestProperties;

    /**
     * A constructor.
     * @param crawler which is responsible for crawling.
     * @param entryUrl an entry point of crawling.
     * @param cookies you can send request with cookies. If you don't want, set null.
     * @param requestProperties you can send request with request properties. If you don't want, set null.
     */
    public Process(
            MainCrawler crawler,
            String entryUrl,
            String cookies,
            Map<String, String> requestProperties
        ) {
        this.crawler = crawler;
        this.cookies = cookies;
        this.requestProperties = requestProperties;
    }

    /**
     * A constructor.
     * @param crawler which is responsible for crawling.
     * @param entryUrl an entry point of crawling.
     * @param cookies you can send request with cookies. If you don't want, set null.
     * @param requestProperties you can send request with request properties. If you don't want, set null.
     */
    public Process(
        MainCrawler crawler,
        String entryUrl,
        Map<String, String> cookies,
        Map<String, String> requestProperties
    ) {
        this.crawler = crawler;
        this.cookies = expressMapToStringOfCookies(cookies);
        this.requestProperties = requestProperties;
    }

    private String expressMapToStringOfCookies(Map<String, String> cookies) {
        StringBuilder sb = new StringBuilder();
        for (String name : cookies.keySet()) {
            String value = cookies.get(name);
            sb.append(name).append('=').append(value).append(';');
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    private static class SingleProcessUnit {
        private String cookies;
        private Map<String, String> requestProperties;
        private MainCrawler crawler;

        SingleProcessUnit(
                String cookies,
                Map<String, String> requestProperties,
                MainCrawler crawler
        ) {
            this.cookies = cookies;
            this.requestProperties = requestProperties;
            this.crawler = crawler;
        }

        public DataContainer<String> fetchPage(String urlToBrowse) throws IOException {
            HttpsRequestHtml request = HttpsRequest.getHTMLRequester(urlToBrowse);
            if (this.cookies != null) {
                request.setCookies(this.cookies);
            }

            if (this.requestProperties != null) {
                request.setProperties(this.requestProperties);
            }
            return request.request();
        }

        public void processPage(DataContainer<String> pageDataForBrowse, String thisPageUrl) {
            this.crawler.processPage(pageDataForBrowse, thisPageUrl);
        }

        public List<String> getNextTargets(DataContainer<String> pageDataForBrowse, String thisPageUrl) {
            return crawler.findNextTargets(pageDataForBrowse, thisPageUrl);
        }

        @Override
        public String toString() {
            return Thread.currentThread().getId() + Thread.currentThread().getName();
        }
    }

    /**
     * Process a page and extract URLs of next target pages.
     * @param urlToBrowse URL specifies the target.
     * @return a list contains next targets to process.
     */
    public List<String> processSinglePage(String urlToBrowse) {
        SingleProcessUnit singleProcessUnit = new SingleProcessUnit(this.cookies, this.requestProperties, this.crawler);
        List<String> nextTargets = null;
        DataContainer<String> fetchedPage = null;
        try {
            fetchedPage = singleProcessUnit.fetchPage(urlToBrowse);
        } catch (IOException e) {
            this.failed.add(urlToBrowse);
        }
        if (fetchedPage != null) {
            singleProcessUnit.processPage(fetchedPage, urlToBrowse);
            nextTargets = singleProcessUnit.getNextTargets(fetchedPage, urlToBrowse);
        }
        if (nextTargets == null) {
            nextTargets = new ArrayList<>();
        }
        return nextTargets;
    }

    /**
     * Given targetUrl, checks if the target has been visited.
     * @param targetUrl to check.
     * @return true if the crawler have visited the target, otherwise false.
     */
    public boolean isVisited(String targetUrl) {
        return this.visited.contains(targetUrl);
    }

    /**
     * Mark targetUrl that has already been visited.
     * @param targetUrl to mark.
     */
    public void markTargetToVisited(String targetUrl) {
        this.visited.add(targetUrl);
    }

    /**
     * Get URLs that the crawler failed to process, by IOException.
     * @return a set of URLs that a process of the crawler has failed to visit.
     */
    public Set<String> getFailed() {
        return Collections.unmodifiableSet(this.failed);
    }
}
