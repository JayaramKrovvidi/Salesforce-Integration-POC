package com.integration.poc.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.response.OrganizationDto;
import com.integration.poc.enums.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.models.Organization;
import com.integration.poc.repositories.IOrgRepository;
import com.integration.poc.services.IOrgService;

@Service
public class OrgServiceImpl implements IOrgService {

  @Autowired
  IOrgRepository orgRepo;

  @Override
  public OrganizationDto save(OrganizationDto orgDto) {
    Organization org = OrganizationDto.createNew(orgDto);
    org.setLastModifiedTm(LocalDateTime.now());
    return new OrganizationDto(orgRepo.save(org));
  }

  @Override
  public OrganizationDto findByOrgId(Integer orgId) {
    Optional<Organization> orgSearch = orgRepo.findById(orgId);
    if (orgSearch.isPresent()) {
      return new OrganizationDto(orgSearch.get());
    }
    throw new GenericException(new GenericError(Error.WORKFLOW.getErrorCode(),
        Error.WORKFLOW.getErrorMsg() + "Couldn't find Organization with Id: " + orgId));
  }

}
