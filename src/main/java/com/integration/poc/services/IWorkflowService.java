package com.integration.poc.services;

import java.util.Map;
import com.integration.poc.dtos.response.WorkFlowResponse;

public interface IWorkflowService {

  public Integer initializeWorkflow(Integer orgId, String jsonKey);

  public void startWorkflow(Integer workFlowId);
  
  public void resumeWorkflow(Integer workFlowId);

  public WorkFlowResponse getWorkflowFullDetails(Integer workFlowId);

  public WorkFlowResponse getWorkFlowBasicDetails(Integer workFlowId);

  public void updateWorkFlowRunConfigs(Integer workFlowId, Map<String, String> runConfigs);
}
