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

public class Process {
    private final MainCrawler crawler;
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Set<String> failed = ConcurrentHashMap.newKeySet();
    private final String cookies;
    private final Map<String, String> requestProperties;

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

        public void processPage(DataContainer<String> pageDataForBrowse) {
            this.crawler.processPage(pageDataForBrowse);
        }

        public List<String> getNextTargets(DataContainer<String> pageDataForBrowse) {
            return crawler.findNextTargets(pageDataForBrowse);
        }

        @Override
        public String toString() {
            return Thread.currentThread().getId() + Thread.currentThread().getName();
        }
    }

    public List<String> processSingleUnit(String urlToBrowse) {
        SingleProcessUnit singleProcessUnit = new SingleProcessUnit(this.cookies, this.requestProperties, this.crawler);
        List<String> nextTargets = null;
        DataContainer<String> fetchedPage = null;
        try {
            fetchedPage = singleProcessUnit.fetchPage(urlToBrowse);
        } catch (IOException e) {
            this.failed.add(urlToBrowse);
        }
        if (fetchedPage != null) {
            singleProcessUnit.processPage(fetchedPage);
            nextTargets = singleProcessUnit.getNextTargets(fetchedPage);
        }
        if (nextTargets == null) {
            nextTargets = new ArrayList<>();
        }
        return nextTargets;
    }

    public boolean isVisited(String targetUrl) {
        return this.visited.contains(targetUrl);
    }

    public void checkTargetToVisited(String targetUrl) {
        this.visited.add(targetUrl);
    }

    public Set<String> getFailed() {
        return Collections.unmodifiableSet(this.failed);
    }
}
