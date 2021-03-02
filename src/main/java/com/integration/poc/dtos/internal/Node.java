package com.integration.poc.dtos.internal;

import java.util.List;
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

}
