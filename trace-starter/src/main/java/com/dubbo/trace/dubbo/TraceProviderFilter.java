package com.dubbo.trace.dubbo;

import com.alibaba.dubbo.rpc.*;
import com.dubbo.trace.TraceContext;
import com.dubbo.trace.base.BaseFilter;
import com.twitter.zipkin.gen.Span;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * @author 王柱星
 * @version 1.0
 * @title
 * @time 2018年10月25日
 * @since 1.0
 */

@Component
public class TraceProviderFilter extends BaseFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        // 开启调用链
        Span span = this.startTrace(invoker, invocation);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 远程调用
        Result result = invoker.invoke(invocation);

        // 结束调用链
        stopWatch.stop();
        // 记录出参
        this.addToBinary_annotations(span,"results",result.getValue().toString());
        this.endTrace(span, stopWatch.getTotalTimeMillis() * 1000, TraceContext.ANNO_SS);

        return result;
    }

    protected Span startTrace(Invoker<?> invoker, Invocation invocation) {
        Long traceId = Long.valueOf(invocation.getAttachment(TraceContext.TRACE_ID_KEY));
        Long spanId = Long.valueOf(invocation.getAttachment(TraceContext.SPAN_ID_KEY));

        TraceContext.setTraceId(traceId);
        TraceContext.setSpanId(spanId);

        Span span = createSpan();

        Long timeStamp = System.currentTimeMillis() * 1000;
        span.setTimestamp(timeStamp);
        addToAnnotations(span, TraceContext.ANNO_SR, timeStamp);

        span.setName("RPC:" + invoker.getInterface().getSimpleName() + ":" + invocation.getMethodName());

        // 记录入参
        addToBinary_annotations(span,"params",Arrays.toString(invocation.getArguments()));

        return span;
    }

}
