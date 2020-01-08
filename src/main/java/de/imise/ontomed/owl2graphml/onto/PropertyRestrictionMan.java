package de.imise.ontomed.owl2graphml.onto;

import de.imise.ontomed.owl2graphml.graph.edge.Edge;
import de.imise.ontomed.owl2graphml.graph.edge.PropertyEdge;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLQuantifiedDataRestriction;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;

public class PropertyRestrictionMan {
	private OWLClass cls;
	private OWLClassExpression clsExp;
	private ClassExpressionType expType;
	private MainOntology mainOnto;
	private boolean withType;
	
	public PropertyRestrictionMan(OWLClass cls, OWLClassExpression clsExp, MainOntology mainOnto, boolean withType) {
		this.mainOnto = mainOnto;
		this.cls = cls;
		this.clsExp = clsExp;
		this.expType = clsExp.getClassExpressionType();
		this.withType = withType;
	}
	
	public Edge getEdge() {
		if (isQuantifiedObjectRestriction())
			return getQuantifiedObjectRestrictionEdge();
		else if (isObjectCardinalityRestriction())
			return getObjectCardinalityRestrictionEdge();
		else if (isObjectHasValueRestriction())
			return getObjectHasValueRestrictionEdge();
		else if (isQuantifiedDataRestriction())
			return getQuantifiedDataRestrictionEdge();
		else if (isDataCardinalityRestriction())
			return getDataCardinalityRestrictionEdge();
		else if (isDataHasValueRestriction())
			return getDataHasValueRestrictionEdge();
		
		return null;
	}
	
	private Edge getObjectHasValueRestrictionEdge() {
		OWLObjectHasValue restr = (OWLObjectHasValue) clsExp;
		OWLObjectProperty prop = (OWLObjectProperty) restr.getProperty();
		OWLIndividual filler = restr.getFiller();
		return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getIndividualNode(filler), getLabel(prop));
	}

	private Edge getDataHasValueRestrictionEdge() {
		OWLDataHasValue restr = (OWLDataHasValue) clsExp;
		OWLDataProperty prop = (OWLDataProperty) restr.getProperty();
		OWLLiteral filler = restr.getFiller();
		return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getLiteralNode(filler), getLabel(prop));
	}

	private Edge getObjectCardinalityRestrictionEdge() {
		OWLObjectCardinalityRestriction restr = (OWLObjectCardinalityRestriction) clsExp;
		OWLObjectProperty prop = (OWLObjectProperty) restr.getProperty();
		OWLClassExpression filler = restr.getFiller();
		int card = restr.getCardinality();
		if (!filler.isAnonymous())
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getNamedClassNode(filler.asOWLClass()), getLabel(prop, card));
		else
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getManchesterExpressionNode(filler), getLabel(prop, card));
	}
	
	private Edge getDataCardinalityRestrictionEdge() {
		OWLDataCardinalityRestriction restr = (OWLDataCardinalityRestriction) clsExp;
		OWLDataProperty prop = (OWLDataProperty) restr.getProperty();
		OWLDataRange filler = restr.getFiller();
		int card = restr.getCardinality();
		if (filler.isDatatype())
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getDataTypeNode(filler.asOWLDatatype()), getLabel(prop, card));
		else
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getManchesterExpressionDataNode(filler), getLabel(prop, card));
	}

	private Edge getQuantifiedObjectRestrictionEdge() {
		OWLQuantifiedObjectRestriction restr = (OWLQuantifiedObjectRestriction) clsExp;
		OWLObjectProperty prop = (OWLObjectProperty) restr.getProperty();
		OWLClassExpression filler = restr.getFiller();
		if (!filler.isAnonymous())
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getNamedClassNode(filler.asOWLClass()), getLabel(prop));
		else
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getManchesterExpressionNode(filler), getLabel(prop));
	}

	private Edge getQuantifiedDataRestrictionEdge() {
		OWLQuantifiedDataRestriction restr = (OWLQuantifiedDataRestriction) clsExp;
		OWLDataProperty prop = (OWLDataProperty) restr.getProperty();
		OWLDataRange filler = restr.getFiller();
		if (filler.isDatatype())
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getDataTypeNode(filler.asOWLDatatype()), getLabel(prop));
		else
			return new PropertyEdge(mainOnto.getNamedClassNode(cls), mainOnto.getManchesterExpressionDataNode(filler), getLabel(prop));
	}
	
	private String getLabel(OWLEntity entity, int card) {
		String shortForm = mainOnto.getLabel(entity);
		
		String type = "";
		
		if (expType.equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM) || expType.equals(ClassExpressionType.DATA_ALL_VALUES_FROM))
			type =  "(all)";
		if (expType.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM) || expType.equals(ClassExpressionType.DATA_SOME_VALUES_FROM))
			type =  "(some)";
		if (expType.equals(ClassExpressionType.OBJECT_EXACT_CARDINALITY) || expType.equals(ClassExpressionType.DATA_EXACT_CARDINALITY))
			type =  "(="+card+")";
		if (expType.equals(ClassExpressionType.OBJECT_MIN_CARDINALITY) || expType.equals(ClassExpressionType.DATA_MIN_CARDINALITY))
			type =  "(>="+card+")";
		if (expType.equals(ClassExpressionType.OBJECT_MAX_CARDINALITY) || expType.equals(ClassExpressionType.DATA_MAX_CARDINALITY))
			type =  "(<="+card+")";
		if (expType.equals(ClassExpressionType.OBJECT_HAS_VALUE) || expType.equals(ClassExpressionType.DATA_HAS_VALUE))
			type =  "(value)";
		
		if (withType)
			return shortForm + type;
		else
			return shortForm;
	}
	
	private String getLabel(OWLEntity entity) {
		return getLabel(entity, -1);
	}
	
	public boolean isRestriction() {
		return isQuantifiedObjectRestriction() || isObjectCardinalityRestriction() || isObjectHasValueRestriction() ||
			   isQuantifiedDataRestriction() || isDataCardinalityRestriction() || isDataHasValueRestriction();
	}
	
	private boolean isQuantifiedObjectRestriction() {
		return expType.equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM) || expType.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM);
	}

	private boolean isQuantifiedDataRestriction() {
		return expType.equals(ClassExpressionType.DATA_ALL_VALUES_FROM) || expType.equals(ClassExpressionType.DATA_SOME_VALUES_FROM);
	}

	private boolean isObjectCardinalityRestriction() {
		return expType.equals(ClassExpressionType.OBJECT_MAX_CARDINALITY) || expType.equals(ClassExpressionType.OBJECT_MIN_CARDINALITY) ||
			   expType.equals(ClassExpressionType.OBJECT_EXACT_CARDINALITY);
	}

	private boolean isDataCardinalityRestriction() {
		return expType.equals(ClassExpressionType.DATA_MAX_CARDINALITY) || expType.equals(ClassExpressionType.DATA_MIN_CARDINALITY) ||
				expType.equals(ClassExpressionType.DATA_EXACT_CARDINALITY);
	}
	
	private boolean isObjectHasValueRestriction() {
		return expType.equals(ClassExpressionType.OBJECT_HAS_VALUE);
	}

	private boolean isDataHasValueRestriction() {
		return expType.equals(ClassExpressionType.DATA_HAS_VALUE);
	}
}
