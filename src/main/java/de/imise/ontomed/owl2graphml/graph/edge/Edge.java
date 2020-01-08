package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public abstract class Edge {
	public static int idCount = 0;
	private String id;
	private Node sourceNode;
	private Node targetNode;
	private String label;
	
	public Edge(Node sourceNode, Node targetNode, String label) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.label = label;
		this.id = "e" + (++idCount);
	}
	
	public String getId() {
		return id;
	}
	
	public Node getSourceNode() {
		return sourceNode;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public String getSourceNodeId() {
		return sourceNode.getId();
	}
	
	public String getTargetNodeId() {
		return targetNode.getId();
	}
	
	public boolean hasLabel() {
		return label != null && !"".equals(label.trim());
	}
	
	public String getLabel() {
		return label;
	}
	
	public abstract String getName();
	public abstract String getTargetArrowType();
	public abstract String getSourceArrowType();
	public abstract String getLineStyleType();
}
