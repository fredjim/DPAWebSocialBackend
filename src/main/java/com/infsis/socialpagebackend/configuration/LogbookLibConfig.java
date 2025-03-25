package com.infsis.socialpagebackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import java.util.function.Predicate;

@Configuration
public class LogbookLibConfig {

    @Bean
    public Logbook logbook() {
        Predicate<HttpRequest> exclude = Conditions.exclude(
                Conditions.requestTo("/logbook/health").or(
                        Conditions.requestTo("/logbook/do-not-log"))
        );

        //DefaultSink httpSink = new DefaultSink(new DefaultHttpLogFormatter(), new DefaultHttpLogWriter());

        //DefaultSink curlSink = new DefaultSink(new CurlHttpLogFormatter(), new DefaultHttpLogWriter());

        DefaultSink jsonSink = new DefaultSink(new JsonHttpLogFormatter(), new DefaultHttpLogWriter());

        return Logbook.builder()
                .condition(exclude)
                .sink(jsonSink)
                .build();
    }
}
