package com.bow.transport.netty4;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import io.netty.util.internal.logging.AbstractInternalLogger;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 将dubbo的logger设给netty
 * 
 * @author vv
 * @since 2016/3/18.
 */
final class Netty4Helper {

    public static void setNettyLoggerFactory() {
        InternalLoggerFactory factory = InternalLoggerFactory.getDefaultFactory();
        if (factory == null || !(factory instanceof DubboLoggerFactory)) {
            InternalLoggerFactory.setDefaultFactory(new DubboLoggerFactory());
        }
    }

    static class DubboLoggerFactory extends InternalLoggerFactory {

        @Override
        public InternalLogger newInstance(String name) {
            return new DubboLogger(LoggerFactory.getLogger(name));
        }
    }

    static class DubboLogger extends AbstractInternalLogger {

        /**
         * dubbo logger
         */
        private Logger logger;

        DubboLogger(Logger logger) {
            super(logger.toString());
            this.logger = logger;
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        @Override
        public void trace(String msg) {
            logger.trace(msg);
        }

        @Override
        public void trace(String format, Object arg) {
            String msg = String.format(format, arg);
            this.trace(msg);
        }

        @Override
        public void trace(String format, Object argA, Object argB) {
            String msg = String.format(format, argA, argB);
            this.trace(msg);
        }

        @Override
        public void trace(String format, Object... arguments) {
            String msg = String.format(format, arguments);
            this.trace(msg);
        }

        @Override
        public void trace(String msg, Throwable t) {
            logger.trace(msg, t);
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        @Override
        public void debug(String msg) {
            logger.debug(msg);
        }

        @Override
        public void debug(String format, Object arg) {
            String msg = String.format(format, arg);
            this.debug(msg);
        }

        @Override
        public void debug(String format, Object argA, Object argB) {
            String msg = String.format(format, argA, argB);
            this.debug(msg);
        }

        @Override
        public void debug(String format, Object... arguments) {
            String msg = String.format(format, arguments);
            this.debug(msg);
        }

        @Override
        public void debug(String msg, Throwable t) {
            logger.debug(msg, t);
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        @Override
        public void info(String msg) {
            logger.info(msg);
        }

        @Override
        public void info(String format, Object arg) {
            String msg = String.format(format, arg);
            this.info(msg);
        }

        @Override
        public void info(String format, Object argA, Object argB) {
            String msg = String.format(format, argA, argB);
            this.info(msg);
        }

        @Override
        public void info(String format, Object... arguments) {
            String msg = String.format(format, arguments);
            this.info(msg);
        }

        @Override
        public void info(String msg, Throwable t) {
            logger.info(msg, t);
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isWarnEnabled();
        }

        @Override
        public void warn(String msg) {
            logger.warn(msg);
        }

        @Override
        public void warn(String format, Object arg) {
            String msg = String.format(format, arg);
            this.warn(msg);
        }

        @Override
        public void warn(String format, Object argA, Object argB) {
            String msg = String.format(format, argA, argB);
            this.warn(msg);
        }

        @Override
        public void warn(String format, Object... arguments) {
            String msg = String.format(format, arguments);
            this.warn(msg);
        }

        @Override
        public void warn(String msg, Throwable t) {
            logger.warn(msg, t);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        @Override
        public void error(String msg) {
            logger.error(msg);
        }

        @Override
        public void error(String format, Object arg) {
            String msg = String.format(format, arg);
            this.error(msg);
        }

        @Override
        public void error(String format, Object argA, Object argB) {
            String msg = String.format(format, argA, argB);
            this.error(msg);
        }

        @Override
        public void error(String format, Object... arguments) {
            String msg = String.format(format, arguments);
            this.error(msg);
        }

        @Override
        public void error(String msg, Throwable t) {
            logger.error(msg, t);
        }
    }

}
