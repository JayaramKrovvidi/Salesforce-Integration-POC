package com.integration.poc.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Check;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.models.Organization;
import com.integration.poc.repositories.IOrgRepository;
import com.integration.poc.services.impl.JSONMediatorImpl;

@RestController
@RequestMapping("org")
public class OrgController {

  @Autowired
  IOrgRepository repo;
  
  @Autowired
  JSONMediatorImpl json;

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

  @GetMapping("/check")
  public void check () {
    List<String> keyList = new ArrayList<>();
    List<String> valueList = new ArrayList<>();
    keyList.add("address.city");
    keyList.add("address.state");
valueList.add("vpm");
valueList.add("tamilnadu");
    JSONObject jsonObjBuilder = json.jsonObjBuilder(valueList, keyList);
    System.out.println(jsonObjBuilder);
    
  }
}
