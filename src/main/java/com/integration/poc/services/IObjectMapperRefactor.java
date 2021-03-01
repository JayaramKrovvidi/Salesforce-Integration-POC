package com.integration.poc.services;

import java.util.List;
import com.integration.poc.dtos.internal.ObjectMapper;

public interface IObjectMapperRefactor {

  public List<String> run(String[] rows, ObjectMapper mapper);
}
