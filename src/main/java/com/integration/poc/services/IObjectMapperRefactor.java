package com.integration.poc.services;

import java.util.List;
import com.integration.poc.dtos.internal.PostProcessConfig;

public interface IObjectMapperRefactor {

 

  public List<String> run(List<List<String>> rows, PostProcessConfig mapper);
}
