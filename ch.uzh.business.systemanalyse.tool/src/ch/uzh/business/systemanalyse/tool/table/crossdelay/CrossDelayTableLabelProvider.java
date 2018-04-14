/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.crossdelay;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;

import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class CrossDelayTableLabelProvider extends AbstractTableLabelProvider {

	public CrossDelayTableLabelProvider(TableViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#getLastColumnTitle()
	 */
	@Override
	protected String getBottomRowTitle() {
		return null;
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
		return Float.NaN;
	}

	@Override
	protected void formatBackgroundCell(ViewerCell cell, String string, boolean corner) {
	}

	@Override
	protected Color getValidationColor(ViewerCell cell) {
		return VALID_COLOR;
	}

}
