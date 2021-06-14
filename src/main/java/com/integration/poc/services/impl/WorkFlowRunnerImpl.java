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


  public void executeWorkflow(WorkflowState wf) throws InterruptedException {
    while (wf.getCurrentApiId() != -1) {
      executeAndUpdateCurrentApi(wf);
    }
    wf.setStatus(StatusConstants.SUCCESS);
    workflowRepo.save(wf);
  }

  public void executeAndUpdateCurrentApi(WorkflowState workflowState) throws InterruptedException {
    Integer currentApiId = workflowState.getCurrentApiId();
    Optional<ApiState> currentApiSearch = apiStateRepo.findById(currentApiId);
    if (currentApiSearch.isPresent()) {

      // Set Current API status to In Progress
      ApiState currentApi = currentApiSearch.get();
      currentApi.setStatus(StatusConstants.IN_PROGRESS);
      apiStateRepo.save(currentApi);

      // Execute API and update API State and Workflow State
      boolean success = checkStatusAndExecuteApi(currentApi);
      currentApi.setStatus(success ? StatusConstants.SUCCESS : StatusConstants.FAILURE);
      apiStateRepo.save(currentApi);
      updateNextApiIntoWorkflowState(success, currentApi, workflowState);
    }
  }

  private boolean checkStatusAndExecuteApi(ApiState apiState) {
    ApiRequestConfig apiConfig = apiState.getRequestConfig();
    String response = getApiResponse(apiState);
    List<Handle> successHandlers = apiConfig.getSuccessHandlers();
    boolean success = handleExecutor.executeHandles(apiState.getApiKey(), successHandlers);
    genericResultProcessor.process(apiConfig, String.valueOf(apiState.getApiId()), response,
        success);
    return success;
  }

  /**
   * Check the Status of the given API. If Not executed till now, execute and return the response.
   * If already executed, return the response directly
   *
   * @param apiState the api state
   * @return the api response
   */
  private String getApiResponse(ApiState apiState) {
    String apiStatus = apiState.getStatus();
    return apiStatus.equals(StatusConstants.INITIALIZED) ? findAdapterAndExecuteApi(apiState)
        : apiState.getResponse();
  }

  /**
   * Find the correct adapter (REST, SOAP, DB etc.,) for the given api and execute.
   *
   * @param apiState the api state
   * @return API response as String
   */
  private String findAdapterAndExecuteApi(ApiState apiState) {
    ApiRequestConfig requestConfig = apiState.getRequestConfig();
    String requestType = requestConfig.getRequestType();
    IApiExecutor apiExecutor = factory.getBeanForClass(AdaptersEnum.getAdapterByKey(requestType));
    return apiExecutor.executeApi(requestConfig, apiState.getApiKey(), apiState.getWfId());
  }

  /**
   * Decide on the next API to be executed based on success or failure and update the Workflow State
   * object with the new API ID
   *
   * @param success the success
   * @param apiState the api state
   * @param workflowState the workflow state
   * @throws InterruptedException the interrupted exception
   */
  private void updateNextApiIntoWorkflowState(boolean success, ApiState apiState,
      WorkflowState workflowState) throws InterruptedException {
    Integer nextApiId = success ? dealWithSuccess(apiState) : dealFailure(apiState);
    workflowState.setCurrentApiId(nextApiId);
    workflowRepo.save(workflowState);
    apiStateRepo.save(apiState);
  }

  private Integer dealWithSuccess(ApiState apiState) {
    List<String> onSuccessList = apiState.getOnSuccess();
    apiState.setStatus(StatusConstants.SUCCESS);
    return Integer.parseInt(onSuccessList.get(0));
  }

  private Integer dealFailure(ApiState apiState) throws InterruptedException {
    String retry = null == apiState.getRetry() ? "" : apiState.getRetry();
    if (retry.equals("*")) {
      Thread.sleep(retryHoldInMillis);
      return apiState.getApiId();
    }
    return dealWithRetry(apiState, retry);
  }

  private Integer dealWithRetry(ApiState apiState, String retry) {
    Integer retryInt = retry.isEmpty() ? 0 : Integer.parseInt(retry);
    if (retryInt.equals(0)) {
      List<String> onFailureList = apiState.getOnFailure();
      apiState.setStatus(StatusConstants.FAILURE);
      return Integer.parseInt(onFailureList.get(0));
    } else {
      apiState.setRetry(String.valueOf(--retryInt));
      return apiState.getApiId();
    }
  }

}
