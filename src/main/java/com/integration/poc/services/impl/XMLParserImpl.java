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

	public String parsedata(String response,String id) {
	    try {
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(response)));
			doc.getDocumentElement().normalize();
			return(doc.getElementsByTagName(id).item(0).getTextContent());
			
	    
	    }  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return "-1";
		}
}
