package com.integration.poc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.services.IMapBuilder;

@Service
public class RequestBodyBuilderUtil {
  
  @Autowired
  IMapBuilder mapBuilder;
  
  private static final String URL_PATTERN = "\\{\\{(.*?)\\}\\}";

  public String buildRequestody(ApiRequestConfig apiConfig) {
    String apiRequestBody=apiConfig.getRequestBody().toString();
    Pattern p = Pattern.compile(URL_PATTERN);
    Matcher ans = p.matcher(apiRequestBody);
    while (ans.find()) {
      String group = ans.group(1);
      int firstDot = group.indexOf(".");
      String key1 = group.substring(0, firstDot);
      String key2 = group.substring(firstDot + 1);

      String res = mapBuilder.getMap(key1, key2)
          .toString();
      apiRequestBody = apiRequestBody.replace("{{" + group + "}}", res);
    }
    return apiRequestBody;
  }

  

}
