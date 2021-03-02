package com.integration.poc.services.impl;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ConvConfig;
import com.integration.poc.dtos.internal.PostProcessConfig;

@Service
public class FileManagerService {

  public PostProcessConfig getConfigFromResource(String path) {
    try {
      JSONParser jsonParser = new JSONParser();
      FileReader reader = new FileReader(path);
      JSONObject configJson = (JSONObject) jsonParser.parse(reader);
      JSONArray mappersJson = (JSONArray) configJson.get("mappers");
      return convertToPostProcessConfig(mappersJson);
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private PostProcessConfig convertToPostProcessConfig(JSONArray mappersJson) {
    List<ConvConfig> mappers = new ArrayList<>();
    for (Object json : mappersJson) {
      JSONObject mapper = (JSONObject) json;
      mappers.add(new ConvConfig((String) mapper.get("sourceId"), (String) mapper.get("destId")));
    }
    return new PostProcessConfig(mappers);
  }

}
