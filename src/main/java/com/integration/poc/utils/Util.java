package com.integration.poc.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
     
        String paramValue=param.getValue();
        Pattern p = Pattern.compile(URL_PATTERN);
        Matcher ans = p.matcher(paramValue);
        while (ans.find()) {
          String group = ans.group(1);
          int firstDot = group.indexOf(".");
          String key1 = group.substring(0, firstDot);
          String key2 = group.substring(firstDot + 1);

          String res = mapBuilder.getMap(key1, key2)
              .toString();
          paramValue = paramValue.replace("{" + group + "}", res);
          param.setValue(paramValue);
        }
//        String value = getMatchedValues(param.getValue()).get(0);
//        int firstDotIndex = value.indexOf(".");
//        String apiKey = value.substring(0, firstDotIndex);
//        String id = value.substring(firstDotIndex + 1);
//        System.out.println(mapBuilder.getMap(apiKey, id));
       
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
