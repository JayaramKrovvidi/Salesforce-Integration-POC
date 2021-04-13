package com.integration.poc.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IMapBuilder;

public class Util {

  @Autowired
  static IMapBuilder mapBuilder;
  private static final String URL_PATTERN = "\\{(.*?)\\}";

  private Util() {

  }

  public static boolean checkRunTimeParameter(String name) {
    return Pattern.matches(URL_PATTERN, name);
  }

  public static List<String> getMatchedValues(String name) {
    return Arrays.asList(StringUtils.substringsBetween(name, "{", "}"));
  }

  public static void replaceParamsAtRuntime(Map<String,Object> map,
      List<NameValuePair<String, String>> params) {
    params.forEach(param -> {
      boolean isRunTimeValue = Util.checkRunTimeParameter(param.getValue());
      if (isRunTimeValue) {
        String value = getMatchedValues(param.getValue()).get(0);
        int firstDotIndex = value.indexOf(".");
        String apiKey = value.substring(0, firstDotIndex);
        String id = value.substring(firstDotIndex + 1);
        param.setValue((String) mapBuilder.getMap(map,apiKey, id));
      }
    });
  }

  public static <K, V> Map<K, V> createMap(List<NameValuePair<K, V>> pairs) {
    return pairs.stream()
        .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
  }

  public static String getDateEnding() {
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
    return today.format(formatter);
  }
}
