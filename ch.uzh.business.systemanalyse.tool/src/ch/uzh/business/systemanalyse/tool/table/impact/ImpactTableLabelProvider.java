/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.impact;

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
public class ImpactTableLabelProvider extends AbstractTableLabelProvider {

	public ImpactTableLabelProvider(TableViewer viewer) {
		super(viewer);
	}

	@Override
	protected String getBottomRowTitle() {
		return "Passive sum";//"Active sum";
	}

	@Override
	protected String getVariableDisplayFormat() {
		return "%.2f";
	}

	@Override
	protected float calculateBottomValue(int columnIndex) {
		return ModelProvider.INSTANCE.getVariables().get(columnIndex -
				IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).calculatePassiveSum();
	}

	@Override
	protected void formatBackgroundCell(ViewerCell cell, String string, boolean corner) {
		if(corner) {
			cell.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), "TABLE_ROW_HIGHLIGHT_DARK")));
		} else {
			StyledString styledString = new StyledString(string, fBoldStyler);
			cell.setStyleRanges(styledString.getStyleRanges());
			cell.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), "TABLE_ROW_HIGHLIGHT_LIGHT")));
		}
	}

	@Override
	protected Color getValidationColor(ViewerCell cell) {
		if (((AbstractTableRowData) cell.getElement()).getVariable() == null) {
			//System.out.println("setting nocontent color for column " + cell.getColumnIndex());
			return cell.getBackground();
		} else {
			Integer columnIndex = cell.getColumnIndex();
			
			if(columnIndex >= 2 && columnIndex <=  ModelProvider.INSTANCE.getVariables().size() - 1
					+ IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS) {
			
				Variable var = ((AbstractTableRowData)cell.getElement()).getVariable();
				VariableValue variableValue = var.getImpactArray().get(columnIndex - 2);
				//System.out.println("var: " + var.getIndex() + " with var " + (columnIndex - 2));
				if(variableValue.isValidated()) {
					//System.out.println("setting valid color for var " + var.getIndex() + " column " + columnIndex);
					this.setValid(true);
					return cell.getBackground();
				} else {
					//System.out.println("setting invalid color for var " + var.getIndex() + " column " + columnIndex);
					this.setValid(false);
					return INVALID_COLOR;
				}
			} else {
				//System.out.println("setting test color for column " + columnIndex);
				return cell.getBackground();
			}
			/*
			if(var.isValidated()) {
				return display.getSystemColor(SWT.COLOR_WHITE);
			} else {
				return display.getSystemColor(SWT.COLOR_RED);
			}*/
		}
	}

	
}
