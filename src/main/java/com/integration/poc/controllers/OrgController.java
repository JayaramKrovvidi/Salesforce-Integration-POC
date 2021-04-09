package com.integration.poc.controllers;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.models.Organization;
import com.integration.poc.repositories.IOrgRepository;

@RestController
@RequestMapping("org")
public class OrgController {

  @Autowired
  IOrgRepository repo;

  @PostMapping("/add")
  public Organization addNew(@RequestBody Organization org) {
    org.setLastModifiedTm(LocalDateTime.now());
    return repo.save(org);
  }

  @GetMapping("/get/{orgId}")
  public Organization getOrg(@PathVariable(name = "orgId") Integer orgId) {
    return repo.findById(orgId)
        .get();
  }

}
