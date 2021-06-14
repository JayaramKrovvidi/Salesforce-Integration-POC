// package com.integration.poc.services.impl;
//
// import java.util.List;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.util.CollectionUtils;
// import com.integration.poc.dtos.external.CompositeApiRequest;
// import com.integration.poc.dtos.internal.ApiRequestConfig;
// import com.integration.poc.dtos.internal.GenericApiRequest;
// import com.integration.poc.dtos.internal.Handle;
// import com.integration.poc.enums.AdaptersEnum;
// import com.integration.poc.enums.Error;
// import com.integration.poc.exceptions.GenericError;
// import com.integration.poc.exceptions.GenericException;
// import com.integration.poc.services.IApiExecutor;
// import com.integration.poc.services.ICompositeApiRunner;
// import com.integration.poc.services.IResultProcessor;
// import com.integration.poc.utils.HandlerExecutorImpl;
//
// @Service
// public class CompositeApiRunnerImpl implements ICompositeApiRunner {
//
// private static final Logger LOGGER = LogManager.getLogger(CompositeApiRunnerImpl.class);
//
// @Value("${retry.hold.value}")
// private Integer retryHoldInMillis;
//
// @Autowired
// BeanFactoryServiceImpl factory;
//
// @Autowired
// HandlerExecutorImpl handleExecutor;
//
// @Autowired
// IResultProcessor genericResultProcessor;
//
// @Override
// public void run(CompositeApiRequest requestConfig) {
// List<GenericApiRequest> apiRequestList = requestConfig.getRequestList();
// if (CollectionUtils.isEmpty(apiRequestList)) {
// throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
// Error.REST_CLIENT.getErrorMsg() + "Should have atleast one request "));
// }
// executeApisSequentially(apiRequestList);
// }
//
// private void executeApisSequentially(List<GenericApiRequest> apiRequestList) {
// GenericApiRequest currentRequest = apiRequestList.get(0);
// while (null != currentRequest) {
// try {
// boolean success = executeCurrentApi(currentRequest);
// currentRequest = decideNextApi(apiRequestList, currentRequest, success);
// } catch (Exception e) {
// throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
// Error.REST_CLIENT.getErrorMsg() + "API Failure: " + currentRequest.getApiKey()));
// }
// }
// }
//
// private boolean executeCurrentApi(GenericApiRequest genericApiRequest) {
// ApiRequestConfig currentApiConfig = genericApiRequest.getApiRequest();
// LOGGER.info("API with Key: {} ready for execution", genericApiRequest.getApiKey());
// String response = findAdapterAndExecuteApi(genericApiRequest);
// System.out.println(response);
// List<Handle> successHandlers = currentApiConfig.getSuccessHandlers();
// boolean success = handleExecutor.executeHandles(genericApiRequest.getApiKey(), successHandlers);
// LOGGER.info("API with Key: {} executed and status: {} ", genericApiRequest.getApiKey(),
// success);
// genericResultProcessor.process(genericApiRequest.getApiRequest(), genericApiRequest.getApiKey(),
// response, success);
// return success;
// }
//
// private String findAdapterAndExecuteApi(GenericApiRequest genericApiRequest) {
// String requestType = genericApiRequest.getApiRequest()
// .getRequestType();
// IApiExecutor apiExecutor = factory.getBeanForClass(AdaptersEnum.getAdapterByKey(requestType));
// return apiExecutor.executeApi(genericApiRequest.getApiRequest(), genericApiRequest.getApiKey());
// }
//
// private GenericApiRequest decideNextApi(List<GenericApiRequest> apiRequestList,
// GenericApiRequest request, boolean success) throws InterruptedException {
// if (success) {
// List<String> onSuccessKeys = request.getOnSuccess();
// return getRequestConfigByApiKey(onSuccessKeys.get(0), apiRequestList);
// }
// return dealWithFailure(apiRequestList, request);
// }
//
// private GenericApiRequest dealWithFailure(List<GenericApiRequest> apiRequestList,
// GenericApiRequest request) throws InterruptedException {
// String retry = null == request.getRetry() ? "" : request.getRetry();
// if (retry.equals("*")) {
// LOGGER.info("API Call on Hold for {} seconds", retryHoldInMillis / 1000);
// Thread.sleep(retryHoldInMillis);
// return request;
// }
// Integer retryInt = retry.isEmpty() ? 0 : Integer.parseInt(retry);
// if (retryInt == 0) {
// List<String> failureKeys = request.getOnFailure();
// return getRequestConfigByApiKey(failureKeys.get(0), apiRequestList);
// }
// request.setRetry(String.valueOf(--retryInt));
// LOGGER.info("Retrying API, remaining attempts: {}", retryInt);
// return request;
// }
//
// private GenericApiRequest getRequestConfigByApiKey(String apiKey,
// List<GenericApiRequest> apiRequestList) {
// if (apiKey.equalsIgnoreCase("-1")) {
// return null;
// }
// for (GenericApiRequest requestConfig : apiRequestList) {
// if (apiKey.equalsIgnoreCase(requestConfig.getApiKey())) {
// LOGGER.info("Next API with key: {} is scheduled for execution", apiKey);
// return requestConfig;
// }
// }
// throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
// Error.REST_CLIENT.getErrorMsg() + "Couldn't find API Key " + apiKey));
// }
//
// }
