package com.integration.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.dtos.response.OrganizationDto;
import com.integration.poc.services.IOrgService;

@RestController
@RequestMapping("org")
public class OrgController {

  @Autowired
  IOrgService orgService;


  @PostMapping("/add")
  public OrganizationDto addNew(@RequestBody OrganizationDto orgDto) {
    return orgService.save(orgDto);
  }

  @GetMapping("/get/{orgId}")
  public OrganizationDto getOrg(@PathVariable(name = "orgId") Integer orgId) {
    return orgService.findByOrgId(orgId);
  }

}
