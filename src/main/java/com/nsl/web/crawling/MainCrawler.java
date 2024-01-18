package com.nsl.web.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nsl.web.data.Buffer;
import com.nsl.web.data.DataContainer;
import com.nsl.web.net.HttpsRequest;
import com.nsl.web.net.HttpsRequestHtml;

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
    private final Set<String> visited;
    private final Set<String> failed = new HashSet<>();
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
        this.ENTRY_URL = entryURL;
        this.visited = ConcurrentHashMap.newKeySet();
        this.cookies = null;
        this.requestProperties = null;
    }
    
    /**
     * Start web crawling. After defining abstract functions and set request properties,
     * just call this method to start crawling.
     */
    public final void run() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        this.visited.add(ENTRY_URL);
        processSingleUnit(ENTRY_URL, executor);
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
    
    private void processSingleUnit(String urlToBrowse, ExecutorService executor)
            throws InterruptedException, ExecutionException {
        List<String> nextTargets = null;
        try {
            nextTargets = extract(urlToBrowse);
        } catch (IOException e1) {
            failed.add(urlToBrowse);
        }
        if (nextTargets == null) {
            nextTargets = new ArrayList<>();
        }
        executeNext(urlToBrowse, executor, nextTargets);
    }

    private void executeNext(String urlToBrowse, ExecutorService executor, List<String> nextTargets) {
        // DFS
        for (String target : nextTargets) {
            if (!this.visited.contains(target)) {
                this.visited.add(target);
                if (!executor.isShutdown()) {
                    executor.submit(() -> {
                        try {
                            processSingleUnit(target, executor);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
    
    private List<String> extract(String urlToBrowse) throws IOException {
        DataContainer<String> container = fetchPage(urlToBrowse);
        processPage(container, urlToBrowse);
        return getNextTargets(container, urlToBrowse);
    }
    
    private DataContainer<String> fetchPage(String urlToBrowse) throws IOException {
        HttpsRequestHtml request = HttpsRequest.getHTMLRequester(urlToBrowse);
        addCookies(request);
        addRequestProperties(request);
        return request.request();
    }
    
    private <D> void addCookies(HttpsRequest<D> request) {
        if (cookies != null) {
            request.setCookies(cookies);
        }
    }
    
    private <D> void addRequestProperties(HttpsRequest<D> request) {
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
    protected void processPage(DataContainer<String> htmlContainer, String thisPageURL) {
        processPage(containerToString(htmlContainer), thisPageURL);
    }

    private String containerToString(DataContainer<String> htmlContainer) {
        StringBuilder sb = new StringBuilder();
        List<Buffer<String>> data = htmlContainer.getData();
        for (Buffer<String> buffer : data) {
            sb.append(buffer.getBuffer());
        }
        return sb.toString();
    }
    
    /**
     * Process fetched HTML data.
     * The responsibility to define how to parse the page is on you. >_<
     * 
     * @param html web page data.
     * @param thisPageURL URL of the page being processed now.
     */
    protected abstract void processPage(String html, String thisPageURL);
    
    /**
     * Find target URLs to fetch next. (In DFS method)
     * If the list containing next targets contains visited URLs already,
     * such targets will be ignored. 
     * @param html current page which finding next targets within
     * @param thisPageURL URL of the page being processed now.
     * @return a list of target URLs.
     */
    protected abstract List<String> findNextTargets(String html, String thisPageURL);
    
    protected List<String> getNextTargets(DataContainer<String> htmlContainer, String thisPageURL) {
        return findNextTargets(containerToString(htmlContainer), thisPageURL);
    }

    /**
     * Return failed URLs.
     * @return conatinig URLs which failed to fetch.
     */
    public final Set<String> getFailedPageURLs() {
        return this.failed;
    }
}