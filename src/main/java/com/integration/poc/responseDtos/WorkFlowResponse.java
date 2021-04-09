package com.integration.poc.responseDtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.integration.poc.models.ApiState;
import com.integration.poc.models.OrgJsonStore;
import com.integration.poc.models.RuntimeVariables;
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

}
