package com.integration.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.models.OrgJsonStore;
//import com.integration.poc.services.ICompositeApiRunner;
import com.integration.poc.services.IOrgJsonStoreService;

@RestController
@RequestMapping("request")
public class CompositeApiController {

//  @Autowired
//  ICompositeApiRunner compositeApiRunner;
  
  @Autowired
  IOrgJsonStoreService orgJsonStoreServiceImpl;

//  @PostMapping("/composite")
//  public void compositeApiTest(@RequestBody CompositeApiRequest runnerConfig) {
//    compositeApiRunner.run(runnerConfig);
//  }
  
  @PostMapping("/orgId/{orgId}/jsonKey/{json_key}")
  public Integer storeCompositeJson(@RequestBody CompositeApiRequest compositeApiRequest
      , @PathVariable(name = "json_key") String entityName, @PathVariable(name = "orgId") Integer orgId) {
      return orgJsonStoreServiceImpl.storeCompositeApi(compositeApiRequest,entityName,orgId);
  }
  
  @GetMapping("/orgId/{id}")
  public OrgJsonStore getByEntityId(@PathVariable(name = "id")Integer id) {
      return orgJsonStoreServiceImpl.getByEntityId(id);
  }
  
  @PutMapping("/orgId/{id}/update")
  public Integer updateJsonStore(@RequestBody CompositeApiRequest compositeApi ,@PathVariable(name = "id")Integer id) {
    return orgJsonStoreServiceImpl.updateJsonStore(compositeApi, id);
  }

  @PutMapping("/orgId/{orgid}/jsonKey/{json_key}/update")
  public Integer updateByJsonKey(@RequestBody CompositeApiRequest compositeApi ,@PathVariable(name = "orgid")Integer id
      ,@PathVariable(name = "json_key")String jsonKey) {
    return orgJsonStoreServiceImpl.updateByJsonKey(compositeApi, id,jsonKey);
  }

}
