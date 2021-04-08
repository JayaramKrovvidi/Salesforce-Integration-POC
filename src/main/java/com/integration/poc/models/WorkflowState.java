package com.integration.poc.models;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WORKFLOW_STATE")
@Getter
@Setter
public class WorkflowState {

  @Id
  @Column(name = "wf_id")
  @GeneratedValue
  private Integer wfId;

  @Column(name = "json_id")
  private Integer jsonId;

  @Column(name = "run_config_json")
  private String runConfigJson;

  @Column(name = "status")
  private String status;

  @Column(name = "detail_msg_txt")
  private String detailMsgTxt;

  @Column(name = "current_api_id")
  private Integer currentApiId;

  @Column(name = "last_modified_tm")
  private LocalDateTime lastModifiedTm;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflow")
  @JsonIgnoreProperties(value = {"workflow"})
  private List<ApiState> apiList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflow")
  @JsonIgnoreProperties(value = {"workflow"})
  private List<RuntimeVariables> runtimeVariablesList;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "json_id", updatable = false, referencedColumnName = "id", insertable = false)
  @JsonIgnoreProperties(value = {"workflowList"})
  private OrgJsonStore jsonStore;

}
