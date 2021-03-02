package com.integration.poc.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.NameValuePair;
import com.integration.poc.dtos.internal.ObjectMapper;
import com.integration.poc.services.IObjectMapperRefactor;

@Service
public class ObjectMapperRefactorImpl implements IObjectMapperRefactor {

  private static final String REGEX = "^[\"']+|[\"']+$";

  @Override
  public List<String> run(String[] rows, ObjectMapper mapper) {
    List<String> header = buildHeader(rows);
    List<String> newHeaders = buildNewHeaders(mapper);
    Map<String, String> correspOldHeaderMap = createOldHeaderMapping(mapper);
    List<Integer> newIndices = getNewIndices(header, newHeaders, correspOldHeaderMap);

    String finalCSVHeader = newHeaders.stream()
        .collect(Collectors.joining(","));
    List<String> fileContent = new ArrayList<>();
    fileContent.add(finalCSVHeader);
    for (int i = 1; i < rows.length; i++) {
    	fileContent.add(processRow(rows[i], newIndices));
    }
    return fileContent;
  }

  public List<String> buildHeader(String[] rows) {
    List<String> header = new ArrayList<>();
    String[] rowcontent = rows[0].split(",");
    for (int i = 0; i < rowcontent.length; i++) {
      header.add(rowcontent[i].replaceAll(REGEX, ""));
    }
    return header;
  }

  private Map<String, String> createOldHeaderMapping(ObjectMapper mapper) {
    List<NameValuePair<String, String>> nameValMapping = mapper.getMappers();
    return nameValMapping.stream()
        .collect(Collectors.toMap(NameValuePair::getValue, NameValuePair::getName));
  }

  private List<String> buildNewHeaders(ObjectMapper mapper) {
    List<NameValuePair<String, String>> nmValMapping = mapper.getMappers();
    return nmValMapping.stream()
        .map(NameValuePair::getValue)
        .collect(Collectors.toList());
  }

  private List<Integer> getNewIndices(List<String> headerList, List<String> newHeaderList,
      Map<String, String> headerMappings) {
    List<Integer> newIndices = new ArrayList<>();
    for (String newHeader : newHeaderList) {
      String correspOldHeader = headerMappings.get(newHeader);
      if (headerList.contains(correspOldHeader)) {
        newIndices.add(headerList.indexOf(correspOldHeader));
      } else {
        newIndices.add(-1);
      }
    }
    return newIndices;
  }

  private String processRow(String row, List<Integer> indices) {
    List<String> oldRowValues = Arrays.asList(row.split(","))
        .stream()
        .map(value -> value.replaceAll(REGEX, ""))
        .collect(Collectors.toList());

    List<String> newRowValues = new ArrayList<>();
    for (Integer index : indices) {
      if (index.equals(-1)) {
        newRowValues.add("");
      } else {
        newRowValues.add(oldRowValues.get(index));
      }
    }
    return newRowValues.stream()
        .collect(Collectors.joining(","));
  }
}
