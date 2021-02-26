package com.integration.poc.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.dtos.internal.ObjectMapper;
import com.integration.poc.enums.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.utils.HandlerExecutorImpl;

@Service
public class CompositeApiRunnerImpl implements ICompositeApiRunner {

  @Value("${retry.hold.value}")
  private Integer retryHoldInMillis;

  @Autowired
  IApiExecutor apiExecutor;

  @Autowired
  HandlerExecutorImpl handleExecutor;
  
  List<ObjectMapper> objectMapper = new ArrayList<ObjectMapper>();
  
  public List<ObjectMapper> getObjectMapper(){
	  return objectMapper;
  }

  @Override
  public void run(CompositeApiRequest requestConfig) {
    List<GenericApiRequest> apiRequestList = requestConfig.getRequestList();
    objectMapper = requestConfig.getObjectMapper();
    if (CollectionUtils.isEmpty(apiRequestList)) {
      throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
          Error.REST_CLIENT.getErrorMsg() + "Should have atleast one request "));
    }
    executeApisSequentially(apiRequestList);
  }

  private void executeApisSequentially(List<GenericApiRequest> apiRequestList) {
    GenericApiRequest currentRequest = apiRequestList.get(0);
    while (null != currentRequest) {
      try {
        ApiRequestConfig currentApiConfig = currentRequest.getApiRequest();
        //object mapper to send
        apiExecutor.executeApi(currentRequest.getApiRequest());
        boolean success = handleExecutor.executeHandles(currentApiConfig.getApiKey(),
            currentApiConfig.getSuccessHandlers());
        currentRequest =
            decideNextApiOnSuccess(apiRequestList, currentRequest, currentApiConfig, success);

      } catch (GenericException e) {
        throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
            Error.REST_CLIENT.getErrorMsg() + "Error while executing API with key: "
                + currentRequest.getApiRequest()
                    .getApiKey()));
      } catch (InterruptedException ie) {
        ie.getMessage();
      }
    }
  }

  private GenericApiRequest decideNextApiOnSuccess(List<GenericApiRequest> apiRequestList,
      GenericApiRequest currentRequest, ApiRequestConfig currentApiConfig, boolean success)
      throws InterruptedException {
    if (success) {
      String nextApiKey = currentRequest.getOnSuccess()
          .get(0);
      currentRequest = getRequestConfigByApiKey(nextApiKey, apiRequestList);
    } else if (currentApiConfig.getRetry()
        .equalsIgnoreCase("*")) {
      Thread.sleep(retryHoldInMillis);
    } else {
      String nextApiKey = currentRequest.getOnFailure()
          .get(0);
      currentRequest = getRequestConfigByApiKey(nextApiKey, apiRequestList);
    }
    return currentRequest;
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
