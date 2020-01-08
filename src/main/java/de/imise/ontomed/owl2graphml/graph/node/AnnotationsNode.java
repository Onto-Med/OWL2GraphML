package de.imise.ontomed.owl2graphml.graph.node;

public class AnnotationsNode extends Node {
	
	private int lineNum;
	private int maxLineLength;
	
	public AnnotationsNode(String label, boolean grayscale) {
		super(label, -1, grayscale);
		String[] lines = super.label.split(("\\r?\\n"));
		lineNum = lines.length;
		
		maxLineLength = 0;
		for (String line : lines) {
			if (line.length() > maxLineLength)
				maxLineLength = line.length();
		}
	}

	public double getHeight() {
		return (((double)(lineNum)) * 13.5) + 11.5;
	}
	
	public double getWidth() {
		return (((double)(maxLineLength)) * charWidth) + 22.0;
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
		return null;
	}
	
	public String getLabelModelName() {
		return "internal";
	}

	public String getLabelModelPosition() {
		return "l";
	}
	
	public String getNodeType() {
		return "y:UMLNoteNode";
	}
}
