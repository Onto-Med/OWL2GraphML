package de.imise.ontomed.owl2graphml.graph.node;

public class LiteralNode extends Node {
	
	public LiteralNode(String label, boolean grayscale) {
		super(label, -1, grayscale);
	}

	public double getHeight() {
		return 25.0;
	}
	
	public double getWidth() {
		return (((double)(label.length())) * charWidth) + 10.0;
	}
	
	public boolean hasFillColor() {
		return false;
	}

	public boolean hasBorderColor() {
		return false;
	}

	public String getFillColor() {
		return null;
	}

	public String getBorderColor() {
		return null;
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