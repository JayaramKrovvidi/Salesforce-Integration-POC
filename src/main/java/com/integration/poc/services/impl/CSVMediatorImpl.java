package com.integration.poc.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
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
    return nodes;
  }
}
