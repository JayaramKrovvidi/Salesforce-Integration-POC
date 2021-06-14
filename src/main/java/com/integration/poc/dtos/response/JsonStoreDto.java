package com.integration.poc.dtos.response;

import com.integration.poc.dtos.external.CompositeApiRequest;
import com.integration.poc.models.OrgJsonStore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JsonStoreDto {

    private Integer id;

    private Integer orgId;

    private String jsonKey;

    private CompositeApiRequest jsonString;

    private LocalDateTime lastModifiedTm;

    private JsonStoreDto() {}

    public JsonStoreDto(OrgJsonStore jsonStore) {
        super();
        this.id = jsonStore.getId();
        this.orgId = jsonStore.getOrgId();
        this.jsonKey = jsonStore.getJsonKey();
        this.jsonString = jsonStore.getJsonString();
        this.lastModifiedTm = jsonStore.getLastModifiedTm();
    }
}
