package com.integration.poc.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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
import com.integration.poc.services.IWorkflowService;

@Service
public class WorkflowServiceImpl implements IWorkflowService {

  @Autowired
  IOrgJsonStoreRepository jsonStoreRepo;

  @Autowired
  IWorkflowRepository workflowRepo;

  @Autowired
  IApiStateRepository apiStateRepo;

  @Override
  @Transactional
  public Integer initializeWorkflow(Integer orgId, String jsonKey) {
    Optional<OrgJsonStore> jsonStoreSearch = jsonStoreRepo.findByOrgIdAndJsonKey(orgId, jsonKey);
    if (jsonStoreSearch.isPresent()) {
      OrgJsonStore jsonStore = jsonStoreSearch.get();
      CompositeApiRequest compositeApi = getCompositeJson(jsonStore.getJsonString());
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
    apiState.setRequestConfig(apiRequest.getApiRequest()
        .toString());
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

  private CompositeApiRequest getCompositeJson(String compositeJsonString) {
    return new CompositeApiRequest();
  }

  private Map<String, Object> createMapper(Map<String, String> runConfig) {
    Map<String, Object> mapper = new HashMap<String, Object>();
    for (Map.Entry<String, String> entry : runConfig.entrySet()) {
      mapper.put(entry.getKey(), entry.getValue());
    }
    return mapper;
  }


  @Override
  public void startWorkflow(Integer workFlowId, Map<String, String> runConfigs) {
    Map<String, Object> runConfigsMapper = createMapper(runConfigs);
    Optional<WorkflowState> workFlow = workflowRepo.findById(workFlowId);
    // if check for status
    if (workFlow.isPresent()) {
      WorkflowState workflowState = workFlow.get();
      workflowState.setRunConfigMapper(runConfigsMapper);

      workflowRepo.save(workflowState);

    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + "Requested workflow not found"));
  }

  @Override
  public WorkFlowResponse getWorkFlow(Integer workFlowId) {
    Optional<WorkflowState> workFlow = workflowRepo.findById(workFlowId);
    if (workFlow.isPresent()) {
      WorkFlowResponse workFlowResponse = new WorkFlowResponse();
      WorkflowState workflowState = workFlow.get();
      workFlowResponse.setWfId(workflowState.getWfId());
      workFlowResponse.setJsonId(workflowState.getJsonId());
      workFlowResponse.setRunConfigMapper(workflowState.getRunConfigMapper());
      workFlowResponse.setStatus(workflowState.getStatus());
      workFlowResponse.setDetailMsgTxt(workflowState.getDetailMsgTxt());
      workFlowResponse.setCurrentApiId(workflowState.getCurrentApiId());
      workFlowResponse.setLastModifiedTm(workflowState.getLastModifiedTm());
      return workFlowResponse;
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + "Requested workflow not found"));
  }

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
        Error.WORKFLOW.getErrorMsg() + "Requested workflow not found"));
  }
}
