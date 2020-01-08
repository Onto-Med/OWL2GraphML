package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class DatPropertyDefEdge extends Edge {
	public DatPropertyDefEdge(Node sourceNode, Node targetNode, String label) {
		super(sourceNode, targetNode, label);
	}

	public String getTargetArrowType() {
		return "plain";
	}

	public String getSourceArrowType() {
		return "transparent_circle";
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
