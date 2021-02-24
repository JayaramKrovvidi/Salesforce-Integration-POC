package com.integration.poc.services.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.exceptions.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.ICompositeApiRunner;

@Service
public class CompositeApiRunnerImpl implements ICompositeApiRunner {

  @Autowired
  IApiExecutor apiExecutor;

  private static final Logger LOGGER = LoggerFactory.getLogger(CompositeApiRunnerImpl.class);

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
      try {
        apiExecutor.executeApi(currentRequest.getApiRequest());
        String nextApiKey = currentRequest.getOnSuccess()
            .get(0);
        currentRequest = getRequestConfigByApiKey(nextApiKey, apiRequestList);
      } catch (GenericException e) {
        throw new GenericException(new GenericError(Error.REST_CLIENT.getErrorCode(),
            Error.REST_CLIENT.getErrorMsg() + "Error while executing API with key: "
                + currentRequest.getApiRequest()
                    .getApiKey()));
      }
    }
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
