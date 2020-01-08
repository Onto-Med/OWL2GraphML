package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class PropertyEdge extends Edge {

	public PropertyEdge(Node sourceNode, Node targetNode, String label) {
		super(sourceNode, targetNode, label);
	}

	public String getTargetArrowType() {
		return "standard";
	}

	public String getSourceArrowType() {
		return "none";
	}

	public String getLineStyleType() {
		return "line";
	}
	
	public String getName() {
		if (hasLabel())
			return getLabel();
		else
			return "noLabelProp";
	}

}
