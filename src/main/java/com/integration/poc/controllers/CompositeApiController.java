package com.integration.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.services.ICompositeApiRunner;

@RestController
@RequestMapping("request")
public class CompositeApiController {

  @Autowired
  ICompositeApiRunner compositeApiRunner;

  @PostMapping("/composite")
  public void compositeApiTest(@RequestBody CompositeApiRequest runnerConfig) {
    compositeApiRunner.run(runnerConfig);
  }
  
  
}
