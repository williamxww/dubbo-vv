package com.bow.service.impl;


import com.bow.service.Calculator;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class CalculatorImpl implements Calculator {
    @Override
    public int calculate(int a, int b) {
        return (a+b)*2;
    }
}
