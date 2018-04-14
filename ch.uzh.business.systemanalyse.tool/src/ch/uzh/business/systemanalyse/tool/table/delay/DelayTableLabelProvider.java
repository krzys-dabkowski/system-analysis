/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.delay;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class DelayTableLabelProvider extends AbstractTableLabelProvider {

	public DelayTableLabelProvider(TableViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider#getLastColumnTitle()
	 */
	@Override
	protected String getBottomRowTitle() {
		return "Received delay";
	}

	@Override
	protected String getVariableDisplayFormat() {
		return "%.2f";
	}

	@Override
	protected float calculateBottomValue(int columnIndex) {
		return ModelProvider.INSTANCE.getVariables().get(columnIndex -
				IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).calculateReceivedDelay();
	}

	@Override
	protected void formatBackgroundCell(ViewerCell cell, String string, boolean corner) {
		if(corner) {
			cell.setBackground(new Color(Display.getCurrent(), IProjectSettingsConstants.DEFAULT_TABLE_ROW_HIGHLIGHT_DARK));
		} else {
			StyledString styledString = new StyledString(string, fBoldStyler);
			cell.setStyleRanges(styledString.getStyleRanges());
			cell.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), "TABLE_ROW_HIGHLIGHT_LIGHT")));
		}
	}

	@Override
	protected Color getValidationColor(ViewerCell cell) {
		if (((AbstractTableRowData) cell.getElement()).getVariable() == null) {
			return cell.getBackground();
		} else {
			Integer columnIndex = cell.getColumnIndex();
			
			if(columnIndex >= 2 && columnIndex <=  ModelProvider.INSTANCE.getVariables().size() - 1
					+ IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS) {
			
				Variable var = ((AbstractTableRowData)cell.getElement()).getVariable();
				VariableValue variableValue = var.getDelayArray().get(columnIndex - 2);
				if(variableValue.isValidated()) {
					this.setValid(true);
					return cell.getBackground();
				} else {
					this.setValid(false);
					return INVALID_COLOR;
				}
			} else {
				return cell.getBackground();
			}
		}
	}

}
