package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class InstanceOfEdge extends Edge {

	public InstanceOfEdge(Node sourceNode, Node targetNode) {
		super(sourceNode, targetNode, null);
	}

	public String getTargetArrowType() {
		return "delta";
	}

	public String getSourceArrowType() {
		return "none";
	}

	public String getLineStyleType() {
		return "dashed";
	}
	
	public String getName() {
		return "instanceOf";
	}
}