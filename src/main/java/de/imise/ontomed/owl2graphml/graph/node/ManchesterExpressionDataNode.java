package de.imise.ontomed.owl2graphml.graph.node;

public class ManchesterExpressionDataNode extends Node {

	private int lineNum;
	private int maxLineLength;
	
	public ManchesterExpressionDataNode(String label, boolean grayscale) {
		super(label, -1, grayscale);
		
		super.label = super.label.replaceAll(" or ", System.lineSeparator() + "or ");
		
		String[] lines = super.label.split(("\\r?\\n"));
		lineNum = lines.length;
		
		setLinesToLabel(lines);
		
		maxLineLength = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.length() > maxLineLength)
				maxLineLength = line.length();
		}
	}

	public double getHeight() {
		return (((double)(lineNum)) * 13.5) + 11.5;
	}
	
	public double getWidth() {
		return (((double)(maxLineLength)) * charWidth) + 20.0;
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
		return "dotted";
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
