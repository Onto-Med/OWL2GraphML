package de.imise.ontomed.owl2graphml.view;

import de.imise.ontomed.owl2graphml.graph.edge.Edge;
import de.imise.ontomed.owl2graphml.graph.node.Node;
import de.imise.ontomed.owl2graphml.onto.MainOntology;
import de.imise.ontomed.owl2graphml.xml.XML;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.OWLWorkspace;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class ConfigPanel extends JPanel {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	private JCheckBox taxonomy = new JCheckBox("taxonomy", true);
	private JCheckBox annotations = new JCheckBox("annotations", true);
	private JCheckBox propertyDefinitions = new JCheckBox("property definitions", true);

	private JCheckBox anonymousSuperClasses = new JCheckBox("anonymous superclasses", true);
	private JCheckBox equivalentClasses = new JCheckBox("equivalent classes", true);
	
	private JCheckBox individuals = new JCheckBox("individuals", true);
	private JCheckBox individualTypes = new JCheckBox("individual types", true);
	private JCheckBox individualAssertions = new JCheckBox("individual assertions", true);

	private JCheckBox grayscale = new JCheckBox("grayscale", false);
	
	private JComboBox<String> restrictionSuperClasses = new JComboBox<>(new String[]{"with type", "without type", "no"});
	private JComboBox<String> taxonomyDirection = new JComboBox<>(new String[]{"down", "up"});
	private JComboBox<String> taxonomyDepth = new JComboBox<>(new String[]{"all", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});

	private OWLWorkspace workspace;
	private OWLModelManager modelManager;
	
    private ConfigPanel configPanel = this;
    
    public ConfigPanel(OWLWorkspace workspace, OWLModelManager modelManager) {
    	this.workspace = workspace;
    	this.modelManager = modelManager;
    	
    	GridLayout layout = new GridLayout(5, 3);
    	layout.setHgap(15);
    	setLayout(layout);

		JPanel restrictionSuperClassesPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel restrictionSuperClassesLab = new JLabel("restriction superclasses");
		restrictionSuperClassesPan.add(restrictionSuperClassesLab);
    	restrictionSuperClassesPan.add(restrictionSuperClasses);
		JPanel taxonomyDirectionPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel taxonomyDirectionLab = new JLabel("taxonomy direction");
		taxonomyDirectionPan.add(taxonomyDirectionLab);
    	taxonomyDirectionPan.add(taxonomyDirection);
		JPanel taxonomyDepthPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel taxonomyDepthLab = new JLabel("taxonomy depth");
		taxonomyDepthPan.add(taxonomyDepthLab);
    	taxonomyDepthPan.add(taxonomyDepth);
    	
    	add(taxonomy);				add(individuals);			add(anonymousSuperClasses);
    	add(annotations); 			add(individualTypes);		add(equivalentClasses);
    	add(propertyDefinitions); 	add(individualAssertions);	add(grayscale);
    	
    	add(restrictionSuperClassesPan); add(taxonomyDirectionPan); add(taxonomyDepthPan);

		JButton createButton = new JButton("Create GraphML");
		add(createButton);
    	
    	createButton.addActionListener(ae -> {
			JFileChooser fc =  new JFileChooser();
			int returnVal = fc.showSaveDialog(null);
			if (returnVal != JFileChooser.APPROVE_OPTION) return;
			File file = fc.getSelectedFile();
			XML xml = new XML();

			LOGGER.info("Started writing GraphML file to " + file.getAbsolutePath() + "...");

			MainOntology onto = new MainOntology(
					modelManager.getActiveOntology(),
					configPanel.getStartClass().getIRI().toString(),
					configPanel.getTaxonomyDirection(),
					configPanel.getTaxonomyDepth()
			);

			if (configPanel.hasTaxonomy()) onto.addTaxonomy();
			if (configPanel.hasAnnotations()) onto.addAnnotations();
			if (configPanel.hasRestrictionSuperClasses()) onto.addPropertyRestrictionSuperClasses();
			if (configPanel.hasAnonymousSuperClasses())
				onto.addAnonymousSuperClasses(!configPanel.hasRestrictionSuperClasses());
			if (configPanel.hasEquivalentClasses()) onto.addEquivalentClasses();
			if (configPanel.hasIndividuals()) onto.addIndividuals();
			if (configPanel.hasIndividualTypes()) onto.addIndividualTypes();
			if (configPanel.hasIndividualAssertions()) onto.addIndividualAssertions();
			if (configPanel.hasPropertyDefinitions()) onto.addPropertyDefinitions();

			LOGGER.debug("Adding nodes to GraphML...");
			for (Node n : onto.getNodes()) {
				xml.addNode(n);
				LOGGER.debug(n.getLabel().replaceAll("\\r?\\n", " ") + " added");
			}

			LOGGER.debug("Adding edges to GraphML...");
			for (Edge e : onto.getEdges()) {
				xml.addEdge(e);
				LOGGER.debug(String.format("%s - %s - %s added", e.getSourceNode().getLabel(), e.getName(), e.getTargetNode().getLabel()));
			}

			xml.writeXML(file);
			LOGGER.info("Writing of GraphML file " + file.getAbsolutePath() + " finished.");
		});
    }

	public boolean hasTaxonomy() {
		return taxonomy.isSelected();
	}

	public boolean hasAnnotations() {
		return annotations.isSelected();
	}

	public boolean hasPropertyDefinitions() {
		return propertyDefinitions.isSelected();
	}

	public boolean hasRestrictionSuperClasses() {
		return !Objects.equals(restrictionSuperClasses.getSelectedItem(), "no");
	}

	@SuppressWarnings("unused")
	public boolean hasRestrictionSuperClassesWithType() {
		return Objects.equals(restrictionSuperClasses.getSelectedItem(), "with type");
	}

	public boolean hasAnonymousSuperClasses() {
		return anonymousSuperClasses.isSelected();
	}

	public boolean hasEquivalentClasses() {
		return equivalentClasses.isSelected();
	}

	public boolean hasIndividuals() {
		return individuals.isSelected();
	}

	public boolean hasIndividualTypes() {
		return individualTypes.isSelected();
	}

	public boolean hasIndividualAssertions() {
		return individualAssertions.isSelected();
	}

	public boolean hasGrayscale() {
		return grayscale.isSelected();
	}

	public String getTaxonomyDirection() {
		return Objects.requireNonNull(taxonomyDirection.getSelectedItem()).toString();
	}

	public int getTaxonomyDepth() {
		if (Objects.equals(taxonomyDepth.getSelectedItem(), "all"))
			return -1;
		else
			return Integer.parseInt(Objects.requireNonNull(taxonomyDepth.getSelectedItem()).toString());
	}    

	public OWLClass getStartClass() {
		OWLEntity selEntity = workspace.getOWLSelectionModel().getSelectedEntity();
		
		if (selEntity != null && selEntity.isOWLClass() && !selEntity.asOWLClass().equals(modelManager.getOWLDataFactory().getOWLThing()))
			return selEntity.asOWLClass();
		else
			return modelManager.getOWLDataFactory().getOWLThing();
	}

	@SuppressWarnings("unused")
	public void printOptions() {
		System.out.println(optionsToString());
	}

    public String optionsToString() {
		return String.join(
				"\n\t",
				"Options:",
				"StartClass: " + getStartClass(),
				"TaxonomyDirection: " + getTaxonomyDirection(),
				"TaxonomyDepth: " + getTaxonomyDepth(),
				"hasGrayscale: " + hasGrayscale(),
				"hasTaxonomy: " + hasTaxonomy(),
				"hasAnnotations: " + hasAnnotations(),
				"hasRestrictionSuperClasses: " + restrictionSuperClasses.getSelectedItem(),
				"hasPropertyDefinitions: " + hasPropertyDefinitions(),
				"hasAnonymousSuperClasses: " + hasAnonymousSuperClasses(),
				"hasEquivalentClasses: " + hasEquivalentClasses(),
				"hasIndividuals: " + hasIndividuals(),
				"hasIndividualTypes: " + hasIndividualTypes(),
				"hasIndividualAssertions: " + hasIndividualAssertions()
		);
	}
}
