package com.integration.poc.controllers;

import com.integration.poc.dtos.response.JsonStoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.services.IOrgJsonStoreService;

@RestController
@RequestMapping("composite-api/json-store")
public class CompositeApiController {

  @Autowired
  IOrgJsonStoreService orgJsonStoreServiceImpl;

  @PostMapping("/orgId/{orgId}/jsonKey/{json_key}")
  public Integer storeCompositeJson(@RequestBody CompositeApiRequest compositeApiRequest,
      @PathVariable(name = "json_key") String entityName,
      @PathVariable(name = "orgId") Integer orgId) {
    return orgJsonStoreServiceImpl.storeCompositeApi(compositeApiRequest, entityName, orgId);
  }

  @GetMapping("/id/{id}")
  public JsonStoreDto getByEntityId(@PathVariable(name = "id") Integer id) {
    return orgJsonStoreServiceImpl.getByEntityId(id);
  }

  @PutMapping("/id/{id}/update")
  public Integer updateJsonStore(@RequestBody CompositeApiRequest compositeApi,
      @PathVariable(name = "id") Integer id) {
    return orgJsonStoreServiceImpl.updateJsonStore(compositeApi, id);
  }

  @PutMapping("/orgId/{orgId}/jsonKey/{json_key}/update")
  public Integer updateByJsonKey(@RequestBody CompositeApiRequest compositeApi,
      @PathVariable(name = "orgId") Integer orgId, @PathVariable(name = "json_key") String jsonKey) {
    return orgJsonStoreServiceImpl.updateByJsonKey(compositeApi, orgId, jsonKey);
  }

}
