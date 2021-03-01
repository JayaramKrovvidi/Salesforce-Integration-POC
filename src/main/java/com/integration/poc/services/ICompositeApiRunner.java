package com.integration.poc.services;

import com.integration.poc.dtos.external.CompositeApiRequest;

public interface ICompositeApiRunner {
  public void run(CompositeApiRequest runnerConfig);
}
