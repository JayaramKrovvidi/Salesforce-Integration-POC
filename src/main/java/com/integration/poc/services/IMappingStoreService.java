package com.integration.poc.services;

import com.integration.poc.dtos.response.MappingStoreDto;

public interface IMappingStoreService {
    public MappingStoreDto addMappingsToExistingJson(MappingStoreDto mappings);

    public MappingStoreDto getMappingStoreById(Integer mappingId);
}
