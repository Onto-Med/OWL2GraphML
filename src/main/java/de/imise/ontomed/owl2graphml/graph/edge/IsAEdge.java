package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class IsAEdge extends Edge {

	public IsAEdge(Node sourceNode, Node targetNode) {
		super(sourceNode, targetNode, null);
	}

	public String getTargetArrowType() {
		return "white_delta";
	}

	public String getSourceArrowType() {
		return "none";
	}

	public String getLineStyleType() {
		return "line";
	}
	
	public String getName() {
		return "isA";
	}

}
