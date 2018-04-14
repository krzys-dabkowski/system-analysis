/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.crosseffect;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class CrossEffectTableRowData extends AbstractTableRowData {

	public CrossEffectTableRowData() {
		super();
	}
	
	public CrossEffectTableRowData(Variable variable) {
		super(variable);
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData#getVariableValueArray()
	 */
	@Override
	public ArrayList<VariableValue> getVariableValueArray() {
		return variable.getStrongestEffectsArray();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData#getRightHandValue()
	 */
	@Override
	public float getRightHandValue() {
		return Float.NaN;
	}

}
