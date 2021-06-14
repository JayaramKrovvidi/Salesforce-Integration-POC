package com.integration.poc.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.constants.StatusConstants;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.dtos.internal.GenericApiRequest;
import com.integration.poc.dtos.response.WorkFlowResponse;
import com.integration.poc.enums.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.models.ApiState;
import com.integration.poc.models.OrgJsonStore;
import com.integration.poc.models.WorkflowState;
import com.integration.poc.repositories.IApiStateRepository;
import com.integration.poc.repositories.IOrgJsonStoreRepository;
import com.integration.poc.repositories.IWorkflowRepository;
// import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.services.IWorkflowService;

@Service
public class WorkflowServiceImpl implements IWorkflowService {

  @Autowired
  IOrgJsonStoreRepository jsonStoreRepo;

  @Autowired
  IWorkflowRepository workflowRepo;

  @Autowired
  IApiStateRepository apiStateRepo;

  @Autowired
  WorkFlowRunnerImpl workFlowRunnerImpl;

  private static final String WF_NOT_PRESENT_ERR_MSG = "Requested workflow not found";
  private static final Logger LOGGER = LogManager.getLogger(WorkflowServiceImpl.class);

  // -------- Workflow Initialize Flow ---------

  @Override
  @Transactional
  public Integer initializeWorkflow(Integer orgId, String jsonKey) {
    Optional<OrgJsonStore> jsonStoreSearch = jsonStoreRepo.findByOrgIdAndJsonKey(orgId, jsonKey);
    if (jsonStoreSearch.isPresent()) {
      OrgJsonStore jsonStore = jsonStoreSearch.get();
      CompositeApiRequest compositeApi = jsonStore.getJsonString();
      WorkflowState workflow = createWorkflowAndApis(jsonStore, compositeApi);
      return workflow.getWfId();
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + "Requested Json not found"));
  }

  private WorkflowState createWorkflowAndApis(OrgJsonStore jsonStore,
      CompositeApiRequest compositeApi) {
    WorkflowState workflow = initializeWorkflowRecord(jsonStore);
    List<ApiState> apiList = createApiStateRecords(compositeApi, workflow);
    updateApiKeysWithIds(apiList);
    return workflow;
  }

  private WorkflowState initializeWorkflowRecord(OrgJsonStore jsonStore) {
    WorkflowState workflow = new WorkflowState();
    workflow.setJsonId(jsonStore.getId());
    workflow.setStatus(StatusConstants.INITIALIZED);
    workflow.setLastModifiedTm(LocalDateTime.now());
    workflowRepo.save(workflow);
    return workflow;
  }

  private List<ApiState> createApiStateRecords(CompositeApiRequest compositeApi,
      WorkflowState workflow) {
    return compositeApi.getRequestList()
        .stream()
        .map(apiRequest -> apiStateRepo.save(createApiStateRecord(workflow.getWfId(), apiRequest)))
        .collect(Collectors.toList());
  }

  private ApiState createApiStateRecord(Integer wfId, GenericApiRequest apiRequest) {
    ApiState apiState = new ApiState();
    apiState.setWfId(wfId);
    apiState.setApiKey(apiRequest.getApiKey());
    apiState.setRetry(apiRequest.getRetry());
    apiState.setRequestConfig(apiRequest.getApiRequest());
    apiState.setStatus(StatusConstants.INITIALIZED);
    apiState.setOnSuccess(apiRequest.getOnSuccess());
    apiState.setOnFailure(apiRequest.getOnFailure());
    apiState.setLastModifiedTm(LocalDateTime.now());
    return apiState;
  }

  private void updateApiKeysWithIds(List<ApiState> apiList) {
    Map<String, Integer> apiIdsMap = apiList.stream()
        .collect(Collectors.toMap(ApiState::getApiKey, ApiState::getApiId));
    apiList.forEach(apiState -> {
      apiState.setOnSuccess(updateKeysFromMap(apiState.getOnSuccess(), apiIdsMap));
      apiState.setOnFailure(updateKeysFromMap(apiState.getOnFailure(), apiIdsMap));
    });
    apiStateRepo.saveAll(apiList);
  }

  private List<String> updateKeysFromMap(List<String> apiKeys, Map<String, Integer> apiIdsMap) {
    return apiKeys.stream()
        .map(apiKey -> String.valueOf(apiIdsMap.get(apiKey)))
        .collect(Collectors.toList());
  }

  // ------------ Workflow Start & Resume Flows ---------------------


  @Override
  public void startWorkflow(Integer workFlowId) {
    Optional<WorkflowState> workFlowSearch = workflowRepo.findById(workFlowId);
    if (workFlowSearch.isPresent()) {
      WorkflowState workflow = workFlowSearch.get();
      checkStatusAndExecuteWorkflow(workflow, StatusConstants.INITIALIZED);
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + WF_NOT_PRESENT_ERR_MSG));
  }

  @Override
  public void resumeWorkflow(Integer workFlowId) {
    Optional<WorkflowState> workFlowSearch = workflowRepo.findById(workFlowId);
    if (workFlowSearch.isPresent()) {
      WorkflowState workflow = workFlowSearch.get();
      checkStatusAndExecuteWorkflow(workflow, StatusConstants.IN_PROGRESS);
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + WF_NOT_PRESENT_ERR_MSG));
  }

  private void checkStatusAndExecuteWorkflow(WorkflowState workflow, String status) {
    String currentStatus = workflow.getStatus();
    if (currentStatus.equals(status)) {
      tryAndExecuteWorkflow(workflow);
    } else {
      throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
          Error.WORKFLOW.getErrorMsg() + "Workflow not in expected " + status + " status"));
    }
  }

  private void tryAndExecuteWorkflow(WorkflowState workflow) {
    workflow.setStatus(StatusConstants.IN_PROGRESS);
    workflow.setLastModifiedTm(LocalDateTime.now());
    workflowRepo.save(workflow);
    try {
      workFlowRunnerImpl.executeWorkflow(workflow);
    } catch (Exception e) {
      workflow.setStatus(StatusConstants.FAILURE);
      workflowRepo.save(workflow);
      LOGGER.error("Exception executing Workflow ID: {} :: {}", workflow.getWfId(), e.getMessage());
    }
  }

  // ----------- Update Workflow RunConfigs -------

  @Override
  public void updateWorkFlowRunConfigs(Integer workFlowId, Map<String, String> runConfigs) {
    Optional<WorkflowState> workFlowOptional = workflowRepo.findById(workFlowId);
    if (workFlowOptional.isPresent()) {
      WorkflowState workflow = workFlowOptional.get();
      Map<String, Object> mapper = createMapper(runConfigs);
      workflow.setRunConfigMapper(mapper);
      workflowRepo.save(workflow);
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + WF_NOT_PRESENT_ERR_MSG));
  }

  // ---------- Get Workflow Details ----------

  @Override
  public WorkFlowResponse getWorkFlowBasicDetails(Integer workFlowId) {
    Optional<WorkflowState> workFlow = workflowRepo.findById(workFlowId);
    if (workFlow.isPresent()) {
      WorkflowState workflowState = workFlow.get();
      return WorkFlowResponse.getBasicDetailsFromEntity(workflowState);
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + WF_NOT_PRESENT_ERR_MSG));
  }

  @Override
  public WorkFlowResponse getWorkflowFullDetails(Integer workFlowId) {
    Optional<WorkflowState> workFlowOptional = workflowRepo.findById(workFlowId);
    if (workFlowOptional.isPresent()) {
      WorkflowState workflow = workFlowOptional.get();
      return WorkFlowResponse.getFullDetailsFromEntity(workflow);
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + WF_NOT_PRESENT_ERR_MSG));
  }

  // --------- Helper Methods Start Here ----------

  private Map<String, Object> createMapper(Map<String, String> runConfig) {
    Map<String, Object> mapper = new HashMap<String, Object>();
    for (Map.Entry<String, String> entry : runConfig.entrySet()) {
      mapper.put(entry.getKey(), entry.getValue());
    }
    return mapper;
  }

}
