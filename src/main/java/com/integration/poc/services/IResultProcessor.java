package com.integration.poc.services;

import com.integration.poc.dtos.internal.ApiRequestConfig;

public interface IResultProcessor {
	public void process(ApiRequestConfig apiRequest, String apiKey, String response);
}
