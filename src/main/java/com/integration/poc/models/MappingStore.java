package com.integration.poc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.integration.poc.converters.PostProcessConfigConverter;
import com.integration.poc.dtos.internal.PostProcessConfig;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MAPPING_STORE")
@Getter
@Setter
public class MappingStore {

    @Id
    @Column(name = "mapping_id")
    @GeneratedValue
    private Integer mappingId;

    @Column(name = "json_id")
    private Integer jsonId;

    @Column(name = "mapping_json")
    @Convert(converter = PostProcessConfigConverter.class)
    private PostProcessConfig mappingJson;

    @Column(name = "last_modified_tm")
    private LocalDateTime lastModifiedTm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "json_id", updatable = false, referencedColumnName = "id", insertable = false)
    @JsonIgnoreProperties(value = {"mappingList"})
    private OrgJsonStore jsonStore;
}
