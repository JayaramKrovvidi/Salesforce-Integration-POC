package com.integration.poc.services.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.enums.Error;
import com.integration.poc.exceptions.GenericError;
import com.integration.poc.exceptions.GenericException;
import com.integration.poc.models.OrgJsonStore;
import com.integration.poc.repositories.IOrgJsonStoreRepository;
import com.integration.poc.services.IOrgJsonStoreService;

@Service
public class OrgJsonStoreServiceImpl implements IOrgJsonStoreService {
  
  @Autowired
  IOrgJsonStoreRepository jsonStoreRepo;

  @Override
  public Integer storeCompositeApi(CompositeApiRequest compositeApi, String jsonKey, Integer orgId) {

    OrgJsonStore orgJsonStore=new OrgJsonStore();
    orgJsonStore.setJsonKey(jsonKey);
    orgJsonStore.setOrgId(orgId);
    orgJsonStore.setJsonString(compositeApi);
    return jsonStoreRepo.save(orgJsonStore).getId();
  }
  public OrgJsonStore getByEntityId(Integer id) {
    Optional<OrgJsonStore> jsonStore = jsonStoreRepo.findById(id);
    return jsonStore.orElse(null);
  }
  
  public Integer updateJsonStore(CompositeApiRequest compositeApi, Integer id) {
    Optional<OrgJsonStore> orgJsonStore = jsonStoreRepo.findById(id);
   if (orgJsonStore.isPresent()) {
     OrgJsonStore orgJson=orgJsonStore.get();
     orgJson.setJsonString(compositeApi);
     return jsonStoreRepo.save(orgJson).getId();
   }
   throw new GenericException(new GenericError(Error.NO_DATA_FOUND.getErrorCode(),
       Error.NO_DATA_FOUND.getErrorMsg() + "Requested Json not found"));

  }
  
  public Integer updateByJsonKey(CompositeApiRequest compositeApi, Integer orgId, String jsonKey) {
    Optional<OrgJsonStore> jsonStoreSearch = jsonStoreRepo.findByOrgIdAndJsonKey(orgId, jsonKey);
    if(jsonStoreSearch.isPresent()) {
      OrgJsonStore jsonStore = jsonStoreSearch.get();
      jsonStore.setJsonString(compositeApi);
      return jsonStoreRepo.save(jsonStore).getId();
    }
    throw new GenericException(new GenericError(Error.NO_DATA_FOUND.getErrorCode(),
        Error.NO_DATA_FOUND.getErrorMsg() + "Requested Json not found"));

  }
  

}
