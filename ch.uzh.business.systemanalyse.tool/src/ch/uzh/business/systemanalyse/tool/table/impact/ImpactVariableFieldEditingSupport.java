/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.impact;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class ImpactVariableFieldEditingSupport extends EditingSupport{

	private final TableViewer viewer;
	private final int column;

	public ImpactVariableFieldEditingSupport(TableViewer viewer, int column) {
		super(viewer);
		this.viewer = viewer;
		this.column = column;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] variableValues = Activator.getDefault().getPreferenceStore().getString("IMPACT_VALUES").split("v");
		CellEditor ce = new ComboBoxCellEditor(viewer.getTable(), variableValues);
		return ce;
	}

	@Override
	protected boolean canEdit(Object element) {
		ImpactTableRowData rowData = (ImpactTableRowData)element;
		if(column //- IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS
				== Integer.valueOf(rowData.getVariableIndex())) {
			return false;
		}
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		AbstractTableRowData rowData = (AbstractTableRowData)element;
		int index = column;// - IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS;
		// find variable value to the index index
		float floatValue = 0.0f;
		for(VariableValue vv : rowData.getVariableValueArray()) {
			if(vv.getVariableIndex() == index) {
				floatValue = vv.getVariableValue();
			}
		}
		/*float floatValue = rowData.getVariableValueArray().get(
				index).getVariableValue();*/
		for(int i = 0; i < Activator.getDefault().getPreferenceStore().getString("IMPACT_VALUES").split("v").length; i++) {
			if(floatValue == Float.valueOf(Activator.getDefault().getPreferenceStore().getString("IMPACT_VALUES").split("v")[i].replace(",", "."))) {
				return i;
			}
		}
		return 1;
	}

	@Override
	protected void setValue(Object element, Object value) {
		AbstractTableRowData rowDat = (AbstractTableRowData) element;
		float floatValue = 9;
		floatValue = Float.valueOf(Activator.getDefault().getPreferenceStore().getString("IMPACT_VALUES").split("v")[((Integer)value)].replace(",", "."));

		
		for(VariableValue vv : rowDat.getVariableValueArray()) {
			if(vv.getVariableIndex() == column) {
				vv.setVariableValue(floatValue);
			}
		}
		/*((VariableValue)rowDat.getVariableValueArray().get(column 
				//- IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS
				)).setVariableValue(floatValue);*/
	}

}
