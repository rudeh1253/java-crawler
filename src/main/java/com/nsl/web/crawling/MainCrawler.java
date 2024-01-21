package com.nsl.web.crawling;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nsl.web.data.Buffer;
import com.nsl.web.data.DataContainer;

/**
 * Crawler class defining execution of crawling.
 * Inherit this class, implement abstract methods, instantiate the subclass, and then
 * call run() method.
 * 
 * @author PGD
 */
public abstract class MainCrawler {
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;
    private final String entryUrl;
    private final Set<String> failed = new HashSet<>();
    private String cookies;
    private Map<String, String> requestProperties;
    
    /**
     * Constructor.
     * 
     * @param entryUrl where crawling starts.
     */
    public MainCrawler(String entryUrl) {
        this(entryUrl, DEFAULT_THREAD_POOL_SIZE);
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
        this.entryUrl = entryURL;
        this.cookies = null;
        this.requestProperties = null;
    }
    
    /**
     * Start web crawling. After defining abstract functions and set request properties,
     * just call this method to start crawling.
     * @return a set data structure containing failed page urls
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public final Set<String> run() throws InterruptedException, ExecutionException {
        return start(entryUrl);
    }
    
    private Set<String> start(String entryUrl)
            throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Process process = new Process(this, entryUrl, this.cookies, this.requestProperties);
        processSingle(entryUrl, process, executor);
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        return process.getFailed();
    }

    private void processSingle(String urlToBrowse, Process process, ExecutorService executor)
            throws InterruptedException, ExecutionException {
        List<String> nextTargets = process.processSinglePage(urlToBrowse);
        executeNext(executor, process, nextTargets);
    }

    private void executeNext(ExecutorService executor, Process process, List<String> nextTargets) {
        // DFS
        for (String target : nextTargets) {
            if (!process.isVisited(target)) {
                process.markTargetToVisited(target);
                if (!executor.isShutdown()) {
                    executor.submit(() -> {
                        try {
                            processSingle(target, process, executor);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
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
     * @param htmlContainer web page data.
     * @param thisPageUrl URL of the page being processed now.
     */
    protected void processPage(DataContainer<String> htmlContainer, String thisPageUrl) {
        processPage(containerToString(htmlContainer), thisPageUrl);
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
     * @param html web page data.
     * @param thisPageUrl URL of the page being processed now.
     */
    protected abstract void processPage(String html, String thisPageUrl);
    
    /**
     * Find target URLs to fetch next. (In DFS method)
     * If the list containing next targets contains visited URLs already,
     * such targets will be ignored. 
     * @param html current page which finding next targets within
     * @param thisPageUrl URL of the page being processed now
     * @return a list of target URLs.
     */
    protected abstract List<String> findNextTargets(String html, String thisPageUrl);
    
    protected List<String> findNextTargets(DataContainer<String> htmlContainer, String thisPageUrl) {
        return findNextTargets(containerToString(htmlContainer), thisPageUrl);
    }

    /**
     * Return failed URLs.
     * @return conatinig URLs which failed to fetch.
     */
    public final Set<String> getFailedPageURLs() {
        return this.failed;
    }
}