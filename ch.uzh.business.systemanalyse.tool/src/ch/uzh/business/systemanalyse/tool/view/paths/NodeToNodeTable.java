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
public class NodeToNodeTable extends AbstractPathTable {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.nodetonodetable";

	private Label startLabel;
	private Label endLabel;
	private Combo startCombo;
	private Combo endCombo;
	private Button showPathsButton;

	protected void createWidgets(Composite parent) {
		startLabel = new Label(parent, SWT.NONE);
		startLabel.setText("start variable: ");
		startCombo = new Combo(parent, SWT.READ_ONLY);
		startCombo.setText("Var no.");
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
				int startSelection = startCombo.getSelectionIndex();
				int endSelection = endCombo.getSelectionIndex();
				if(startSelection != -1 && endSelection != -1) {
					int startIndex = Integer.valueOf(startCombo.getItem(startCombo.getSelectionIndex()));
					int endIndex = Integer.valueOf(endCombo.getItem(endCombo.getSelectionIndex()));
					ModelProvider.INSTANCE.setFromToPaths(startIndex, endIndex);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		setComboItems();
	}
	
	protected void setComboItems() {
		int count = 0;
		String [] startComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];
		String [] endComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];

		for(Variable v : ModelProvider.INSTANCE.getVariables()) {
			startComboItems [count] = String.valueOf(v.getIndex());
			endComboItems [count] = String.valueOf(v.getIndex());

			count++;
		}
		//int oldStartSelection = Integer.valueOf(startCombo.getItem(startCombo.getSelectionIndex()));
		int oldStartSelection = startCombo.getSelectionIndex();
		//int oldEndSelection = Integer.valueOf(endCombo.getItem(endCombo.getSelectionIndex()));
		int oldEndSelection = endCombo.getSelectionIndex();
		startCombo.setItems(startComboItems);
		endCombo.setItems(endComboItems);
		/*startLabel.pack();
		startCombo.pack();
		endLabel.pack();
		endCombo.pack();
		showPathsButton.pack();*/
		//adjustGridData();
		
		startCombo.select(oldStartSelection);
		endCombo.select(oldEndSelection);
	}

	@Override
	protected void createProviders() {
		viewer.setLabelProvider(new PathTableLabelProvider());
		viewer.setContentProvider(new PathTableContentProvider());
		//if(ModelProvider.i)
		viewer.setInput(new PathsModelProvider(ModelProvider.INSTANCE.getFromToPaths()).getRows());
		
	}

	@Override
	protected int getNumberOfGrids() {
		return 5;
	}

}
