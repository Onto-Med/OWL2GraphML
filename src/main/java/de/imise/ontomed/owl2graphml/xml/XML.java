package de.imise.ontomed.owl2graphml.xml;

import de.imise.ontomed.owl2graphml.graph.edge.Edge;
import de.imise.ontomed.owl2graphml.graph.node.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

public class XML {

	private Document doc;
	private Element graphml;
	private Element graph;
	
	public XML() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.newDocument();
			
			graphml = doc.createElement("graphml");
			graphml.setAttribute("xmlns", "http://graphml.graphdrawing.org/xmlns");
			graphml.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			graphml.setAttribute("xmlns:y", "http://www.yworks.com/xml/graphml");
			graphml.setAttribute("xmlns:yed", "http://www.yworks.com/xml/yed/3");
			graphml.setAttribute("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd");
		    doc.appendChild(graphml);
		    
		    addKeyElement("graphml", "d0", "resources", null);
			addKeyElement("port", "d1", "portgraphics", null);
			addKeyElement("port", "d2", "portgeometry", null);
			addKeyElement("port", "d3", "portuserdata", null);
			addKeyElement("node", "d4", null, "url");
			addKeyElement("node", "d5", null, "description");
			addKeyElement("node", "d6", "nodegraphics", null);
			addKeyElement("graph", "d7", null, "description");
			addKeyElement("edge", "d8", null, "url");
			addKeyElement("edge", "d9", null, "description");
			addKeyElement("edge", "d10", "edgegraphics", null);
		    
		    graph = doc.createElement("graph");
		    graph.setAttribute("edgedefault", "directed");
		    graph.setAttribute("id", "G");
		    graphml.appendChild(graph);
		    
		    Element data = doc.createElement("data");
		    data.setAttribute("key", "d7");
		    graph.appendChild(data);
		    
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private Element addKeyElement(String forAttr, String id, String type, String attrName) {
		Element element = doc.createElement("key");
		element.setAttribute("for", forAttr);
		element.setAttribute("id", id);
		if (type != null) element.setAttribute("yfiles.type", type);
		if (attrName != null) {
			element.setAttribute("attr.name", attrName);
			element.setAttribute("attr.type", "string");
		}
		graphml.appendChild(element);
		return element;
	}
	
	public void addNode(Node n) {
		Element node = doc.createElement("node");
		node.setAttribute("id", n.getId());
		graph.appendChild(node);
		
		Element data1 = doc.createElement("data");
	    data1.setAttribute("key", "d5");
	    node.appendChild(data1);

	    Element data2 = doc.createElement("data");
	    data2.setAttribute("key", "d6");
	    node.appendChild(data2);
	    
	    Element genericNode = doc.createElement(n.getNodeType());
	    data2.appendChild(genericNode);
	    
	    Element geometry = doc.createElement("y:Geometry");
	    geometry.setAttribute("height", ""+n.getHeight());
	    geometry.setAttribute("width", ""+n.getWidth());
	    genericNode.appendChild(geometry);
	    
	    Element fill = doc.createElement("y:Fill");
		if (n.hasFillColor())    
		    fill.setAttribute("color", n.getFillColor());
		else
			fill.setAttribute("hasColor", "false");
		fill.setAttribute("transparent", "false");
		genericNode.appendChild(fill);
	    
		    
	    Element borderStyle = doc.createElement("y:BorderStyle");
	    if (n.hasBorderColor())
	    	borderStyle.setAttribute("color", n.getBorderColor());
	    else
	    	borderStyle.setAttribute("hasColor", "false");
	    borderStyle.setAttribute("type", n.getBorderStyleType());
	    borderStyle.setAttribute("width", "1.0");
	    genericNode.appendChild(borderStyle);
	    
	    Element nodeLabel = doc.createElement("y:NodeLabel");
	    nodeLabel.setAttribute("fontFamily", "Consolas");
	    nodeLabel.setAttribute("fontStyle", "bold");
	    if (n.getLabelModelName() != null && n.getLabelModelPosition() != null) {
	    	nodeLabel.setAttribute("modelName", n.getLabelModelName());
	    	nodeLabel.setAttribute("modelPosition", n.getLabelModelPosition());
	    }
	    nodeLabel.setTextContent(n.getLabel());
	    genericNode.appendChild(nodeLabel);
	    
	    if (n.getShapeType() != null) {		    
		    Element shape = doc.createElement("y:Shape");
		    shape.setAttribute("type", n.getShapeType());
		    genericNode.appendChild(shape);
	    }
	}

	public void addEdge(Edge e) {
		Element edge = doc.createElement("edge");
		edge.setAttribute("id", e.getId());
		edge.setAttribute("source", e.getSourceNodeId());
		edge.setAttribute("target", e.getTargetNodeId());
		graph.appendChild(edge);
		
		Element data1 = doc.createElement("data");
	    data1.setAttribute("key", "d9");
	    edge.appendChild(data1);

	    Element data2 = doc.createElement("data");
	    data2.setAttribute("key", "d10");
	    edge.appendChild(data2);
	    
	    Element polyLineEdge = doc.createElement("y:PolyLineEdge");
	    data2.appendChild(polyLineEdge);
	    
	    Element path = doc.createElement("y:Path");
	    path.setAttribute("sx", "0.0");
	    path.setAttribute("sy", "0.0");
	    path.setAttribute("tx", "0.0");
	    path.setAttribute("ty", "0.0");
	    polyLineEdge.appendChild(path);
	    
	    Element lineStyle = doc.createElement("y:LineStyle");
	    lineStyle.setAttribute("color", "#000000");
	    lineStyle.setAttribute("type", e.getLineStyleType());
	    lineStyle.setAttribute("width", "1.0");
	    polyLineEdge.appendChild(lineStyle);

	    Element arrows = doc.createElement("y:Arrows");
	    arrows.setAttribute("source", e.getSourceArrowType());
	    arrows.setAttribute("target", e.getTargetArrowType());
	    polyLineEdge.appendChild(arrows);
	    
	    if (e.hasLabel()) {
	    	Element edgeLabel = doc.createElement("y:EdgeLabel");
	    	edgeLabel.setAttribute("fontFamily", "Dialog");
	    	edgeLabel.setAttribute("backgroundColor", "#FFFFFF");
	    	edgeLabel.setTextContent(e.getLabel());
		    polyLineEdge.appendChild(edgeLabel);
	    }
	}

	public File writeXML(File file) {
		Element data = doc.createElement("data");
		data.setAttribute("key", "d0");
		data.appendChild(doc.createElement("y:Resources"));
		graphml.appendChild(data);

		generateXml(new StreamResult(file));

		return file;
	}
	
	public String toString() {
		Element data = doc.createElement("data");
	    data.setAttribute("key", "d0");
	    data.appendChild(doc.createElement("y:Resources"));
	    graphml.appendChild(data);

		StringWriter sw = new StringWriter();
		generateXml(new StreamResult(sw));

		return sw.toString();
	}

	private StreamResult generateXml(StreamResult streamResult) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), streamResult);
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}

		return streamResult;
	}
}
