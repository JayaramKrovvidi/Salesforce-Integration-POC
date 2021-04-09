package com.integration.poc.converter;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MapToStringConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final String SEPERATOR = "|";
    private static final String SEPERATOR_PATTERN = "\\|";

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return attribute.keySet().stream().map(key -> key + "=" + attribute.get(key).toString())
                .collect(Collectors.joining(SEPERATOR));
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        String[] mapper = dbData.split(SEPERATOR_PATTERN);
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < mapper.length; i++) {
            String[] split = mapper[i].split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }
}
