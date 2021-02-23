package com.integration.poc.services.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
  public void run(CompositeApiRequest requestConfigList) {
    try {
      GenericApiRequest apiRequest = requestConfigList.getRequestList()
          .get(0);
      apiExecutor.executeApi(apiRequest.getApiRequest());
    } catch (GenericException e) {
      LOGGER.error("Runtime Exception: {}", e.getMessage());
    }
  }

  private GenericApiRequest getRequestConfigByApiKey(String apiKey,
      List<GenericApiRequest> requestConfigList) {
    for (GenericApiRequest requestConfig : requestConfigList) {
      if (apiKey.equalsIgnoreCase(requestConfig.getApiRequest()
          .getApiKey())) {
        return requestConfig;
      }
    }
    throw new GenericException(
        new GenericError(Error.REST_CLIENT.getErrorCode(), Error.REST_CLIENT.getErrorMsg()));
  }

}
