package de.imise.ontomed.owl2graphml.graph.node;

public class DataTypeNode extends Node {
	
	public DataTypeNode(String label, boolean grayscale) {
		super(label, -1, grayscale);
	}

	public double getHeight() {
		return 25.0;
	}
	
	public double getWidth() {
		return (((double)(label.length())) * charWidth) + 20.0;
	}
	
	public boolean hasFillColor() {
		return false;
	}

	public boolean hasBorderColor() {
		return true;
	}

	public String getFillColor() {
		return null;
	}

	public String getBorderColor() {
		return "#000000";
	}

	public String getBorderStyleType() {
		return "line";
	}
	
	public String getShapeType() {
		return "parallelogram";
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
