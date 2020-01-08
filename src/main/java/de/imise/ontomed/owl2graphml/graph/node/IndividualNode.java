package de.imise.ontomed.owl2graphml.graph.node;

public class IndividualNode extends Node {

	public IndividualNode(String label, int ontoIndex, boolean grayscale) {
		super(label, ontoIndex, grayscale);
	}
	
	public double getHeight() {
		return 20.0;
	}
	
	public double getWidth() {
		return 20.0;
	}
	
	public boolean hasFillColor() {
		return true;
	}

	public boolean hasBorderColor() {
		return true;
	}

	public String getFillColor() {
		if (ontoIndex == -1)
			return thingFillColor;
		else
			return fillColors[ontoIndex % fillColors.length];
	}

	public String getBorderColor() {
		return "#000000";
	}

	public String getBorderStyleType() {
		return "line";
	}
	
	public String getShapeType() {
		return "ellipse";
	}
	
	public String getLabelModelName() {
		return "eight_pos";
	}

	public String getLabelModelPosition() {
		return "e";
	}

	public String getNodeType() {
		return "y:ShapeNode";
	}
}
