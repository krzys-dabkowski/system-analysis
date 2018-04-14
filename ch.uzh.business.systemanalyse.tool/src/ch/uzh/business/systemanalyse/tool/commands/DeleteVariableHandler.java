package ch.uzh.business.systemanalyse.tool.commands;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;
import ch.uzh.business.systemanalyse.tool.table.impact.ImpactTableRowData;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractTitledView;
import ch.uzh.business.systemanalyse.tool.view.table.DelayTableView;
import ch.uzh.business.systemanalyse.tool.view.table.ImpactTableView;

/**
 * 
 */

/**
 * Handler for deleting the selected variable.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class DeleteVariableHandler extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		IWorkbenchPart activePart = page.getActivePart();
		if (activePart instanceof AbstractTableView) {
			ISelection selection = ((AbstractTitledView)activePart).getSite().getSelectionProvider().getSelection();

			if(selection != null && selection instanceof IStructuredSelection) {
				AbstractTableView impactView = (AbstractTableView) page.findView(ImpactTableView.ID);
				AbstractTableView delayView = (AbstractTableView) page.findView(DelayTableView.ID);
				ModelProvider model = ModelProvider.INSTANCE;
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				for(Iterator<ImpactTableRowData> iterator = 
					structuredSelection.iterator(); iterator.hasNext();) {
					AbstractTableRowData row = iterator.next();
					model.deleteVariable(Integer.valueOf(row.getVariableIndex()));
					impactView.deleteVariableColumn(Integer.valueOf(row.getVariableIndex()));
					delayView.deleteVariableColumn(Integer.valueOf(row.getVariableIndex()));
				}
				//view.updateTable();
				impactView.getViewer().refresh();
				delayView.getViewer().refresh();
			}
		}
		ModelProvider.INSTANCE.modelChanged(false);
		return null;
	}

}
