package com.integration.poc.dtos.response;

import java.time.LocalDateTime;
import com.integration.poc.models.Organization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDto {

  private Integer orgId;
  private String orgNm;
  private String integrationTypNm;
  private LocalDateTime lastModifiedTm;

  public OrganizationDto(Organization org) {
    super();
    this.orgId = org.getOrgId();
    this.orgNm = org.getOrgNm();
    this.integrationTypNm = org.getIntegrationTypNm();
    this.lastModifiedTm = org.getLastModifiedTm();
  }

  public static Organization createNew(OrganizationDto orgDto) {
    Organization org = new Organization();
    org.setOrgId(orgDto.getOrgId());
    org.setOrgNm(orgDto.getOrgNm());
    org.setIntegrationTypNm(orgDto.getIntegrationTypNm());
    org.setLastModifiedTm(orgDto.getLastModifiedTm());
    return org;
  }

}
