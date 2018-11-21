package com.dubbo.trace.servlet;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;
import com.dubbo.trace.TraceContext;
import com.dubbo.trace.base.BaseFilter;
import com.dubbo.trace.utils.NetworkUtils;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import zipkin.internal.moshi.Json;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class TraceServletFilter extends BaseFilter implements Filter {

    public TraceServletFilter() {
    }

    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        BufferedHttpRequestWrapper newReq = new BufferedHttpRequestWrapper(httpReq);

        Span span = this.startTrace(newReq);
        Long timeStamp = System.currentTimeMillis() * 1000;
        span.setTimestamp(timeStamp);
        addToAnnotations(span, TraceContext.ANNO_SR, timeStamp);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            chain.doFilter(newReq, resp);
            stopWatch.stop();
        } catch (Throwable var15) {
            span.addToBinary_annotations(BinaryAnnotation.create("error", var15.getMessage(),
                    Endpoint.create(NetworkUtils.getIp() + ":" + TraceContext.getTraceConfig().getServerPort() + httpReq.getRequestURL().toString(),
                            NetworkUtils.getIp(),
                            TraceContext.getTraceConfig().getServerPort())));
            this.endTrace(span, stopWatch.getTotalTimeMillis() * 1000, TraceContext.ANNO_SS);
            throw var15;
        } finally {
            HttpServletResponse var12 = (HttpServletResponse) resp;
            var12.setHeader("trace_id", String.valueOf(span.getTrace_id()));
            var12.setHeader("span_id", String.valueOf(span.getId()));
            this.endTrace(span, stopWatch.getTotalTimeMillis() * 1000, TraceContext.ANNO_SS);
        }

    }


    public Span startTrace(HttpServletRequest httpReq) {
        // 处理HTTP头部trace信息
        getTraceHttpHeader(httpReq);
        Span span = createSpan();
        span.setName("HTTP:" + NetworkUtils.ip + ":" + TraceContext.getTraceConfig().getServerPort() + httpReq.getRequestURI());

        // cookies
        // addToBinary_annotations(span,"cookies",Arrays.toString(httpReq.getCookies()));
        return span;
    }

}

