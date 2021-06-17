package com.integration.poc.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.poc.dtos.internal.ConvConfig;
import com.integration.poc.dtos.internal.PostProcessConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class PostProcessConfigConverter implements AttributeConverter<PostProcessConfig, String> {

    @Override
    public String convertToDatabaseColumn(PostProcessConfig attribute) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mappers", attribute.getMappers());
        return jsonObject.toString();
    }

    @Override
    public PostProcessConfig convertToEntityAttribute(String dbData) {
        PostProcessConfig config = new PostProcessConfig();
        JSONObject jsonObject = new JSONObject(dbData);
        JSONArray object = (JSONArray) jsonObject.get("mappers");
        config.setMappers(parseMappers(object));
        return config;
    }

    private List<ConvConfig> parseMappers(JSONArray mappersList) {
        List<ConvConfig> mappers = new ArrayList<>();
        for (int i = 0; i < mappersList.length(); i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ConvConfig mapper = objectMapper.readValue(mappersList.get(i)
                        .toString(), ConvConfig.class);
                mappers.add(mapper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mappers;
    }
}
