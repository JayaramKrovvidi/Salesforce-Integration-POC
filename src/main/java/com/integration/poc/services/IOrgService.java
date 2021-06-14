package com.integration.poc.services;

import com.integration.poc.dtos.response.OrganizationDto;

public interface IOrgService {

  OrganizationDto save(OrganizationDto orgDto);

  OrganizationDto findByOrgId(Integer orgId);

}
