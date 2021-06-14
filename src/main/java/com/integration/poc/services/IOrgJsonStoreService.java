package com.integration.poc.services;

import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.models.OrgJsonStore;

public interface IOrgJsonStoreService {

  public Integer storeCompositeApi(CompositeApiRequest compositeApi, String entityName,
      Integer orgId);

  public OrgJsonStore getByEntityId(Integer id);

  public Integer updateJsonStore(CompositeApiRequest compositeApi, Integer orgId);

  public Integer updateByJsonKey(CompositeApiRequest compositeApi, Integer orgId, String jsonKey);

}
