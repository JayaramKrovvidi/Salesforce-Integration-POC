package com.integration.poc.services;

import java.util.Map;
import com.integration.poc.responseDtos.WorkFlowResponse;

public interface IWorkflowService {

  public Integer initializeWorkflow(Integer orgId, String jsonKey);

  public void startWorkflow(Integer workFlowId, Map<String, String> runConfigs);


  public WorkFlowResponse getWorkFlow(Integer workFlowId);

  public void updateWorkFlowRunConfigs(Integer workFlowId, Map<String, String> runConfigs);
}
