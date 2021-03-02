package com.integration.poc.services;

import java.util.List;
import com.integration.poc.dtos.internal.Node;
import com.integration.poc.dtos.internal.PostProcessConfig;

public interface IMediator {
  public List<Node> from(String response);

  public String to(List<Node> nodes);

  public List<Node> process(List<Node> nodes, PostProcessConfig config);
}
