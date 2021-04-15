package com.integration.poc.services.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import com.integration.poc.models.RuntimeVariables;
import com.integration.poc.repositories.IRuntimeVariablesRepository;
import com.integration.poc.services.IMapBuilder;


@Service
@RequestScope
public class MapBuilderImpl implements IMapBuilder {
  
  @Autowired
  IRuntimeVariablesRepository runTimeRepo;

//  Map<String, Map<String, Object>> mapBuilder = new HashMap<>();

  @Override
  public void putValue(Integer wfId,String apiKey, String id, Object obj) {
    RuntimeVariables runtimeVariables=new RuntimeVariables();
    runtimeVariables.setWfId(wfId);
    runtimeVariables.setValue(obj);
    runtimeVariables.setKey(apiKey+"_"+id);
     runTimeRepo.save(runtimeVariables);
  }

  @Override
  public Object getValue(Integer wfId,String apiKey, String id) {
            
        return runTimeRepo.findByWfIdAndKey(wfId, apiKey+"_"+id);
        
  }

  
}
