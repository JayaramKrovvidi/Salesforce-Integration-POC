package com.integration.poc.repositories;

import java.util.Optional;
import com.integration.poc.models.RuntimeVariables;

public interface IRuntimeVariablesRepository
    extends ICustomJPARepository<RuntimeVariables, Integer> {

  public Optional<RuntimeVariables> findByWfId(Integer wfId);

  public Optional<RuntimeVariables> findByWfIdAndKey(Integer wfId, String key);


}
