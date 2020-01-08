package de.imise.ontomed.owl2graphml.graph.node;

public class NamedClassNode extends Node {
	
	public NamedClassNode(String label, int ontoIndex, boolean grayscale) {
		super(label, ontoIndex, grayscale);
	}
	
	public double getHeight() {
		return 25.0;
	}
	
	public double getWidth() {
		return (((double)(label.length())) * charWidth) + 10.0;
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
		return "roundrectangle";
	}
	
	public String getLabelModelName() {
		return null;
	}

	public String getLabelModelPosition() {
		return null;
	}
	
	public String getNodeType() {
		return "y:ShapeNode";
	}
}
