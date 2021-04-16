package com.integration.poc.services.impl;

import java.util.Optional;
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

  @Override
  public void putValue(Integer wfId, String apiKey, String id, Object obj) {
    RuntimeVariables runtimeVariables = new RuntimeVariables();
    runtimeVariables.setWfId(wfId);
    runtimeVariables.setValue(obj);
    runtimeVariables.setKey(apiKey + "_" + id);
    runTimeRepo.save(runtimeVariables);
  }

  @Override
  public Object getValue(Integer wfId, String apiKey, String id) {

    Optional<RuntimeVariables> runtimeOptional =
        runTimeRepo.findByWfIdAndKey(wfId, apiKey + "_" + id);
    if (runtimeOptional.isPresent()) {
      return runtimeOptional.get()
          .getValue();
    }
    return null;
  }


}
