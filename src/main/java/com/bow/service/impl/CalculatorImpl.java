package com.bow.service.impl;

import com.bow.service.Calculator;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class CalculatorImpl implements Calculator {
	@Override
	public int calculate(int a, int b) {
		return (a + b) * 2;
	}

	@Override
	public int add(int a, int b) {
		return a + b;
	}

	@Override
	public int sub(int a, int b) {
		return a - b;
	}
}
