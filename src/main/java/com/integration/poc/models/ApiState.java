package com.integration.poc.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "api_state")
@Getter
@Setter
public class ApiState {

  @Id
  @Column(name = "api_id")
  @GeneratedValue
  private Integer apiId;

  @Column(name = "wf_id")
  private Integer wfId;

  @Column(name = "api_key")
  private String apiKey;

  @Column(name = "request_config")
  private Integer requestConfig;

  @Column(name = "status")
  private String status;

  @Column(name = "detail_msg_txt")
  private Integer detailMsgTxt;

  @Column(name = "retry")
  private String retry;

  @Column(name = "on_success")
  private String onSuccess;

  @Column(name = "on_failure")
  private Integer onFailure;

  @Column(name = "last_modified_tm")
  private LocalDateTime lastModifiedTm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wf_id", updatable = false, referencedColumnName = "wf_id", insertable = false)
  @JsonIgnoreProperties(value = {"apiList"})
  private WorkflowState workflow;

}
