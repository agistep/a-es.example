package io.agistep.event.repository;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import io.agistep.event.Event;
import io.agistep.event.serializer.ConvertUtil;
import org.slf4j.LoggerFactory;

import java.util.List;

class EventLogger {

    private final static Logger logger = (Logger) LoggerFactory.getLogger("EVENT_POLLING_PUBLISHER");
    static {
        LoggerContext loggerContext = logger.getLoggerContext();
        loggerContext.reset();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%message%n");
        encoder.start();


        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(loggerContext);
        appender.setEncoder(encoder);
        appender.setAppend(true);
        appender.setFile("/Users/q43qtc41f2/workspace/event");//TODO
        appender.start();
        logger.addAppender(appender);
    }

    static void log(List<Event> events) {
        events.forEach(EventLogger::outboxing);
    }

    static void outboxing(Event e) {
        logger.info(ConvertUtil.convert(e));
    }

}
