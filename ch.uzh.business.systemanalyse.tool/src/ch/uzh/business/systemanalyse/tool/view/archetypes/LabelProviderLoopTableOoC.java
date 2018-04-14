/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.archetypes;

import java.util.ArrayList;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.systempaths.PathsFinder;

/**
 * @author Krzysztof Dabkowski and Alexander Schmid
 *
 */
public class LabelProviderLoopTableOoC extends StyledCellLabelProvider {

	private int controlIndex;
	
	public LabelProviderLoopTableOoC(int controlIndex) {
		this.controlIndex = controlIndex;
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
			ArrayList<Integer> path1 = new ArrayList<Integer>();
			boolean after = false;
			for (Integer node : path.getNodes()){
				if (node.intValue() == this.controlIndex) {after = true;}
				if (after) {path1.add(node);}}
			cell.setText(String.valueOf(PathsFinder.calculateDelay(path1)));
		} else if(columnIndex == 4) {
			cell.setText(String.format("%.2f", path.getEffect()));
		} else if(columnIndex == 5) {
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
