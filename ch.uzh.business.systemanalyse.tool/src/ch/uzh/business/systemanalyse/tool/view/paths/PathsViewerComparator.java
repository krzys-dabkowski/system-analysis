/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.paths;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class PathsViewerComparator extends ViewerComparator {

	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;
	
	public PathsViewerComparator() {
		
	}
	
	public void setColumn(int column) {
		if(column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		Path p1 = (Path)e1;
		Path p2 = (Path)e2;
		int rc = 0;
		switch(propertyIndex) {
		
		case 1:
			rc = (p1.getNodes().size() > p2.getNodes().size() ? 1 : -1);
			break;
		case 2:
			rc = (p1.getDelay() > p2.getDelay() ? 1 : -1);
			break;
		case 3:
			rc = (p1.getEffect() > p2.getEffect() ? 1 : -1);
			break;
		case 0:
			rc = (p1.getIndex() < p2.getIndex() ? 1 : -1);
			break;
		default:
			rc = 0;
			break;
		}
		
		if(direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
	
}
