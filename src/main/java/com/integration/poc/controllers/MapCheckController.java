package com.integration.poc.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integration.poc.models.WorkflowState;
import com.integration.poc.repositories.IApiStateRepository;
import com.integration.poc.repositories.IOrgJsonStoreRepository;
import com.integration.poc.repositories.IOrgRepository;
import com.integration.poc.repositories.IWorkflowStateRepository;

@RestController
@RequestMapping("runconfig")
public class MapCheckController {

  @Autowired
  IWorkflowStateRepository workFlowrepo;

  @Autowired
  IOrgJsonStoreRepository orgJsonRepo;

  @Autowired
  IApiStateRepository apiRepo;

  @Autowired
  IOrgRepository orgRpo;

  @PutMapping("/update/{id}")
  public void updateConfig(@RequestBody Map<String, String> runConfig,
      @PathVariable(name = "id") Integer id) {
    WorkflowState workflowState = workFlowrepo.findById(id)
        .get();
    workflowState.setRunConfigMapper(createMapper(runConfig));
    workFlowrepo.save(workflowState);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteConfig(@PathVariable(name = "id") Integer id) {
    workFlowrepo.deleteById(id);
  }


  @GetMapping("/get/{id}")
  public WorkflowState getConfig(@PathVariable(name = "id") Integer id) {
    System.out.println(id);
    WorkflowState workflowState = workFlowrepo.findById(id)
        .get();
    System.out.println(workflowState);
    return workflowState;
  }

  @PostMapping("/store")
  public Integer storeRunConfig(@RequestBody Map<String, String> runConfig) {
    LocalDateTime localDateTime = LocalDateTime.now();
    // Organization organization = new Organization();
    // organization.setOrgNm("1");
    // organization.setIntegrationTypNm("1");
    // organization.setLastModifiedTm(localDateTime);
    // orgRpo.save(organization);

    // OrgJsonStore orgJsonStore = new OrgJsonStore();
    // orgJsonStore.setOrgId(1);
    // orgJsonStore.setJsonKey("1");
    // orgJsonStore.setJsonString("{ \r\n"
    // + " \"employee\": { \r\n"
    // + " \"name\": \"sonoo\", \r\n"
    // + " \"salary\": 56000, \r\n"
    // + " \"married\": true \r\n"
    // + " } \r\n"
    // + "} ");
    // orgJsonStore.setLastModifiedTm(localDateTime);
    // orgJsonRepo.save(orgJsonStore);

    // ApiState apiState = new ApiState();
    // apiState.setApiKey("1");
    // apiState.setDetailMsgTxt(1);
    // apiState.setLastModifiedTm(localDateTime);
    // apiState.setOnFailure(-1);
    // apiState.setOnSuccess("2");
    // apiState.setRequestConfig(1);
    // apiState.setRetry("5");
    // apiState.setStatus("pending");
    // apiState.setWfId(1);
    // apiRepo.save(apiState);

    WorkflowState workflowState = new WorkflowState();
    workflowState.setJsonId(3);
    workflowState.setStatus("starting");
    workflowState.setRunConfigMapper(createMapper(runConfig));
    
    return workFlowrepo.save(workflowState).getWfId();

  }

  private Map<String, Object> createMapper(Map<String, String> runConfig) {
    Map<String, Object> mapper = new HashMap<String, Object>();
    for (Map.Entry<String, String> entry : runConfig.entrySet()) {
      mapper.put(entry.getKey(), entry.getValue());
    }
    return mapper;
  }
}
