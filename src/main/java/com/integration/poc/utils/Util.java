package com.integration.poc.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IMapBuilder;

public class Util {

  private static final String URL_PATTERN = "\\{(.*?)\\}";

  private Util() {

  }

  public static boolean checkRunTimeParameter(String name) {
    return Pattern.matches(URL_PATTERN, name);
  }

  public static List<String> getMatchedValues(String name) {
    return Arrays.asList(StringUtils.substringsBetween(name, "{", "}"));
  }

  public static void replaceParamsAtRuntime(IMapBuilder mapBuilder,
      List<NameValuePair<String, String>> params) {
    params.forEach(param -> {
      boolean isRunTimeValue = Util.checkRunTimeParameter(param.getValue());
      if (isRunTimeValue) {
        String value = getMatchedValues(param.getValue()).get(0);
        int firstDotIndex = value.indexOf(".");
        String apiKey = value.substring(0, firstDotIndex);
        String id = value.substring(firstDotIndex + 1);
        param.setValue((String) mapBuilder.getMap(apiKey, id));
      }
    });
  }
}
