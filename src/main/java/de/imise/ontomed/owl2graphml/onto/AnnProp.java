package de.imise.ontomed.owl2graphml.onto;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;
import java.util.Set;

public class AnnProp {
	
	private OWLAnnotationProperty property;
	private Set<OWLOntology> ontologies;
	private OWLDataFactory factory;
	private OWLClass domain;
	private OWLDatatype range;
	
	public AnnProp(OWLAnnotationProperty property, Set<OWLOntology> ontologies, OWLDataFactory factory) {
		this.property = property;
		this.ontologies = ontologies;
		this.factory = factory;
	}
	
	public boolean hasSingleDomainCls() {
		Collection<IRI> domainIris = EntitySearcher.getDomains(property, ontologies);
		if (domainIris.size() == 1) {
			IRI domainIRI = domainIris.iterator().next();
			domain = factory.getOWLClass(domainIRI);
			return true;
		}
		
		return false;
	}
	
	public boolean hasSingleRangeDatatype() {
		Collection<IRI> rangeIris = EntitySearcher.getRanges(property, ontologies);
		if (rangeIris.size() == 1) {
			IRI rangeIRI = rangeIris.iterator().next();
			range = factory.getOWLDatatype(rangeIRI);
			return true;
		}
		
		return false;
	}

	public OWLAnnotationProperty getProperty() {
		return property;
	}

	public OWLClass getDomain() {
		return domain;
	}

	public OWLDatatype getRange() {
		return range;
	}
}
