package com.integration.poc.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.integration.poc.constants.StatusConstants;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.Handle;
import com.integration.poc.enums.AdaptersEnum;
import com.integration.poc.models.ApiState;
import com.integration.poc.models.WorkflowState;
import com.integration.poc.repositories.IApiStateRepository;
import com.integration.poc.repositories.IWorkflowRepository;
import com.integration.poc.services.IApiExecutor;
import com.integration.poc.services.IResultProcessor;
import com.integration.poc.utils.HandlerExecutorImpl;

@Service
public class WorkFlowRunnerImpl {

  @Value("${retry.hold.value}")
  private Integer retryHoldInMillis;

  @Autowired
  IApiStateRepository apiStateRepo;

  @Autowired
  BeanFactoryServiceImpl factory;

  @Autowired
  HandlerExecutorImpl handleExecutor;

  @Autowired
  IResultProcessor genericResultProcessor;

  @Autowired
  IWorkflowRepository workflowRepo;


  public void process(Integer workFlowId) throws InterruptedException {
    while (true) {
      Optional<WorkflowState> workFlowOptional = workflowRepo.findById(workFlowId);
      if (workFlowOptional.isPresent()) {
        WorkflowState workflow = workFlowOptional.get();
        if (workflow.getCurrentApiId() == -1) {
          workflow.setStatus(StatusConstants.SUCCESS);
          break;
        }
        executeComposite(workflow);
      }
    }
  }

  public void executeComposite(WorkflowState workflowState) throws InterruptedException {
    Integer currentApiId = workflowState.getCurrentApiId();
    Optional<ApiState> currentApi = apiStateRepo.findById(currentApiId);
    if (currentApi.isPresent()) {
      ApiState apiState = currentApi.get();
      apiState.setStatus(StatusConstants.IN_PROGRESS);
      ApiState apiState2 = apiStateRepo.save(apiState);
      boolean success = executeCurrentApi(apiState2, workflowState.getWfId());
      isSuccess(success, apiState, workflowState);
    }
  }


  private void isSuccess(boolean success, ApiState apiState, WorkflowState workflowState)
      throws InterruptedException {
    if (success) {
      Integer nextApiID = Integer.parseInt(apiState.getOnSuccess()
          .get(0));
      apiState.setStatus(StatusConstants.SUCCESS);
      apiStateRepo.save(apiState);
      workflowState.setCurrentApiId(nextApiID);
      workflowRepo.save(workflowState);
    } else {
      dealWithFailure(apiState, workflowState);
    }
  }

  private void dealWithFailure(ApiState apiState, WorkflowState workflowState)
      throws InterruptedException {
    String retry = null == apiState.getRetry() ? "" : apiState.getRetry();
    if (retry.equals("*")) {
      Thread.sleep(retryHoldInMillis);
    }
    Integer retryInt = retry.isEmpty() ? 0 : Integer.parseInt(retry);
    if (retryInt.equals(0)) {
      workflowState.setCurrentApiId(Integer.parseInt(apiState.getOnFailure()
          .get(0)));
      apiState.setStatus(StatusConstants.FAILURE);
      apiStateRepo.save(apiState);
      workflowRepo.save(workflowState);
    } else {
      apiState.setRetry(String.valueOf(--retryInt));
      apiStateRepo.save(apiState);
    }
  }

  private boolean executeCurrentApi(ApiState apiState, Integer workFlowId) {
    ApiRequestConfig currentApiConfig = apiState.getRequestConfig();
    String response = findAdapterAndExecuteApi(apiState, workFlowId);
    List<Handle> successHandlers = currentApiConfig.getSuccessHandlers();
    boolean success = handleExecutor.executeHandles(apiState.getApiKey(), successHandlers);
    genericResultProcessor.process(currentApiConfig, String.valueOf(apiState.getApiId()), response,
        success);
    return success;
  }

  private String findAdapterAndExecuteApi(ApiState apiState, Integer workFlowId) {
    ApiRequestConfig requestConfig = apiState.getRequestConfig();
    String requestType = requestConfig.getRequestType();
    IApiExecutor apiExecutor = factory.getBeanForClass(AdaptersEnum.getAdapterByKey(requestType));
    return apiExecutor.executeApi(requestConfig, apiState.getApiKey(), workFlowId);
  }

}