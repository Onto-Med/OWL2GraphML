package de.imise.ontomed.owl2graphml.util;

import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;

public class Owl2GraphmlUtil {
    /**
     * Returns a ShortFormProvider for the given OWLOntology.
     * @param ontology an OWLOntology object
     * @return ShortFormProvider
     */
    public static BidirectionalShortFormProvider getShortFormProvider(OWLOntology ontology) {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        return new BidirectionalShortFormProviderAdapter(
                manager.getOntologies(),
                new ManchesterOWLSyntaxPrefixNameShortFormProvider(manager.getOntologyFormat(ontology))
        );
    }
}
