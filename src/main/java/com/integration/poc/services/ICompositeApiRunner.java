package com.integration.poc.services;

import java.util.List;

import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.ObjectMapper;

public interface ICompositeApiRunner {
  public void run(CompositeApiRequest runnerConfig);
  public List<ObjectMapper> getObjectMapper();
}
