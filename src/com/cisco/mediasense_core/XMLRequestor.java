package com.cisco.mediasense_core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.cisco.beans.RequestParameterBean;

/**
 * 
 * @author santoshs
 */
public class XMLRequestor {
	public RequestParameterBean read_From_XML(String id, String XML_FILE_PATH) {
		RequestParameterBean rpb = new RequestParameterBean();
		try {
			System.out.println("in read from XML");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(XML_FILE_PATH);
			XPath xpath = XPathFactory.newInstance().newXPath();

			// XPath Query for showing all nodes value
			String command = id;

			XPathExpression expr = xpath.compile("//request[@id='" + command
					+ "']/*/text()");
			String baseUrl = xpath.evaluate("/requestlist/baseurl", doc);
			System.out.println("base url:" + baseUrl);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			String[] array = new String[7];
			for (int i = 0; i < nodes.getLength(); i++) {
				array[i] = nodes.item(i).getNodeValue();
				System.out.println(nodes.item(i).getNodeValue());
			}
			String name = array[0];
			String url = baseUrl + array[1];
			String method = array[2];
			String header = array[3];
			String contenttype = array[4];
			String body = array[5];
			String classpath = array[6];
			System.out.println("name: " + name + "\r\n" + "url:" + url);
			rpb.setName(name);
			rpb.setBody(body);
			rpb.setContent_type(contenttype);
			rpb.setHeader(header);
			rpb.setMethod(method);
			rpb.setUrl(url);
			rpb.setClasspath(classpath);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return rpb;
	}

}
