package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class ObjPropertyDefEdge extends Edge {
	public ObjPropertyDefEdge(Node sourceNode, Node targetNode, String label) {
		super(sourceNode, targetNode, label);
	}

	public String getTargetArrowType() {
		return "plain";
	}

	public String getSourceArrowType() {
		return "circle";
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
