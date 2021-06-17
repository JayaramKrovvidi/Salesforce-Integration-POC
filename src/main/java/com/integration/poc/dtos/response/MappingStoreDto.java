package com.integration.poc.dtos.response;

import com.integration.poc.dtos.internal.PostProcessConfig;
import com.integration.poc.models.MappingStore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MappingStoreDto {
    private Integer mappingId;
    private Integer jsonId;
    private PostProcessConfig mappingJson;
    private LocalDateTime lastModifiedTm;

    public MappingStoreDto(MappingStore mappingStore) {
        setJsonId(mappingStore.getJsonId());
        setMappingId(mappingStore.getMappingId());
        setMappingJson(mappingStore.getMappingJson());
        setLastModifiedTm(mappingStore.getLastModifiedTm());
    }

    public static MappingStore createNew(MappingStoreDto dto) {
        MappingStore mappingStore = new MappingStore();
        mappingStore.setJsonId(dto.getJsonId());
        mappingStore.setMappingId(dto.getMappingId());
        mappingStore.setMappingJson(dto.getMappingJson());
        mappingStore.setLastModifiedTm(LocalDateTime.now());
        return mappingStore;
    }

}
