package com.integration.poc.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.ApiRequestConfig;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.services.IMapBuilder;

@Service
public class UrlBuilderUtil {

  @Autowired
  IMapBuilder mapBuilder;

  private UrlBuilderUtil() {}

  private static final String EMPTY_STRING = "";
  private static final String URL_PATTERN = "\\{(.*?)\\}";

  public String buildUrl(ApiRequestConfig apiConfig) {
    StringBuilder urlBuilder = new StringBuilder(buildPathParams(apiConfig.getUrl()));
    urlBuilder.append(addRequestParams(apiConfig.getRequestParams()));
    return urlBuilder.toString();
  }

  private String buildPathParams(String url) {
  Integer wfId=null;
    Pattern p = Pattern.compile(URL_PATTERN);
    Matcher ans = p.matcher(url);
    while (ans.find()) {
      String group = ans.group(1);
      int firstDot = group.indexOf(".");
      String key1 = group.substring(0, firstDot);
      String key2 = group.substring(firstDot + 1);

      String res = mapBuilder.getValue(wfId,key1, key2)
          .toString();
      url = url.replace("{" + group + "}", res);
    }
    return url;
  }

  private String addRequestParams(List<NameValuePair<String, String>> requestParams) {
    if (CollectionUtils.isEmpty(requestParams)) {
      return EMPTY_STRING;
    }
    return "?" + requestParams.stream()
        .map(pair -> pair.getName() + "=" + pair.getValue())
        .collect(Collectors.joining("&"));
  }
}
