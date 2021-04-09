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
@Table(name = "runtime_variables")
@Getter
@Setter
public class RuntimeVariables {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private Integer id;

  @Column(name = "wf_id")
  private Integer wfId;

  @Column(name = "key")
  private String key;

  @Column(name = "value")
  private String value;

  @Column(name = "last_modified_tm")
  private LocalDateTime lastModifiedTm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wf_id", updatable = false, referencedColumnName = "wf_id", insertable = false)
  @JsonIgnoreProperties(value = {"runtimeVariablesList"})
  private WorkflowState workflow;

}
