package ch.uzh.business.systemanalyse.tool.commands;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.view.paths.AbstractPathTable;

/**
 * 
 */

/**
 * @author krzysztof.dabkowski
 *
 */
public class SaveTablePathsXls extends SavePathsXls {

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.commands.SavePathsXls#findPaths(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	protected List<Path> findPaths(ExecutionEvent event) {
		IViewPart viewPart = (IViewPart) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		assert viewPart instanceof AbstractPathTable;
		
		TableViewer tableViewer = ((AbstractPathTable)viewPart).getViewer();
		List<Path> paths = ((List<Path>)tableViewer.getInput());
		return paths;
	}

	
}
