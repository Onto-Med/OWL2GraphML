package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class EquivalenceEdge extends Edge {

	public EquivalenceEdge(Node sourceNode, Node targetNode) {
		super(sourceNode, targetNode, null);
	}

	public String getTargetArrowType() {
		return "delta";
	}

	public String getSourceArrowType() {
		return "delta";
	}

	public String getName() {
		return "equivalentTo";
	}

	public String getLineStyleType() {
		return "line";
	}

}
