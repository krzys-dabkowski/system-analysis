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
import ch.uzh.business.systemanalyse.tool.table.impact.ImpactTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.impact.ImpactTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.impact.ImpactVariableFieldEditingSupport;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class ImpactTableView extends AbstractTableView implements IVariableValidationChangeListener {
	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.impacttableview";
	public static final String TITLE = "Cross-Impact Matrix";

	/**
	 * 
	 */
	public ImpactTableView() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
	 */
	/*@Override
	public void init(IViewSite site) throws PartInitException {
		ModelProvider.INSTANCE.fillModel();
		super.init(site);
	}*/

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
			} else {
				viewer.getTable().getColumn(i + IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).setText("new column");
			}
		}
		
		viewer.getTable().getColumn(viewer.getTable().getColumnCount() - 1).setText("Active sum");
				//ModelProvider.INSTANCE.getVariables().size() +
				//IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).setText("Active sum");
	}

	@Override
	protected void registerMenus() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(viewer.getTable());
		viewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
		getSite().setSelectionProvider(viewer);
	}

	@Override
	protected EditingSupport createEditingSupport(TableViewer viewer,
			TableViewerColumn column) {
		return new ImpactVariableFieldEditingSupport(viewer, Integer.valueOf(column.getColumn().getText()));// + 1);

	}

	@Override
	protected AbstractTableModelProvider createModelProvider() {
		return new ImpactTableModelProvider();
	}
	
	@Override
	protected AbstractTableLabelProvider createLabelProvider() {
		return new ImpactTableLabelProvider(viewer);
	}

	@Override
	protected void checkValidation() {
		
	}

	@Override
	public void variableValidationChanged(VariableValidationType type) {
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		ModelProvider.INSTANCE.unregisterValidationListener(this);
	}
	
}