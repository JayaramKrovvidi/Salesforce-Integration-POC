package com.integration.poc.repositories;

import java.util.Optional;
import com.integration.poc.models.OrgJsonStore;

public interface IOrgJsonStoreRepository extends ICustomJPARepository<OrgJsonStore, Integer> {

  public Optional<OrgJsonStore> findByOrgIdAndJsonKey(Integer orgId, String jsonKey);

}
