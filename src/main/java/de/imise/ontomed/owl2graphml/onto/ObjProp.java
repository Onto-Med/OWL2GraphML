package de.imise.ontomed.owl2graphml.onto;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;
import java.util.Set;

public class ObjProp {
	
	private OWLObjectProperty property;
	private Set<OWLOntology> ontologies;
	private OWLClass domain;
	private OWLClass range;
	
	public ObjProp(OWLObjectProperty property, Set<OWLOntology> ontologies) {
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
	
	public boolean hasSingleRangeCls() {
		Collection<OWLClassExpression> ranges = EntitySearcher.getRanges(property, ontologies);
		if (ranges.size() == 1) {
			OWLClassExpression rangeExp = ranges.iterator().next();
			if (!rangeExp.isAnonymous()) {
				range = rangeExp.asOWLClass();
				return true;
			}
		}
		
		return false;
	}

	public OWLObjectProperty getProperty() {
		return property;
	}

	public OWLClass getDomain() {
		return domain;
	}

	public OWLClass getRange() {
		return range;
	}
}
