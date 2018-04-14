/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.paths;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class PathTableLabelProvider extends StyledCellLabelProvider {

	public PathTableLabelProvider() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		int columnIndex = cell.getColumnIndex();
		Path path = (Path) cell.getElement();
		if(columnIndex == 0) {
			cell.setText(String.valueOf(path.getIndex()));
		} else if(columnIndex == 1) {
			cell.setText(String.valueOf(path.getNodes().size() - 1));
		} else if(columnIndex == 2) {
			cell.setText(String.valueOf(path.getDelay()));
		} else if(columnIndex == 3) {
			cell.setText(String.format("%.2f", path.getEffect()));
		} else if(columnIndex == 4) {
			String label = "";
			for(Integer node : path.getNodes()) {
				label += String.valueOf(node) + "->";
			}
			label = label.substring(0, label.length() - 2);
			cell.setText(label);
		} else {
			cell.setText("");
		}
		super.update(cell);
	}


}
