package de.imise.ontomed.owl2graphml.onto;

import com.google.common.collect.Multimap;
import de.imise.ontomed.owl2graphml.util.Owl2GraphmlUtil;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.*;

public class Ontology {
	protected OWLOntology ontology;
	protected OWLDataFactory factory;
	protected Set<OWLOntology> ontologies;
	protected Set<OWLClass> classes;
	protected Set<OWLClass> rootClasses;
	
	protected ManchesterOWLSyntaxOWLObjectRendererImpl manchesterRenderer;
	
	protected Ontology(OWLOntology ontology) {
		OWLOntologyManager manager = ontology.getOWLOntologyManager();
		
		this.ontology    = ontology;
		this.factory     = manager.getOWLDataFactory();
		this.ontologies  = manager.getOntologies();
		this.classes     = ontology.getClassesInSignature(Imports.INCLUDED);
		this.rootClasses = getRootClasses();
		
		this.manchesterRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
		this.manchesterRenderer.setShortFormProvider(Owl2GraphmlUtil.getShortFormProvider(ontology));
	}

	protected Set<OWLClassExpression> getSubClasses(OWLClass cls, boolean isAnonymous) {
		Set<OWLClassExpression> subClasses = new HashSet<>();
		
		EntitySearcher.getSubClasses(cls, ontologies).forEach(subClsExp -> {
			if (subClsExp.isAnonymous() == isAnonymous)
				subClasses.add(subClsExp);
		});
		
		if (!isAnonymous && cls.equals(factory.getOWLThing()))
			subClasses.addAll(rootClasses);
		
		return subClasses;
	}
		
	protected Set<OWLClassExpression> getSuperClasses(OWLClass cls, boolean isAnonymous) {
		Set<OWLClassExpression> superClasses = new HashSet<>();
		
		EntitySearcher.getSuperClasses(cls, ontologies).forEach(superClsExp -> {
			if (superClsExp.isAnonymous() == isAnonymous)
				superClasses.add(superClsExp);
		});
		
		if (!isAnonymous && !cls.equals(factory.getOWLThing()) && superClasses.size() == 0)
			superClasses.add(factory.getOWLThing());
		
		return superClasses;
	}

	protected ArrayList<OWLClassExpression[]> getEquivalentClasses() {
		ArrayList<OWLClassExpression[]> eqClasses = new ArrayList<>();
		
		ontology.getTBoxAxioms(Imports.INCLUDED).forEach(owlAx -> {
			if (owlAx.getAxiomType().equals(AxiomType.EQUIVALENT_CLASSES)) {
				OWLEquivalentClassesAxiom eqClsAx = (OWLEquivalentClassesAxiom) owlAx;
				List<OWLClassExpression> eqClassesOfAx = eqClsAx.getClassExpressionsAsList();
				for (int i = 0; i < eqClassesOfAx.size(); i++) {
					for (int j = i+1; j < eqClassesOfAx.size(); j++) {
						eqClasses.add(new OWLClassExpression[]{eqClassesOfAx.get(i), eqClassesOfAx.get(j)});
					}
				}
			}
		});
		
		return eqClasses;
	}

	protected Collection<OWLIndividual> getIndividuals(OWLClass cls) {
		return EntitySearcher.getIndividuals(cls, ontologies);
	}

	protected Set<AnnProp> getAnnotationProperties() {
		Set<AnnProp> props = new HashSet<>();
		
		ontology.getAnnotationPropertiesInSignature(Imports.INCLUDED).forEach(prop -> {
			AnnProp annProp = new AnnProp(prop, ontologies, factory);
			if (annProp.hasSingleDomainCls() && annProp.hasSingleRangeDatatype())
				props.add(annProp);
		});
		
		return props; 
	}

	protected Set<ObjProp> getObjectProperties() {
		Set<ObjProp> props = new HashSet<>();

		ontology.getObjectPropertiesInSignature(Imports.INCLUDED).forEach(prop -> {
			ObjProp objProp = new ObjProp(prop, ontologies);
			if (objProp.hasSingleDomainCls() && objProp.hasSingleRangeCls())
				props.add(objProp);
		});
		
		return props; 
	}

	protected Set<DatProp> getDataProperties() {
		Set<DatProp> props = new HashSet<>();
		
		ontology.getDataPropertiesInSignature(Imports.INCLUDED).forEach(prop -> {
			DatProp datProp = new DatProp(prop, ontologies);
			if (datProp.hasSingleDomainCls() && datProp.hasSingleRangeDatatype())
				props.add(datProp);
		});
		
		return props; 
	}
	
	private Set<OWLClass> getRootClasses() {
		Set<OWLClass> rootClasses = new HashSet<>();
		for (OWLClass cls : classes)
			if (isRootClass(cls)) rootClasses.add(cls);
		
		return rootClasses;
	}
	
	private boolean isRootClass(OWLClass cls) {
		if (factory.getOWLThing().equals(cls))
			return false;

		return EntitySearcher.getSuperClasses(cls, ontologies).stream().allMatch(IsAnonymous::isAnonymous);
	}
	
	protected String getLabel(OWLEntity entity) {
		return Owl2GraphmlUtil.getShortFormProvider(ontology).getShortForm(entity);
	}
	
	protected String getOntoIriForOwlEntity(OWLEntity ent) {
		String ns = ent.getIRI().getNamespace();
		return ns.substring(0, ns.length()-1);
	}

	protected OWLAnnotation getAnnotation(String anIri, OWLClass cls) {
		OWLAnnotationProperty anProp = factory.getOWLAnnotationProperty(IRI.create(anIri));
		for (OWLOntology onto : ontologies)
			return EntitySearcher.getAnnotations(cls, onto, anProp).stream().findFirst().orElse(null);

		return null;
	}

	protected Set<OWLAnnotation> getAnnotations(OWLClass cls) {
		Set<OWLAnnotation> annotations = new HashSet<>();
		
		for (OWLOntology onto : ontologies)
			annotations.addAll(EntitySearcher.getAnnotations(cls, onto));
		
		return annotations;
	}
	
	protected String getAnnotationsAsString(OWLClass cls) {
		StringBuilder annotations = new StringBuilder();
		
		for (OWLAnnotation an : getAnnotations(cls)) {
			OWLAnnotationProperty prop = an.getProperty();
			annotations.append(getLabel(prop));

			OWLAnnotationValue value = an.getValue();
			if (value != null && value.asLiteral().isPresent())
				annotations.append(" := ").append(value.asLiteral().get().toString());
			
			annotations.append(System.lineSeparator());
		}
		
		return (annotations.toString().trim().equals("")) ? null : annotations.toString();
	}
	
	protected Multimap<OWLObjectPropertyExpression, OWLIndividual> getObjectPropertyValues(OWLIndividual ind) {
		return EntitySearcher.getObjectPropertyValues(ind, ontologies);
	}

	protected Multimap<OWLDataPropertyExpression, OWLLiteral> getDataPropertyValues(OWLIndividual ind) {
		return EntitySearcher.getDataPropertyValues(ind, ontologies);
	}
}
