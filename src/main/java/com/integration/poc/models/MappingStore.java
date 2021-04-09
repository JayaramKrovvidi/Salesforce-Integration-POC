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
@Table(name = "MAPPING_STORE")
@Getter
@Setter
public class MappingStore {

  @Id
  @Column(name = "mapping_id")
  @GeneratedValue
  private Integer mappingId;

  @Column(name = "json_id")
  private Integer jsonId;

  @Column(name = "mapping_json")
  private String mappingJson;

  @Column(name = "last_modified_tm")
  private LocalDateTime lastModifiedTm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "json_id", updatable = false, referencedColumnName = "id", insertable = false)
  @JsonIgnoreProperties(value = {"mappingList"})
  private OrgJsonStore jsonStore;
}
