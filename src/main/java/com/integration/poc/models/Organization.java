package com.integration.poc.models;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Getter
@Setter
public class Organization {

  @Id
  @Column(name = "org_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer orgId;

  @Column(name = "org_nm")
  private String orgNm;


  // changes
  @Column(name = "integration_typ_nm")
  private String integrationTypNm;

  @Column(name = "last_modified_tm")
  private LocalDateTime lastModifiedTm;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
  @JsonIgnoreProperties(value = {"organization"})
  private List<OrgJsonStore> jsonStoreList;

}
