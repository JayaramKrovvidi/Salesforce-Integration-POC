package com.integration.poc.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.utils.UrlBuilderUtil;

@Service
public class CompositeApiRunnerImpl implements ICompositeApiRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompositeApiRunnerImpl.class);

  @Override
  public void run(CompositeApiRequest runnerConfig) {
    for (GenericApiRequest apiRequest : runnerConfig.getRequestList()) {
      String url = UrlBuilderUtil.buildUrl(apiRequest.getApiRequest());
      LOGGER.info("Url: {}", url);
    }
  }

}
