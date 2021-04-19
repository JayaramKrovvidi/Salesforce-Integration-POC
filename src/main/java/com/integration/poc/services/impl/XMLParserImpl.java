package com.integration.poc.services.impl;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import com.integration.poc.services.IXMLParser;


@Service
public class XMLParserImpl implements IXMLParser {
  
  Document doc = null;

  public String parsedata(String response, String id) {
    try {
      if (null==doc) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(new InputSource(new StringReader(response)));
      } 
      doc.getDocumentElement()
          .normalize();
      return (doc.getElementsByTagName(id)
          .item(0)
          .getTextContent());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
}
