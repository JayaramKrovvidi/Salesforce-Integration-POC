package com.integration.poc.models;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "org_json_store")
@Getter
@Setter
public class OrgJsonStore {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "org_id")
  private Integer orgId;

  @Column(name = "json_key")
  private String jsonKey;

  @Column(name = "json_string")
  private String jsonString;

  @Column(name = "last_modified_tm")
  private LocalDateTime lastModifiedTm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "org_id", updatable = false, referencedColumnName = "org_id",
      insertable = false)
  @JsonIgnoreProperties(value = {"jsonStoreList"})
  private Organization organization;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "jsonStore")
  @JsonIgnoreProperties(value = {"jsonStore"})
  private List<WorkflowState> workflowList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "jsonStore")
  @JsonIgnoreProperties(value = {"jsonStore"})
  private List<MappingStore> mappingList;

}
