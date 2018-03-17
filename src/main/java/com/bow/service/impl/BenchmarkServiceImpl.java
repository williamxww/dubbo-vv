package com.bow.service.impl;

import java.util.List;
import java.util.Map;

import com.bow.service.BenchmarkService;

public class BenchmarkServiceImpl implements BenchmarkService {

	@Override
	public String echoService(String request) {
		return request;
	}

	@Override
	public Object echoObj(Object request) {
		return request;
	}

	@Override
	public void emptyService() {
		System.out.println("3");
	}

	@Override
	public Map<Long, Integer> getUserTypes(List<Long> uids) {
		return null;
	}

	@Override
	public long[] getLastStausIds(long[] uids) {
		return new long[0];
	}
}
