package de.imise.ontomed.owl2graphml.onto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.imise.ontomed.owl2graphml.graph.node.DataTypeNode;
import de.imise.ontomed.owl2graphml.graph.node.LiteralNode;
import de.imise.ontomed.owl2graphml.graph.node.ManchesterExpressionDataNode;
import de.imise.ontomed.owl2graphml.graph.node.NamedClassNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.imise.ontomed.owl2graphml.graph.edge.AnnPropertyDefEdge;
import de.imise.ontomed.owl2graphml.graph.edge.AnnotationsEdge;
import de.imise.ontomed.owl2graphml.graph.edge.DatPropertyDefEdge;
import de.imise.ontomed.owl2graphml.graph.edge.Edge;
import de.imise.ontomed.owl2graphml.graph.edge.EquivalenceEdge;
import de.imise.ontomed.owl2graphml.graph.edge.InstanceOfEdge;
import de.imise.ontomed.owl2graphml.graph.edge.IsAEdge;
import de.imise.ontomed.owl2graphml.graph.edge.ObjPropertyDefEdge;
import de.imise.ontomed.owl2graphml.graph.edge.PropertyEdge;
import de.imise.ontomed.owl2graphml.graph.node.AnnotationsNode;
import de.imise.ontomed.owl2graphml.graph.node.IndividualNode;
import de.imise.ontomed.owl2graphml.graph.node.ManchesterExpressionNode;
import de.imise.ontomed.owl2graphml.graph.node.Node;
import de.imise.ontomed.owl2graphml.xml.XML;

public class MainOntology extends Ontology {
	private List<String> ontoIris;
		
	private HashMap<String, Node> uniqueNodes;
	private HashSet<Node> nonUniqueNodes;
	private ArrayList<Edge> edges;
		
	private Multimap<OWLClass, OWLClass> selectedTaxonomyPart;
	private Set<OWLClass> selectedClasses;

	private String taxonomyDirection;
	private int taxonomyDepth;
	private boolean hasGrayscale;
	private boolean hasRestrictionSuperClassesWithType;

	private OWLClass startCls;
	
	public MainOntology(OWLOntology ontology, String startClassIri, String taxonomyDirection, int taxonomyDepth) {
		super(ontology);
		
		Node.idCount = 0;
		Edge.idCount = 0;
		
		this.startCls = factory.getOWLClass(IRI.create(startClassIri));
		this.taxonomyDirection = taxonomyDirection;
		this.taxonomyDepth = taxonomyDepth;
		this.uniqueNodes = new HashMap<>();
		this.nonUniqueNodes = new HashSet<>();
		this.edges = new ArrayList<>();
		
		this.ontoIris = new ArrayList<>();
		
		for (OWLOntology o : ontologies) {
			String ontoIri = o.getOntologyID().getOntologyIRI().toString();
			ontoIris.add(ontoIri);
		}

		setSelectedTaxonomyPart();
	}

	public Collection<Node> getNodes() {
		nonUniqueNodes.addAll(uniqueNodes.values());
		return nonUniqueNodes;
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////

	private void setSelectedTaxonomyPart() {
		selectedClasses = new HashSet<>();
		selectedClasses.add(startCls);
		selectedTaxonomyPart = HashMultimap.create();
		
		if ("Up".equals(taxonomyDirection))
			addSuperClasses(startCls, 0, taxonomyDepth);
		else
			addSubClasses(startCls, 0, taxonomyDepth);
	}
	
	private void addSubClasses(OWLClass cls, int index, int depth) {
		if (depth != -1 && index == depth)
			return;
		index++;
		for (OWLClassExpression subClsExp : getSubClasses(cls, false)) {
			OWLClass subCls = subClsExp.asOWLClass();
			selectedTaxonomyPart.put(cls, subCls);
			selectedClasses.add(subCls);
			addSubClasses(subCls, index, depth);
		}
	}
	
	private void addSuperClasses(OWLClass cls, int index, int depth) {
		if (depth != -1 && index == depth)
			return;
		index++;
		for (OWLClassExpression superClsExp : getSuperClasses(cls, false)) {
			OWLClass superCls = superClsExp.asOWLClass();
			selectedTaxonomyPart.put(superCls, cls);
			selectedClasses.add(superCls);
			addSuperClasses(superCls, index, depth);	
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	public void addTaxonomy() {
		for (OWLClass cls : selectedClasses) {
			for (OWLClass subCls : selectedTaxonomyPart.get(cls)) {
				edges.add(new IsAEdge(getNamedClassNode(subCls), getNamedClassNode(cls)));
			}
		}
	}

	@SuppressWarnings("unused")
	public void addAnnotations() {
		for (OWLClass cls : selectedClasses) {
			String annotations = getAnnotationsAsString(cls);
			if (annotations != null) {
				AnnotationsNode aNode = new AnnotationsNode(annotations, hasGrayscale);
				nonUniqueNodes.add(aNode);
				edges.add(new AnnotationsEdge(getNamedClassNode(cls), aNode));
			}
		}
	}

	@SuppressWarnings("unused")
	public void addPropertyRestrictionSuperClasses() {
		for (OWLClass cls : selectedClasses) {
			for (OWLClassExpression superClsExp : getSuperClasses(cls, true)) {
				PropertyRestrictionMan propMan = new PropertyRestrictionMan(cls, superClsExp, this, hasRestrictionSuperClassesWithType);
				if (propMan.isRestriction())
					edges.add(propMan.getEdge());
			}
		}
	}

	@SuppressWarnings("unused")
	public void addAnonymousSuperClasses(boolean all) {
		for (OWLClass cls : selectedClasses) {
			for (OWLClassExpression superClsExp : getSuperClasses(cls, true)) {
				if (all || !new PropertyRestrictionMan(cls, superClsExp, this, hasRestrictionSuperClassesWithType).isRestriction())
					edges.add(new IsAEdge(getNamedClassNode(cls), getManchesterExpressionNode(superClsExp)));
			}
		}
	}

	@SuppressWarnings("unused")
	public void addEquivalentClasses() {
		for (OWLClassExpression[] clsExpressions : getEquivalentClasses()) {
			
			if ( (!clsExpressions[0].isAnonymous() && selectedClasses.contains(clsExpressions[0].asOWLClass())) ||
				 (!clsExpressions[1].isAnonymous() && selectedClasses.contains(clsExpressions[1].asOWLClass())) ) {
			
				Node n1, n2;
				
				if (clsExpressions[0].isAnonymous())
					n1 = getManchesterExpressionNode(clsExpressions[0]);
				else
					n1 = getNamedClassNode(clsExpressions[0].asOWLClass());
	
				if (clsExpressions[1].isAnonymous())
					n2 = getManchesterExpressionNode(clsExpressions[1]);
				else
					n2 = getNamedClassNode(clsExpressions[1].asOWLClass());
				
				edges.add(new EquivalenceEdge(n1, n2));
			}
		}
	}

	@SuppressWarnings("unused")
	public void addIndividuals() {
		for (OWLClass cls : selectedClasses) {
			EntitySearcher.getIndividuals(cls, ontologies).forEach(this::getIndividualNode);
		}
	}

	@SuppressWarnings("unused")
	public void addIndividualTypes() {
		for (OWLClass cls : selectedClasses) {
			EntitySearcher.getIndividuals(cls, ontologies).forEach(
				ind -> edges.add(new InstanceOfEdge(getIndividualNode(ind), getNamedClassNode(cls)))
			);
		}
	}

	@SuppressWarnings("unused")
	public void addIndividualAssertions() {
		for (OWLClass cls : selectedClasses) {
			EntitySearcher.getIndividuals(cls, ontologies).forEach( ind -> {
				
				Multimap<OWLObjectPropertyExpression, OWLIndividual> indPropValues = getObjectPropertyValues(ind);
				for (OWLObjectPropertyExpression prop : indPropValues.keySet()) {
					for (OWLIndividual valInd : indPropValues.get(prop)) {
						edges.add(new PropertyEdge(getIndividualNode(ind), getIndividualNode(valInd), getLabel(prop.asOWLObjectProperty())));
					}
				}
				
				Multimap<OWLDataPropertyExpression, OWLLiteral> litPropValues = getDataPropertyValues(ind);
				for (OWLDataPropertyExpression prop : litPropValues.keySet()) {
					for (OWLLiteral valLit : litPropValues.get(prop)) {
						edges.add(new PropertyEdge(getIndividualNode(ind), getLiteralNode(valLit), getLabel(prop.asOWLDataProperty())));
					}
				}
			});
		}
	}

	@SuppressWarnings("unused")
	public void addPropertyDefinitions() {
		for (AnnProp prop : getAnnotationProperties()) {
			if (selectedClasses.contains(prop.getDomain()))
				edges.add(new AnnPropertyDefEdge(getNamedClassNode(prop.getDomain()), getDataTypeNode(prop.getRange()), getLabel(prop.getProperty())));
		}

		for (ObjProp prop : getObjectProperties()) {
			if (selectedClasses.contains(prop.getDomain()) || selectedClasses.contains(prop.getRange()))
				edges.add(new ObjPropertyDefEdge(getNamedClassNode(prop.getDomain()), getNamedClassNode(prop.getRange()), getLabel(prop.getProperty())));
		}

		for (DatProp prop : getDataProperties()) {
			if (selectedClasses.contains(prop.getDomain()))
				edges.add(new DatPropertyDefEdge(getNamedClassNode(prop.getDomain()), getDataTypeNode(prop.getRange()), getLabel(prop.getProperty())));
		}
	}

	@SuppressWarnings("unused")
	public XML toXml() {
		XML xml = new XML();
		
		for (Node n : getNodes()) xml.addNode(n);
        for (Edge e : getEdges()) xml.addEdge(e);
        
        return xml;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int getOntoIndexForOwlEntity(OWLEntity ent) {
		return ontoIris.indexOf(getOntoIriForOwlEntity(ent));
	}
	
	Node getNamedClassNode(OWLClass cls) {
		if (uniqueNodes.get(cls.getIRI().toString()) == null) {
			uniqueNodes.put(cls.getIRI().toString(), new NamedClassNode(getLabel(cls), getOntoIndexForOwlEntity(cls), hasGrayscale));
		}
		
		return uniqueNodes.get(cls.getIRI().toString());
	}

	Node getIndividualNode(OWLIndividual ind) {
		if (uniqueNodes.get(ind.asOWLNamedIndividual().getIRI().toString()) == null) {
			uniqueNodes.put(ind.asOWLNamedIndividual().getIRI().toString(), 
				new IndividualNode(getLabel(ind.asOWLNamedIndividual()), getOntoIndexForOwlEntity(ind.asOWLNamedIndividual()), hasGrayscale));
		}
		
		return uniqueNodes.get(ind.asOWLNamedIndividual().getIRI().toString());
	}
	
	Node getManchesterExpressionNode(OWLClassExpression clsExp) {
		ManchesterExpressionNode meNode = new ManchesterExpressionNode(manchesterRenderer.render(clsExp), hasGrayscale);
		nonUniqueNodes.add(meNode);
		return meNode;
	}

	Node getManchesterExpressionDataNode(OWLDataRange dr) {
		ManchesterExpressionDataNode medNode = new ManchesterExpressionDataNode(manchesterRenderer.render(dr), hasGrayscale);
		nonUniqueNodes.add(medNode);
		return medNode;
	}

	Node getLiteralNode(OWLLiteral lit) {
		LiteralNode litNode = new LiteralNode(lit.toString(), hasGrayscale);
		nonUniqueNodes.add(litNode);
		return litNode;
	}
	
	Node getDataTypeNode(OWLDatatype dt) {
		if (uniqueNodes.get(dt.getIRI().toString()) == null) {
			uniqueNodes.put(dt.getIRI().toString(), new DataTypeNode(getLabel(dt), hasGrayscale));
		}
		
		return uniqueNodes.get(dt.getIRI().toString());
	}

	@SuppressWarnings("unused")
	public void setHasGrayscale(boolean hasGreyScale) {
		this.hasGrayscale = hasGreyScale;
	}

	@SuppressWarnings("unused")
	public void setHasRestrictionSuperClassesWithType(boolean hasRestrictionSuperClassesWithType) {
		this.hasRestrictionSuperClassesWithType = hasRestrictionSuperClassesWithType;
	}
}
