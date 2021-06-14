package com.integration.poc.dtos.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.integration.poc.models.ApiState;
import com.integration.poc.models.OrgJsonStore;
import com.integration.poc.models.RuntimeVariables;
import com.integration.poc.models.WorkflowState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkFlowResponse {

  private Integer wfId;

  private Integer jsonId;

  private Map<String, Object> runConfigMapper;

  private String status;

  private String detailMsgTxt;

  private Integer currentApiId;

  private LocalDateTime lastModifiedTm;

  private List<ApiState> apiList;

  private List<RuntimeVariables> runtimeVariablesList;

  private OrgJsonStore jsonStore;

  public static WorkFlowResponse getBasicDetailsFromEntity(WorkflowState workflow) {
    WorkFlowResponse workFlowResponse = new WorkFlowResponse();
    workFlowResponse.setWfId(workflow.getWfId());
    workFlowResponse.setJsonId(workflow.getJsonId());
    workFlowResponse.setRunConfigMapper(workflow.getRunConfigMapper());
    workFlowResponse.setStatus(workflow.getStatus());
    workFlowResponse.setDetailMsgTxt(workflow.getDetailMsgTxt());
    workFlowResponse.setCurrentApiId(workflow.getCurrentApiId());
    workFlowResponse.setLastModifiedTm(workflow.getLastModifiedTm());
    return workFlowResponse;
  }

  public static WorkFlowResponse getFullDetailsFromEntity(WorkflowState workflow) {
    WorkFlowResponse workFlowResponse = getBasicDetailsFromEntity(workflow);
    workFlowResponse.setCurrentApiId(workflow.getCurrentApiId());
    workFlowResponse.setApiList(workflow.getApiList());
    workFlowResponse.setRuntimeVariablesList(workflow.getRuntimeVariablesList());
    workFlowResponse.setJsonStore(workflow.getJsonStore());
    return workFlowResponse;
  }

}
