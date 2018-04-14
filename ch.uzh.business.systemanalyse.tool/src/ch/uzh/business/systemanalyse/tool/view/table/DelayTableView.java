/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.table;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.IVariableValidationChangeListener;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.VariableValidationType;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.delay.DelayTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.delay.DelayTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.delay.DelayVariableFieldEditingSupport;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class DelayTableView extends AbstractTableView implements IVariableValidationChangeListener {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.delaytableview";
	public static final String TITLE = "Cross-Time Matrix";

	@Override
	protected void registerMenus() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(viewer.getTable());
		viewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
		getSite().setSelectionProvider(viewer);
	}



	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		createTitle(parent, 1);
		super.createPartControl(parent);
		ModelProvider.INSTANCE.registerValidationListener(this);
	}



	@Override
	protected void setColumnsText() {
		viewer.getTable().getColumn(0).setText("");
		viewer.getTable().getColumn(1).setText("Variable name");
		for(int i = 0; i < ModelProvider.INSTANCE.getVariables().size(); i++){
			if(i < ModelProvider.INSTANCE.getVariables().size()) {
				viewer.getTable().getColumn(i + 
						IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).setText(
								String.valueOf(
										ModelProvider.INSTANCE.getVariables().get(i).getIndex()));
			}
		}
		viewer.getTable().getColumn(viewer.getTable().getColumnCount() - 1).setText("Produced Delay");
		//viewer.getTable().getColumn(ModelProvider.INSTANCE.getVariables().size() +
				//IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).setText("Produced Delay");
	}


	@Override
	protected EditingSupport createEditingSupport(TableViewer viewer,
			TableViewerColumn column) {
		return new DelayVariableFieldEditingSupport(viewer, Integer.valueOf(column.getColumn().getText()));
	}


	@Override
	protected AbstractTableModelProvider createModelProvider() {
		return new DelayTableModelProvider();
	}


	@Override
	protected AbstractTableLabelProvider createLabelProvider() {
		return new DelayTableLabelProvider(viewer);
	}



	@Override
	protected void checkValidation() {
		
	}



	@Override
	public void variableValidationChanged(VariableValidationType type) {
		/*if(type == VariableValidationType.DELAY) {
			Control c = this.viewer.getControl();
			c.getParent().getParent().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA));
			Viewer v = getViewer();
			showBusy(true);
			System.out.println();
		}*/
	}
}
