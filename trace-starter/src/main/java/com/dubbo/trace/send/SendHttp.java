package com.dubbo.trace.send;

import com.github.kristofa.brave.http.HttpSpanCollector;
import com.twitter.zipkin.gen.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


@Component
@DependsOn("zipkinConfig")
public class SendHttp extends TraceAgent {

    @Autowired
    private HttpSpanCollector httpSpanCollector;


    @Override
    public void send(Span span) {
        if (span != null) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(span.toString());
                    httpSpanCollector.collect(span);
                    httpSpanCollector.flush();
                }
            });
        }
    }

}
