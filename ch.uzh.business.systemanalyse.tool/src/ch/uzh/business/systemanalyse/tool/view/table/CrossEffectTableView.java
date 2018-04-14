/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.table;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.crosseffect.CrossEffectTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.crosseffect.CrossEffectTableModelProvider;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class CrossEffectTableView extends AbstractTableView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.crosseffecttableview";
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
	 */
	/*@Override
	public void init(IViewSite site) throws PartInitException {
		ModelProvider.INSTANCE.generateShortestPathsImpact();
		super.init(site);
	}*/

	
	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#registerMenus()
	 */
	@Override
	protected void registerMenus() {

	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		createTitle(parent, 1);
		super.createPartControl(parent);
	}



	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#setColumnsText()
	 */
	@Override
	protected void setColumnsText() {
		viewer.getTable().getColumn(0).setText("");
		viewer.getTable().getColumn(1).setText("Variable name");
		for(int i = 0; i < ModelProvider.INSTANCE.getVariables().size(); i++){
			viewer.getTable().getColumn(i + 
					IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).setText(
							String.valueOf(
									ModelProvider.INSTANCE.getVariables().get(i).getIndex()));
		}
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createEditingSupport(org.eclipse.jface.viewers.TableViewer, org.eclipse.jface.viewers.TableViewerColumn)
	 */
	@Override
	protected EditingSupport createEditingSupport(TableViewer viewer,
			TableViewerColumn column) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createModelProvider()
	 */
	@Override
	protected AbstractTableModelProvider createModelProvider() {
		return new CrossEffectTableModelProvider();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createLabelProvider()
	 */
	@Override
	protected AbstractTableLabelProvider createLabelProvider() {
		return new CrossEffectTableLabelProvider(viewer);
	}

	@Override
	protected void checkValidation() {
	}

}
