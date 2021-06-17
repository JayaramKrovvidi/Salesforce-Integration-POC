package com.integration.poc.controllers;

import com.integration.poc.dtos.response.MappingStoreDto;
import com.integration.poc.services.IMappingStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mapping-store")
public class MappingStoreController {

    @Autowired
    IMappingStoreService mappingStoreService;

    @PostMapping("/add-new")
    public MappingStoreDto addMappingToExistingJson(@RequestBody MappingStoreDto mappingDto) {
        return mappingStoreService.addMappingsToExistingJson(mappingDto);
    }

    @GetMapping("get/mappingId/{mappingId}")
    public MappingStoreDto getMappingStoreById(@PathVariable(name = "mappingId") Integer mappingId) {
        return mappingStoreService.getMappingStoreById(mappingId);
    }
}
