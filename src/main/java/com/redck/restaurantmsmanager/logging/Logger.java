package com.redck.restaurantmsmanager.logging;

import com.redck.restaurantmsmanager.logging.enumeration.LogTag;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Logger
{
    public static org.slf4j.Logger logger;

    public Logger(final Class<?> classOfLogger) {
        logger = LoggerFactory.getLogger(classOfLogger);
    }

    public void info(final List<LogTag> tags, final String message){
        logger.info(tags + " " + message);
    }

    public void info(final String correlationId, final List<LogTag> tags, final String message){
        logger.info(correlationId + " | " + tags + " | " + message);
    }

    public void error(final List<LogTag> tags, final String message){
        logger.error(tags + " " + message);
    }

    public void warn(final List<LogTag> tags, final String message){
        logger.warn(tags + " " + message);
    }

    public void debug(final List<LogTag> tags, final String message){
        logger.debug(tags + " " + message);
    }
}
