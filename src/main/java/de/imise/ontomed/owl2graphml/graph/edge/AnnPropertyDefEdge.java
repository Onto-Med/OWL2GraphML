package de.imise.ontomed.owl2graphml.graph.edge;

import de.imise.ontomed.owl2graphml.graph.node.Node;

public class AnnPropertyDefEdge extends Edge {
	public AnnPropertyDefEdge(Node sourceNode, Node targetNode, String label) {
		super(sourceNode, targetNode, label);
	}

	public String getTargetArrowType() {
		return "plain";
	}

	public String getSourceArrowType() {
		return "skewed_dash";
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
