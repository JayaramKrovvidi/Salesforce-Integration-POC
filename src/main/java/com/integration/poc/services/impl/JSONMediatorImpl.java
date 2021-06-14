package com.integration.poc.services.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ConvConfig;
import com.integration.poc.dtos.internal.Node;
import com.integration.poc.dtos.internal.PostProcessConfig;
import com.integration.poc.services.IMediator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
public class JSONMediatorImpl implements IMediator {

  private static final Logger LOGGER = LogManager.getLogger(JSONMediatorImpl.class);

  private static final String ROOT_NODE_NM = "root";
  private static final String OBJECT_NODE_NM = "obj";
  private static final String NEW_LINE = "\n";

  // ------ Converting from CSV String to Node convention -------

  @Override
  public List<Node> from(String csvString) {
    try {
      List<String[]> rows = covertToRows(csvString);
      List<Node> objectNodes = createObjectNodes(rows);
      return Arrays.asList(new Node(ROOT_NODE_NM, objectNodes));
    } catch (Exception e) {
      LOGGER.error("Error While Parsing CSV String: {}", e.getMessage());
    }
    return Collections.emptyList();
  }

  private List<String[]> covertToRows(String csvString) throws CsvException, IOException {
    StringReader stringReader = new StringReader(csvString);
    CSVReader csvReader = new CSVReader(stringReader);
    List<String[]> rows = csvReader.readAll();
    csvReader.close();
    stringReader.close();
    return rows;
  }

  private List<Node> createObjectNodes(List<String[]> rows) {
    List<String> headerValues = Arrays.asList(rows.get(0));
    List<Node> objectNodes = new ArrayList<>();
    for (int i = 1; i < rows.size(); i++) {
      objectNodes.add(new Node(OBJECT_NODE_NM, createSubNodesForRow(headerValues, rows.get(i))));
    }
    return objectNodes;
  }

  private List<Node> createSubNodesForRow(List<String> headerValues, String[] row) {
    List<Node> nmValPairNodes = new ArrayList<>();
    List<String> rowValues = Arrays.asList(row);
    for (int i = 0; i < rowValues.size(); i++) {
      nmValPairNodes.add(new Node(headerValues.get(i), rowValues.get(i)));
    }
    return nmValPairNodes;
  }

  // ------ Converting from Node convention to JSON String -------

  @Override
  public String to(List<Node> nodes) {
    Node rootNode = nodes.get(0);
    return buildJSONString(rootNode.getSubNodes());
  }

  private String buildJSONString(List<Node> objectNodes) {
    Node firstObject = objectNodes.get(0);
    List<String> headers = getNamesFromNodeList(firstObject.getSubNodes());
    List<JSONObject> array = new ArrayList<>();
    for (Node objectNode : objectNodes) {
      JSONObject obj = jsonObjBuilder(getValuesFromNodeList(objectNode.getSubNodes()), headers);
      array.add(obj);
    }

    System.out.println(array.toString());
    return array.toString();
  }

  public JSONObject jsonObjBuilder(List<String> values, List<String> root) {
    // Map<String, String> map = new HashMap<>();
    // for (int i = 0; i < root.size(); i++) {
    // map.put(root.get(i), values.get(i));
    // }
    // return new JSONObject(map);
    Map<String, Object> map = new HashMap();
    for (Integer j = 0; j < root.size(); j++) {
      String key = root.get(j);
      String value = values.get(j);
      Map<String, Object> m = map;
      String[] parts = key.split("\\.");
      for (Integer i = 0; i < parts.length; i++) {
        String part = parts[i];
        if (i == parts.length - 1) {
          // At last part so put value
          m.put(part, value);
        } else {
          // Before last part so put map
          Map<String, Object> mm = (Map<String, Object>) m.get(part);
          if (mm == null) {
            mm = new HashMap<String, Object>();
            m.put(part, mm);
          }
          m = mm;
        }
      }
    }
    return new JSONObject(map);

  }

  private List<String> getNamesFromNodeList(List<Node> nodes) {
    return nodes.stream()
        .map(Node::getName)
        .collect(Collectors.toList());
  }

  private List<String> getValuesFromNodeList(List<Node> nodes) {
    return nodes.stream()
        .map(Node::getValue)
        .collect(Collectors.toList());
  }

  // ------ Doing Custom Processing based on Config -------

  @Override
  public List<Node> process(List<Node> nodes, PostProcessConfig config) {
    Node rootNode = nodes.get(0);
    List<Node> objNodes = rootNode.getSubNodes();
    List<String> destinationIds = getDestinationIds(config);
    Map<String, Integer> destIdToSrcIndexMap =
        getDestIdToSrcIndexMap(objNodes.get(0), config, destinationIds);
    processNodeList(objNodes, destinationIds, destIdToSrcIndexMap, config);
    return nodes;
  }

  private List<String> getDestinationIds(PostProcessConfig config) {
    return config.getMappers()
        .stream()
        .map(ConvConfig::getDestId)
        .collect(Collectors.toList());
  }

  private Map<String, Integer> getDestIdToSrcIndexMap(Node sampleObjNode, PostProcessConfig config,
      List<String> destinationIds) {
    List<Node> sampleValueNodes = sampleObjNode.getSubNodes();
    Map<String, String> destIdToSrcIdMap = getDestToSrcMap(config);
    return destinationIds.stream()
        .collect(Collectors.toMap(Function.identity(),
            destId -> getIndexOfNodeByName(sampleValueNodes, destIdToSrcIdMap.get(destId))));
  }

  private Integer getIndexOfNodeByName(List<Node> nodes, String name) {
    for (int index = 0; index < nodes.size(); index++) {
      String nodeName = nodes.get(index)
          .getName();
      if (nodeName.equals(name)) {
        return index;
      }
    }
    return -1;
  }

  private void processNodeList(List<Node> objNodes, List<String> destinationIds,
      Map<String, Integer> destIdToSrcIndexMap, PostProcessConfig config) {
    for (Node objNode : objNodes) {
      processObjectNode(objNode, destinationIds, destIdToSrcIndexMap, config);
    }
  }

  private void processObjectNode(Node objNode, List<String> destinationIds,
      Map<String, Integer> destIdToSrcIndexMap, PostProcessConfig config) {
    List<Node> valueNodes = objNode.getSubNodes();
    List<Node> newValueNodes = new ArrayList<>();
    for (String destId : destinationIds) {
      String defaultValue = getDefaultValue(config, destId);
      Integer srcIndex = destIdToSrcIndexMap.get(destId);
      if (srcIndex.equals(-1)) {
        newValueNodes.add(new Node(destId, defaultValue));
      } else {
        Node valueNode = valueNodes.get(srcIndex);
        String value = valueNode.getValue();
        if (value.isEmpty()) {
          newValueNodes.add(new Node(destId, defaultValue));
        } else {
          newValueNodes.add(new Node(destId, dm(value)));
        }
      }
    }
    objNode.setSubNodes(newValueNodes);
  }

  private String dm(String value) {
    return null == value ? "" : value.replace(NEW_LINE, ", ");
  }

  private String getDefaultValue(PostProcessConfig config, String destId) {
    for (ConvConfig mapper : config.getMappers()) {
      String destinationId = mapper.getDestId();
      if (destinationId.equals(destId)) {
        return null == mapper.getDefaultValue() ? "" : mapper.getDefaultValue();
      }
    }
    return "";
  }

  private Map<String, String> getDestToSrcMap(PostProcessConfig config) {
    List<ConvConfig> mappingsList = config.getMappers();
    return mappingsList.stream()
        .collect(Collectors.toMap(ConvConfig::getDestId, ConvConfig::getSourceId));
  }
}
