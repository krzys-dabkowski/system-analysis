/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.uzh.business.systemanalyse.tool.table.impact.ImpactTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class TableContentProvider implements IStructuredContentProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List<?>){
			List<ImpactTableRowData> rows = (List<ImpactTableRowData>)inputElement;
			return rows.toArray();
		}
		//String [] elements = new String[rows.g];
		return null;
	}

}
