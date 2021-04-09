package com.integration.poc.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.dtos.response.WorkFlowResponse;
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

  @PostMapping(value = "/start/{workFlowId}")
  public void startWorkFlow(@RequestBody Map<String, String> runConfigs,
      @PathVariable(name = "workFlowId") Integer workFlowId) {
    workflowService.startWorkflow(workFlowId, runConfigs);
  }


  @GetMapping(value = "/get/{workFlowId}")
  public WorkFlowResponse getWorkflow(@PathVariable(name = "workFlowId") Integer workFlowId) {
    return workflowService.getWorkFlow(workFlowId);
  }

  @PutMapping(value = "/updateRunConfig/{workFlowId}")
  public void updateWorkFlow(@PathVariable(name = "workFlowId") Integer workFlowId,
      @RequestBody Map<String, String> runConfigs) {
    workflowService.updateWorkFlowRunConfigs(workFlowId, runConfigs);
  }

}
