package com.integration.poc.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.integration.poc.dtos.internal.ConvConfig;
import com.integration.poc.dtos.internal.Node;
import com.integration.poc.dtos.internal.PostProcessConfig;
import com.integration.poc.services.IMediator;

@Service
public class CSVMediatorImpl implements IMediator {

  private static final String CSV_NEW_LINE_DELIMITTER = "\\r?\\n";
  private static final String REGEX_ELIMINATE_QUOTES = "^[\"']+|[\"']+$";
  private static final String ROOT_NODE_NM = "root";
  private static final String OBJECT_NODE_NM = "obj";
  private static final String NEW_LINE = "\n";

  // ------ Converting from CSV String to Node convention -------

  @Override
  public List<Node> from(String csvString) {
    String[] rows = csvString.split(CSV_NEW_LINE_DELIMITTER);
    List<Node> objectNodes = createObjectNodes(rows);
    return Arrays.asList(new Node(ROOT_NODE_NM, objectNodes));
  }

  private List<Node> createObjectNodes(String[] rows) {
    List<String> headerValues = splitValuesOfRow(rows[0]);
    List<Node> objectNodes = new ArrayList<>();
    for (int i = 1; i < rows.length; i++) {
      objectNodes.add(new Node(OBJECT_NODE_NM, createSubNodesForRow(headerValues, rows[i])));
    }
    return objectNodes;
  }

  private List<String> splitValuesOfRow(String row) {
    return Arrays.asList(row.split(","))
        .stream()
        .map(column -> column.replaceAll(REGEX_ELIMINATE_QUOTES, ""))
        .collect(Collectors.toList());
  }

  private List<Node> createSubNodesForRow(List<String> headerValues, String row) {
    List<Node> nmValPairNodes = new ArrayList<>();
    List<String> rowValues = splitValuesOfRow(row);
    for (int i = 0; i < rowValues.size(); i++) {
      nmValPairNodes.add(new Node(headerValues.get(i), rowValues.get(i)));
    }
    return nmValPairNodes;
  }

  // ------ Converting from Node convention to CSV String -------

  @Override
  public String to(List<Node> nodes) {
    Node rootNode = nodes.get(0);
    return buildCSVString(rootNode.getSubNodes());
  }

  private String buildCSVString(List<Node> objectNodes) {
    StringBuilder csvBuilder = new StringBuilder();

    // Add Header Line
    Node firstObject = objectNodes.get(0);
    List<String> headers = getNamesFromNodeList(firstObject.getSubNodes());
    addLineToCSVString(csvBuilder, headers);

    // Add Value lines
    for (Node objectNode : objectNodes) {
      addLineToCSVString(csvBuilder, getValuesFromNodeList(objectNode.getSubNodes()));
    }

    return csvBuilder.toString();
  }

  private void addLineToCSVString(StringBuilder csvBuilder, List<String> values) {
    csvBuilder.append(values.stream()
        .collect(Collectors.joining(",")));
    csvBuilder.append(NEW_LINE);
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
    processNodeList(objNodes, destinationIds, destIdToSrcIndexMap);
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
      Map<String, Integer> destIdToSrcIndexMap) {
    for (Node objNode : objNodes) {
      processObjectNode(objNode, destinationIds, destIdToSrcIndexMap);
    }
  }

  private void processObjectNode(Node objNode, List<String> destinationIds,
      Map<String, Integer> destIdToSrcIndexMap) {
    List<Node> valueNodes = objNode.getSubNodes();
    List<Node> newValueNodes = new ArrayList<>();
    for (String destId : destinationIds) {
      Integer srcIndex = destIdToSrcIndexMap.get(destId);
      if (srcIndex.equals(-1)) {
        newValueNodes.add(new Node(destId, ""));
      } else {
        Node valueNode = valueNodes.get(srcIndex);
        newValueNodes.add(new Node(destId, valueNode.getValue()));
      }
    }
    objNode.setSubNodes(newValueNodes);
  }

  private Map<String, String> getDestToSrcMap(PostProcessConfig config) {
    List<ConvConfig> mappingsList = config.getMappers();
    return mappingsList.stream()
        .collect(Collectors.toMap(ConvConfig::getDestId, ConvConfig::getSourceId));
  }
}
