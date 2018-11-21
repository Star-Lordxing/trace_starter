package com.dubbo.trace.base;

import com.dubbo.trace.TraceContext;
import com.dubbo.trace.send.TraceAgent;
import com.dubbo.trace.utils.IdUtils;
import com.dubbo.trace.utils.NetworkUtils;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;


public abstract class BaseFilter {

    /**
     * 创建span信息
     */
    protected Span createSpan() {
        Span span = new Span();

        long id = IdUtils.getId();
        span.setId(id);

        Long traceId = TraceContext.getTraceId();
        // 首次调用
        if (traceId == null) {
            TraceContext.start();
            traceId = id;
            TraceContext.setTraceId(traceId);
        }

        span.setTrace_id(traceId);
        span.setName(TraceContext.getTraceConfig().getApplicationName());

        // 首次调用spanId和parentId相等
        if (TraceContext.getSpanId() == null) {
            span.setParent_id(span.getId());
        }

        span.setParent_id(TraceContext.getSpanId());
        TraceContext.setSpanId(span.getId());

        return span;
    }

    /**
     * 添加节点信息
     */
    public void addToAnnotations(Span span, String traceType, Long timeStamp) {
        span.addToAnnotations(
                Annotation.create(timeStamp, traceType,
                        Endpoint.create(TraceContext.getTraceConfig().getApplicationName(),
                                NetworkUtils.getIp(),
                                TraceContext.getTraceConfig().getServerPort()))
        );
    }

    /**
     * 增加接口信息
     */
    protected  void addToBinary_annotations(Span span, String key, String value){
        span.addToBinary_annotations(BinaryAnnotation.create(key, value,
                Endpoint.create(TraceContext.getTraceConfig().getApplicationName(),
                        NetworkUtils.getIp(),
                        TraceContext.getTraceConfig().getServerPort())));
    }

    /**
     * 结束调用链
     */
    public void endTrace(Span span, Long duration, String traceType) {
        addToAnnotations(span, traceType, System.currentTimeMillis() * 1000);
        span.setDuration(duration);
        TraceAgent.getTraceAgent().send(span);
    }

    protected void getTraceHttpHeader(HttpServletRequest httpReq) {

        String traceId = httpReq.getHeader("trace_id");
        String spanId = httpReq.getHeader("span_id");

        if(StringUtils.isNotBlank(traceId)){
            TraceContext.setTraceId(Long.parseLong(traceId));
        }

        if(StringUtils.isNotBlank(spanId)){
            TraceContext.setSpanId(Long.parseLong(spanId));
        }
    }

    protected void setTraceToHttpHeader(HttpRequest httpRequest , Span span){
        // 内部请求可以携带trace信息，外部请求改行代码注释掉
        httpRequest.getHeaders().set("trace_id", String.valueOf(span.getTrace_id()));
        httpRequest.getHeaders().set("span_id", String.valueOf(span.getId()));
    }

}