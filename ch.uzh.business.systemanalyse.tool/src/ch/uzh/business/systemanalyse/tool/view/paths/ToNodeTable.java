/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;


/**
 * @author Krzysztof Dabkowski
 *
 */
public class ToNodeTable extends AbstractPathTable {
	
	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.tonodetable";
	
	private Label endLabel;
	private Combo endCombo;
	private Button showPathsButton;
	/**
	 * 
	 */
	public ToNodeTable() {
		
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.paths.AbstractPathTable#createProviders()
	 */
	@Override
	protected void createProviders() {
		viewer.setLabelProvider(new PathTableLabelProvider());
		viewer.setContentProvider(new PathTableContentProvider());
		viewer.setInput(new PathsModelProvider(ModelProvider.INSTANCE.getToPaths()).getRows());

	}

	@Override
	protected void createWidgets(Composite parent) {
		endLabel = new Label(parent, SWT.NONE);
		endLabel.setText("end variable: ");
		endCombo = new Combo(parent, SWT.READ_ONLY);
		endCombo.setText("Var no.");
		showPathsButton = new Button(parent, SWT.PUSH);
		showPathsButton.setText("Show paths");
		showPathsButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				//String [] items = 
				int endSelection = endCombo.getSelectionIndex();
				if(endSelection != -1) {
					int endIndex = Integer.valueOf(endCombo.getItems()[endSelection]);
					ModelProvider.INSTANCE.setToPaths(endIndex);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		setComboItems();
	}

	@Override
	protected int getNumberOfGrids() {
		return 3;
	}

	@Override
	protected void setComboItems() {
		int count = 0;
		String [] startComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];
		String [] endComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];

		for(Variable v : ModelProvider.INSTANCE.getVariables()) {
			startComboItems [count] = String.valueOf(v.getIndex());
			endComboItems [count] = String.valueOf(v.getIndex());

			count++;
		}
		endCombo.setItems(endComboItems);
	}

}
