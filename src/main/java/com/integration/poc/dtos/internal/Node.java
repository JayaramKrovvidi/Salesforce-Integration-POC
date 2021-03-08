package com.integration.poc.dtos.internal;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {

  private String name;
  private String value;

  private List<Node> subNodes;

  public Node() {}

  public Node(String name) {
    super();
    this.name = name;
  }

  public Node(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }


  public Node(String name, List<Node> subNodes) {
    super();
    this.name = name;
    this.subNodes = subNodes;
  }

  @Override
  public String toString() {
    return "\nNode [name=" + name + ", value=" + value + ", subNodes=" + printSubNodes(subNodes)
        + "]";
  }

  private String printSubNodes(List<Node> subNodes) {
    if (CollectionUtils.isEmpty(subNodes)) {
      return "{}";
    }
    String result = "{";
    for (Node node : subNodes) {
      result += "\t" + node.toString();
    }
    result += "\n}";
    return result;
  }

  public static void printNodes(List<Node> nodes) {
    for (Node node : nodes) {
      System.out.println(node.toString());
    }
  }

}
