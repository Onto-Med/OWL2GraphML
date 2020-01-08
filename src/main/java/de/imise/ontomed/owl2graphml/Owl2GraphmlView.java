package de.imise.ontomed.owl2graphml;

import de.imise.ontomed.owl2graphml.view.ConfigPanel;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

import java.awt.*;

public class Owl2GraphmlView extends AbstractOWLViewComponent {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void disposeOWLView() {}
	
	@Override
	protected void initialiseOWLView() {
		setLayout(new FlowLayout());
        ConfigPanel configPan = new ConfigPanel(getOWLWorkspace(), getOWLModelManager());
        add(configPan);
	}
}
