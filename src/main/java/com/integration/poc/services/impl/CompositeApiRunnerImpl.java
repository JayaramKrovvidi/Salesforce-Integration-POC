package com.integration.poc.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.dtos.internal.Handle;
import com.integration.poc.enums.AdaptersEnum;
import com.integration.poc.enums.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.HandlerExecutorImpl;

@Service
public class CompositeApiRunnerImpl implements ICompositeApiRunner {

  @Value("${retry.hold.value}")
  private Integer retryHoldInMillis;

  @Autowired
  BeanFactoryServiceImpl factory;

  @Autowired
  HandlerExecutorImpl handleExecutor;

  @Autowired
  IResultProcessor genericResultProcessor;

  @Override
  public void run(CompositeApiRequest requestConfig) {
    List<GenericApiRequest> apiRequestList = requestConfig.getRequestList();
    if (CollectionUtils.isEmpty(apiRequestList)) {
      throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
          Error.REST_CLIENT.getErrorMsg() + "Should have atleast one request "));
    }
    executeApisSequentially(apiRequestList);
  }

  private void executeApisSequentially(List<GenericApiRequest> apiRequestList) {
    GenericApiRequest currentRequest = apiRequestList.get(0);
    while (null != currentRequest) {
      ApiRequestConfig currentApiConfig = currentRequest.getApiRequest();
      System.out.println("Api key=---------------->"+currentRequest.getApiRequest().getApiKey());
      try {
        boolean success = executeCurrentApi(currentApiConfig);
        currentRequest = decideNextApi(apiRequestList, currentRequest, success);
      } catch (Exception e) {
        throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
            Error.REST_CLIENT.getErrorMsg() + "API Failure: " + currentApiConfig.getApiKey()));
      }
    }
  }

  private boolean executeCurrentApi(ApiRequestConfig currentApiConfig) {
    String response = findAdapterAndExecuteApi(currentApiConfig);
    List<Handle> successHandlers = currentApiConfig.getSuccessHandlers();
    boolean success = handleExecutor.executeHandles(currentApiConfig.getApiKey(), successHandlers);
    genericResultProcessor.process(currentApiConfig, response, success);
    return success;
  }

  private String findAdapterAndExecuteApi(ApiRequestConfig request) {
    String requestType = request.getRequestType();
    IApiExecutor apiExecutor = factory.getBeanForClass(AdaptersEnum.getAdapterByKey(requestType));
    return apiExecutor.executeApi(request);
  }

  private GenericApiRequest decideNextApi(List<GenericApiRequest> apiRequestList,
      GenericApiRequest request, boolean success) throws InterruptedException {
    ApiRequestConfig currentApiConfig = request.getApiRequest();
    String retry = currentApiConfig.getRetry();
    if (!success && retry.equalsIgnoreCase("*")) {
      Thread.sleep(retryHoldInMillis);
      return request;
    }
    List<String> nextKeys = success ? request.getOnSuccess() : request.getOnFailure();
    return getRequestConfigByApiKey(nextKeys.get(0), apiRequestList);
  }

  private GenericApiRequest getRequestConfigByApiKey(String apiKey,
      List<GenericApiRequest> apiRequestList) {
    if (apiKey.equalsIgnoreCase("-1")) {
      return null;
    }
    for (GenericApiRequest requestConfig : apiRequestList) {
      if (apiKey.equalsIgnoreCase(requestConfig.getApiRequest()
          .getApiKey())) {
        return requestConfig;
      }
    }
    throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
        Error.REST_CLIENT.getErrorMsg() + "Couldn't find API Key " + apiKey));
  }

}
