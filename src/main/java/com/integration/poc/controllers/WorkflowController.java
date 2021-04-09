package com.integration.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.services.IWorkflowService;

@RestController
@RequestMapping("workflow")
public class WorkflowController {

  @Autowired
  IWorkflowService workflowService;

  @PostMapping(name = "/initialize/{orgId}/{jsonKey}")
  public Integer initializeWorkflow(@PathVariable(name = "orgId") Integer orgId,
      @PathVariable(name = "jsonKey") String jsonKey) {
    return workflowService.initializeWorkflow(orgId, jsonKey);

  }
}
