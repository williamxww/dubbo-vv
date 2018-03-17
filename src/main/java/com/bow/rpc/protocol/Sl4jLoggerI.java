package com.bow.rpc.protocol;

import org.slf4j.LoggerFactory;

import Ice.Logger;

/**
 * @author wwxiang
 * @since 2017/6/27.
 */
public class Sl4jLoggerI implements Logger {

	private final org.slf4j.Logger logger;

    public String getPrefix() {
        return prefix;
    }

    private final String prefix;

	public Sl4jLoggerI(String prefix) {
		logger = LoggerFactory.getLogger(prefix);
		this.prefix = prefix;
	}

	@Override
	public void trace(String category, String message) {
		logger.debug(category + " " + message);
	}

	@Override
	public void print(String message) {
		logger.info(message);
	}

	@Override
	public void warning(String message) {
		logger.warn(message);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}

	@Override
	public Logger cloneWithPrefix(String prefix) {
		return new Sl4jLoggerI(prefix);
	}
}
