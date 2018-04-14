/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.table;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;

/**
 * @author Krzysztof Dabkowski
 *
 */
public abstract class AbstractInfluenceTableView extends AbstractTableView {

	
	Label varLabel;
	Combo varCombo;
	Button showButton;
	Text varNameText;
	
	public int analyzedVariableIndex = -1;
	
	/**
	 * 
	 */
	public AbstractInfluenceTableView() {
		
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		
		createTitle(parent, 1);
		
		createWidgets(parent);
		
		super.createPartControl(parent);
		//((GridData)pa.getLayoutData()).horizontalSpan = getNumberOfGrids();
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = getNumberOfGrids();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}
	
	private int getNumberOfGrids() {
		return 4;
	}

	private Composite createWidgets(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(getNumberOfGrids(), false);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		varLabel = new Label(composite, SWT.NONE);
		//varLabel.setText("end variable: ");
		varLabel.setText(getVariableLabelText());
		varCombo = new Combo(composite, SWT.READ_ONLY);
		varCombo.setText("Var no.");
		
		
		
		showButton = new Button(composite, SWT.PUSH);
		showButton.setText("Show influence");
		showButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				//String [] items = 
				int endSelection = varCombo.getSelectionIndex();
				if(endSelection != -1) {
					int selectionValue = Integer.valueOf(varCombo.getItem(endSelection));
					varNameText.setText(
							ModelProvider.INSTANCE.getVariable(selectionValue).getIndex() + " - " +
							ModelProvider.INSTANCE.getVariable(selectionValue).getName());
					updateTable();
					analyzedVariableIndex = selectionValue;
					ModelProvider.INSTANCE.modelChanged(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		//int count = 0;
		//String [] startComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];
		/*String [] endComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];

		for(Variable v : ModelProvider.INSTANCE.getVariables()) {
			//startComboItems [count] = String.valueOf(v.getIndex());
			endComboItems [count] = String.valueOf(v.getIndex());

			count++;
		}*/
		varCombo.setItems(getComboItems());
		
		varNameText = new Text(composite, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER | SWT.LEFT);
		GridData textGridData = new GridData();
		textGridData.horizontalAlignment = GridData.FILL;
		textGridData.grabExcessHorizontalSpace = true;
		//textGridData.horizontalSpan = 2;
		varNameText.setLayoutData(textGridData);
		
		return composite;
	}
	
	abstract String getVariableLabelText();

	private String [] getComboItems() {
		int count = 0;
		String [] endComboItems = new String[ModelProvider.INSTANCE.getVariables().size()];

		for(Variable v : ModelProvider.INSTANCE.getVariables()) {
			endComboItems [count] = String.valueOf(v.getIndex());

			count++;
		}
		
		return endComboItems;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#registerMenus()
	 */
	@Override
	protected void registerMenus() {
		

	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createEditingSupport(org.eclipse.jface.viewers.TableViewer, org.eclipse.jface.viewers.TableViewerColumn)
	 */
	@Override
	protected EditingSupport createEditingSupport(TableViewer viewer,
			TableViewerColumn column) {
		return null;
	}

	
	@Override
	public void fireModelChange(boolean clear) {
		int oldVariableIndex = -1;
		if(varCombo.getSelectionIndex() != -1) {
			oldVariableIndex =  Integer.valueOf(varCombo.getItems()[varCombo.getSelectionIndex()]);
		}
		
		super.fireModelChange(clear);
		
		
		varCombo.setItems(getComboItems());
		String[] comboItems = getComboItems();
		int newVariableIndex = -1;
		for(int i = 0; i < comboItems.length; i++) {
			if(Integer.valueOf(comboItems[i]) == oldVariableIndex) {
				newVariableIndex = i;
				break;
			}
		}
		varCombo.select(newVariableIndex);
	}
	

}
