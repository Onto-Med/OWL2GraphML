package de.imise.ontomed.owl2graphml.onto;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;
import java.util.Set;

public class DatProp {
	
	private OWLDataProperty property;
	private Set<OWLOntology> ontologies;
	private OWLClass domain;
	private OWLDatatype range;
	
	public DatProp(OWLDataProperty property, Set<OWLOntology> ontologies) {
		this.property = property;
		this.ontologies = ontologies;
	}
	
	public boolean hasSingleDomainCls() {
		Collection<OWLClassExpression> domains = EntitySearcher.getDomains(property, ontologies);
		if (domains.size() == 1) {
			OWLClassExpression domainExp = domains.iterator().next();
			if (!domainExp.isAnonymous()) {
				domain = domainExp.asOWLClass();
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasSingleRangeDatatype() {
		Collection<OWLDataRange> ranges = EntitySearcher.getRanges(property, ontologies);
		if (ranges.size() == 1) {
			OWLDataRange rangeExp = ranges.iterator().next();
			if (rangeExp.isDatatype()) {
				range = rangeExp.asOWLDatatype();
				return true;
			}
		}
		
		return false;
	}

	public OWLDataProperty getProperty() {
		return property;
	}

	public OWLClass getDomain() {
		return domain;
	}

	public OWLDatatype getRange() {
		return range;
	}
}
