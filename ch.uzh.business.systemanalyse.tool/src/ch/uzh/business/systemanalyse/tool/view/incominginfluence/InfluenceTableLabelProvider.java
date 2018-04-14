/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.incominginfluence;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;

import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.view.outgoinginfluence.OutgoingInfluenceTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class InfluenceTableLabelProvider extends
		AbstractTableLabelProvider {

	public InfluenceTableLabelProvider(TableViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#getBottomRowTitle()
	 */
	@Override
	protected String getBottomRowTitle() {
		return "";
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#getVariableDisplayFormat()
	 */
	@Override
	protected String getVariableDisplayFormat() {
		return "%.2f";
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#calculateBottomValue(int)
	 */
	@Override
	protected float calculateBottomValue(int columnIndex) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#getTextForSecondRow(java.lang.Object)
	 */
	@Override
	protected String getTextForSecondRow(Object element) {
		if(element instanceof IncomingInfluenceTableRowData) {
			if(((IncomingInfluenceTableRowData)element).getRowIndex() == 0) {
				return "delay";
			} else if (((IncomingInfluenceTableRowData)element).getRowIndex() == 1) {
				return "effect";
			} else {
				return "";
			}
		} else {
			if(((OutgoingInfluenceTableRowData)element).getRowIndex() == 0) {
				return "delay";
			} else if (((OutgoingInfluenceTableRowData)element).getRowIndex() == 1) {
				return "effect";
			} else {
				return "";
			}
		}
	}

	@Override
	protected void formatBackgroundCell(ViewerCell cell, String string,
			boolean corner) {
		
	}

	@Override
	protected Color getValidationColor(ViewerCell cell) {
		return VALID_COLOR;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#getTextForVariableRow(java.lang.Object, int)
	 */
	/*@Override
	protected String getTextForVariableRow(Object element, int columnIndex) {
		String text = super.getTextForVariableRow(element, columnIndex);
		if(text.equals("Infinit))
	}*/

	
}
