package com.dubbo.trace.send;

import com.twitter.zipkin.gen.Span;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author 王柱星
 * @version 1.0
 * @title
 * @time 2018年10月25日
 * @since 1.0
 */
public abstract class TraceAgent {
    private static TraceAgent traceAgent;
    private final int THREAD_POOL_COUNT = 5;
    protected final ExecutorService executor =
            Executors.newFixedThreadPool(this.THREAD_POOL_COUNT, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread worker = new Thread(r);
                    worker.setName("TRACE-AGENT-WORKER");
                    worker.setDaemon(true);
                    return worker;
                }
            });

    public static TraceAgent getTraceAgent() {
        return traceAgent;
    }

    public abstract void send(Span span);

    @PostConstruct
    public void init() {
        traceAgent = this;
    }
}
