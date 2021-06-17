package com.integration.poc.services.impl;

import com.integration.poc.dtos.response.MappingStoreDto;
import com.integration.poc.models.MappingStore;
import com.integration.poc.repositories.IMappingStoreRepository;
import com.integration.poc.services.IMappingStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MappingStoreServiceImpl implements IMappingStoreService {

    @Autowired
    IMappingStoreRepository mappingStoreRepository;

    /**
     * Save Given Mapping Config into DB by converting an external DTO into Entity Object, saving it and converting back to Dto
     *
     * @param mappings
     * @return
     */
    @Override
    @Transactional
    public MappingStoreDto addMappingsToExistingJson(MappingStoreDto mappings) {
        MappingStore mappingStore = MappingStoreDto.createNew(mappings);
        mappingStoreRepository.save(mappingStore);
        return new MappingStoreDto(mappingStore);
    }

    @Override
    public MappingStoreDto getMappingStoreById(Integer mappingId) {
        Optional<MappingStore> mappingStoreSearch = mappingStoreRepository.findById(mappingId);
        return mappingStoreSearch.map(MappingStoreDto::new).orElse(null);
    }
}
