package com.dubbo.trace;

import com.dubbo.trace.configuration.TraceConfig;
import com.twitter.zipkin.gen.Span;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王柱星
 * @version 1.0
 * @title
 * @time 2018年10月25日
 * @since 1.0
 */
@Data
@Component
public class TraceContext {
    public static final String TRACE_ID_KEY = "traceId";
    public static final String SPAN_ID_KEY = "spanId";
    public static final String ANNO_CS = "cs";
    public static final String ANNO_CR = "cr";
    public static final String ANNO_SR = "sr";
    public static final String ANNO_SS = "ss";
    public static TraceContext traceContext;
    private static ThreadLocal<Long> TRACE_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<Long> SPAN_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<List<Span>> SPAN_LIST = new InheritableThreadLocal<>();
    @Autowired
    private TraceConfig traceConfig;

    public static Long getSpanId() {
        return SPAN_ID.get();
    }

    public static void setSpanId(Long spanId) {
        SPAN_ID.set(spanId);
    }

    public static Long getTraceId() {
        return TRACE_ID.get();
    }

    public static void setTraceId(Long traceId) {
        TRACE_ID.set(traceId);
    }

    public static void clear() {
        TRACE_ID.remove();
        SPAN_ID.remove();
        SPAN_LIST.remove();
    }

    public static void start() {
        clear();
        SPAN_LIST.set(new ArrayList<Span>());
    }

    public static TraceConfig getTraceConfig() {
        return traceContext.traceConfig;
    }

    public static void addSpan(Span span) {
        List<Span> spanList = SPAN_LIST.get();
        spanList.add(span);
    }

    @PostConstruct
    public void init() {
        traceContext = this;
    }
}
