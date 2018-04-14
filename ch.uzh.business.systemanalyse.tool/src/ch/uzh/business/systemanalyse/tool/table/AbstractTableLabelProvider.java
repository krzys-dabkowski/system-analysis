/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

/**
 * @author Krzysztof Dabkowski
 *
 */
abstract public class AbstractTableLabelProvider extends StyledCellLabelProvider {

	private static Font font;
	protected final Styler fBoldStyler;
	protected final static Color COLOR_HIGHLIGHTED_BCKGND = new Color(Display.getCurrent(), PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), "TABLE_ROW_HIGHLIGHT_LIGHT"));
	protected final static Color VALID_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	protected final static Color INVALID_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	protected final static Color TEST_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	protected final static Color NOCONTENT_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	
	//private Variable unvalidatedVariable;
	//private VariableValue unvalidatedVariableValue;
	private boolean valid = true;

	public AbstractTableLabelProvider(TableViewer viewer) {
		font = new Font(Display.getCurrent(), getModifiedFontData(viewer.getTable().getFont().getFontData(), SWT.BOLD));
		fBoldStyler = new Styler() {

			@Override
			public void applyStyles(TextStyle textStyle) {
				textStyle.font = font;
			}
		};

	}

	private static FontData[] getModifiedFontData(FontData[] originalData, int additionalStyle) {
		FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
		}
		return styleData;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		cell.setStyleRanges(null);
		cell.setText(getTextForCell(cell.getElement(), cell.getColumnIndex(), cell));
		//cell.getControl().setBackground(null);
		cell.setBackground(getValidationColor(cell));
		super.update(cell);
	}

	abstract protected Color getValidationColor(ViewerCell cell);

	private String getTextForCell(Object element, int columnIndex, ViewerCell cell) {
		
		if (((AbstractTableRowData) element).getVariable() != null) {
			
			return getTextForCellVariable(element, columnIndex, cell);

		} else {

			return getTextForCellLowest(columnIndex, cell);

		}
	}

	private String getTextForCellVariable(Object element, int columnIndex, ViewerCell cell) {
		if (columnIndex == 0) {
			return getTextForFirstRow(element);
		}
		if (columnIndex == 1) {
			return getTextForSecondRow(element);
		}
		if (columnIndex == ModelProvider.INSTANCE.getVariables().size()
				+ IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS) {
			Float value = ((AbstractTableRowData) element).getRightHandValue();
			if(value.equals(Float.NaN)) {
				formatBackgroundCell(cell, "", false);
				return "";
			} else {
				String string = String.format("%.2f", value);
				formatBackgroundCell(cell, string, false);
				return string;
			}
		}
		if (columnIndex < 2
				|| columnIndex > ((AbstractTableRowData) element)
				.getVariableValueArray().size() + 1) {
			return "0";
		} else {
			return getTextForVariableRow(element, columnIndex);
		}
	}

	private String getTextForCellLowest(int columnIndex, ViewerCell cell) {
		String string;
		if(columnIndex == ModelProvider.INSTANCE.getVariables().size() +
				IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS) {
			string = "";
			formatBackgroundCell(cell, string, true);
		} else if(columnIndex == 0){
			string = "";
			formatBackgroundCell(cell, string, false);
		} else if (columnIndex == 1) {
			string = getBottomRowTitle();
			formatBackgroundCell(cell, string, false);
		} else {
			Float value = calculateBottomValue(columnIndex);
			if(value.equals(Float.NaN)) {
				string = "";
				formatBackgroundCell(cell, string, false);
			} else {
				string = String.format("%.2f", value);
				formatBackgroundCell(cell, string, false);
			}
		}
		return string;
	}

	abstract protected void formatBackgroundCell(ViewerCell cell, String string, boolean corner);
	
	protected String getTextForFirstRow(Object element) {
		return ((AbstractTableRowData) element).getVariableIndex();
	}
	
	protected String getTextForSecondRow(Object element) {
		return ((AbstractTableRowData) element).getVariableName();
	}
	
	protected String getTextForVariableRow(Object element, int columnIndex) {
		Float value = ((AbstractTableRowData) element)
		.getVariableValueArray().get(columnIndex - 2)
		.getVariableValue();
		if(value == 0) {
			return "";
		} else if(value == Float.POSITIVE_INFINITY || value == Float.NEGATIVE_INFINITY) {
			return "-";
		} else {
			return String.format(getVariableDisplayFormat(), value);
		}
	}

	abstract protected String getBottomRowTitle();

	abstract protected String getVariableDisplayFormat();

	abstract protected float calculateBottomValue(int columnIndex);

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/*public Variable getUnvalidatedVariable() {
		return unvalidatedVariable;
	}

	public void setUnvalidatedVariable(Variable unvalidatedVariable) {
		this.unvalidatedVariable = unvalidatedVariable;
	}

	public VariableValue getUnvalidatedVariableValue() {
		return unvalidatedVariableValue;
	}

	public void setUnvalidatedVariableValue(VariableValue unvalidatedVariableValue) {
		this.unvalidatedVariableValue = unvalidatedVariableValue;
	}*/
}
