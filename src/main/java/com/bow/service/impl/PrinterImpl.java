package com.bow.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Demo._PrinterDisp;
import Ice.Current;

/**
 * @author vv
 * @since 2017/4/3.
 */
public class PrinterImpl extends _PrinterDisp {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrinterImpl.class);

	@Override
	public int printString(String s, Current __current) {
		LOGGER.info("receive " + s);
		return s.length();
	}
}
