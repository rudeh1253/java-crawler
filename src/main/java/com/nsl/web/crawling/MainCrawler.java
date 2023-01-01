package com.nsl.web.crawling;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.nsl.web.data.DataContainer;
import com.nsl.web.data.HTMLContainer;
import com.nsl.web.net.HttpsRequest;

/**
 * Crawler class defining execution of crawling.
 * Inherit this class, implement abstract methods, instantiate the subclass, and then
 * call run() method.
 * 
 * @author PGD
 */
public abstract class MainCrawler {
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;
    private final String ENTRY_URL;
    private final int threadPoolSize;
    private final Set<String> visited;
    private String cookies;
    private Map<String, String> requestProperties;
    
    /**
     * Constructor.
     * 
     * @param entryURL where crawling starts.
     */
    public MainCrawler(String entryURL) {
        this(entryURL, DEFAULT_THREAD_POOL_SIZE);
    }
    
    /**
     * Constructor.
     * 
     * @param entryURL where crawling starts.
     * @param threadPoolSize size of thread pool.
     *                       If the number of available processors < threadPoolSize,
     *                       the size of the thread pool would be the same as
     *                       the number of available processors.
     */
    public MainCrawler(String entryURL, int threadPoolSize) {
        int maxCore = Runtime.getRuntime().availableProcessors();
        this.threadPoolSize = threadPoolSize <= maxCore ? threadPoolSize : maxCore;
        this.ENTRY_URL = entryURL;
        this.visited = new HashSet<>();
        this.cookies = null;
        this.requestProperties = null;
    }
    
    /**
     * Start web crawling. After defining abstract functions and set request properties,
     * just call this method to start crawling.
     */
    public final void run() throws InterruptedException, ExecutionException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(this.threadPoolSize);
        processSingleUnit(ENTRY_URL, executor);
        executor.shutdown();
    }
    
    private void processSingleUnit(String urlToBrowse, ExecutorService executor)
            throws InterruptedException, ExecutionException {
        Future<List<String>> nextTargatesInFuture = executor.submit(() -> executionInCallable(urlToBrowse));
        // DFS
        List<String> nextTargets = nextTargatesInFuture.get();
        for (String target : nextTargets) {
            if (!this.visited.contains(target)) {
                this.visited.add(urlToBrowse);
                processSingleUnit(target, executor);
            }
        }
    }
    
    private List<String> executionInCallable(String urlToBrowse) throws Exception {
        System.out.println(Thread.currentThread().getName()); // TODO
        DataContainer container = fetchPage(urlToBrowse);
        HTMLContainer htmlContainer = (HTMLContainer)container.getData();
        processPage(htmlContainer, urlToBrowse);
        return getNextTargets(htmlContainer, urlToBrowse);
    }
    
    private DataContainer fetchPage(String urlToBrowse) throws IOException {
        HttpsRequest request = HttpsRequest.getHTMLRequester(urlToBrowse);
        setCookie(request);
        setRequestProperties(request);
        return request.request();
    }
    
    private void setCookie(HttpsRequest request) {
        if (cookies != null) {
            request.setCookies(cookies);
        }
    }
    
    private void setRequestProperties(HttpsRequest request) {
        if (requestProperties != null) {
            request.setProperties(requestProperties);
        }
    }
    
    /**
     * Set cookies into request.
     * The String should be the following format:
     * <name>=<value>;<name>=<value>;...;<name>=<value>
     * 
     * @param cookie name-value pairs of cookie.
     */
    public void setCookie(String cookie) {
        this.cookies = cookie;
    }
    
    /**
     * Set cookies into request.
     * The java.util.Map object should contain keys as
     * names of cookies, and values as values of cookies.
     * 
     * @param cookie name-value pairs of cookie.
     */
    public void setCookie(Map<String, String> cookie) {
        StringBuilder sb = new StringBuilder();
        for (String name : cookie.keySet()) {
            String value = cookie.get(name);
            sb.append(name).append('=').append(value).append(';');
        }
        setCookie(sb.substring(0, sb.length() - 1));
    }
    
    /**
     * Set request properties.
     * 
     * @param properties key-value pairs of properties.
     */
    public void setRequestProperty(Map<String, String> properties) {
        this.requestProperties = properties;
    }
    
    /**
     * Process fetched HTML data.
     * The responsibility to define how to parse the page is on you.
     * 
     * @param htmlContainer web page data.
     * @param thisPageURL URL of the page being processed now.
     */
    public void processPage(HTMLContainer htmlContainer, String thisPageURL) {
        processPage(htmlContainer.toString(), thisPageURL);
    }
    
    /**
     * Process fetched HTML data.
     * The responsibility to define how to parse the page is on you. >_<
     * 
     * @param html web page data.
     * @param thisPageURL URL of the page being processed now.
     */
    public abstract void processPage(String html, String thisPageURL);
    
    /**
     * Find target URLs to fetch next. (In DFS method)
     * If the list containing next targets contains visited URLs already,
     * such targets will be ignored. 
     * @param html TODO
     * @param thisPageURL URL of the page being processed now.
     * @return a list of target URLs.
     */
    public abstract List<String> findNextTargets(String html, String thisPageURL);
    
    public List<String> getNextTargets(HTMLContainer htmlContainer, String thisPageURL) {
        return findNextTargets(htmlContainer.toString(), thisPageURL);
    }
}