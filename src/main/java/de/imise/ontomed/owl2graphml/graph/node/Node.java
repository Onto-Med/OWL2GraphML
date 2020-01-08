package de.imise.ontomed.owl2graphml.graph.node;

public abstract class Node {
	
	public static int idCount = 0;
	private String id;
	protected String label;
	protected int ontoIndex;
	
	protected String[] fillColors;
	protected String thingFillColor;
	
	protected double charWidth = 6.6;
	
	protected Node(String label, int ontoIndex, boolean grayscale) {
		if (grayscale) {
			fillColors = new String[]{"#cccccc", "#ffffff"};
			thingFillColor = "#ffffff";
		} else {
			fillColors = new String[]{"#CC99FF", "#CCCCCC", "#FF9900", "#99CCFF", "#FFCC99", "#FF99CC"};
			thingFillColor = "#FF6600";
		}
		
		this.label = label;
		this.ontoIndex = ontoIndex;
		this.id = "n" + (++idCount);
	}
	
	public String getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public abstract double getHeight();
	public abstract double getWidth();
	public abstract boolean hasFillColor();
	public abstract boolean hasBorderColor();
	public abstract String getFillColor();
	public abstract String getBorderColor();
	public abstract String getBorderStyleType();
	public abstract String getShapeType();
	public abstract String getLabelModelName();
	public abstract String getLabelModelPosition();
	public abstract String getNodeType();
	
	public void setLinesToLabel(String[] lines) {
		StringBuilder newLabel = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			if (i != 0)
				newLabel.append(System.lineSeparator());
			
			newLabel.append(lines[i].trim());
		}
		this.label = newLabel.toString();
	}
}
