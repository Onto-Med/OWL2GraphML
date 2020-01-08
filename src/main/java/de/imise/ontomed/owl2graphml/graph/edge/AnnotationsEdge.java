package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class AnnotationsEdge extends Edge {

	public AnnotationsEdge(Node sourceNode, Node targetNode) {
		super(sourceNode, targetNode, null);
	}

	public String getTargetArrowType() {
		return "none";
	}

	public String getSourceArrowType() {
		return "none";
	}

	public String getLineStyleType() {
		return "dashed";
	}
	
	public String getName() {
		return "annotatedWith";
	}
}