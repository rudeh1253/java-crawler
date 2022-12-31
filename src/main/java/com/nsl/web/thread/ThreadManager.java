package com.nsl.web.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Manage the thread pool.
 * 
 * @author PGD
 */
public class ThreadManager {
    private static final int DEFAULT_POOL_SIZE = 10;
    private final int POOL_SIZE;
    
    private final ExecutorService executor;
    
    public ThreadManager() {
	this(DEFAULT_POOL_SIZE);
    }
    
    public ThreadManager(int poolSize) {
	int maxCore = Runtime.getRuntime().availableProcessors();
	this.POOL_SIZE = poolSize <= maxCore
		         ? poolSize
		         : maxCore;
	executor = Executors.newFixedThreadPool(this.POOL_SIZE);
    }
    
    public void submit(Runnable task) {
	executor.submit(task);
    }
    
    public <V> Future<V> submit(Callable<V> task) {
	return executor.submit(task);
    }
    
    public void shutdown() {
	executor.shutdown();
    }
    
    public void shutdownNow() {
	executor.shutdownNow();
    }
    
    public void awaitTermination(long timeout) throws InterruptedException {
	executor.awaitTermination(timeout, TimeUnit.SECONDS);
    }
}