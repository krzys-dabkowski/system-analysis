/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.impact;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class ImpactTableRowData extends AbstractTableRowData{

	public ImpactTableRowData(Variable variable) {
		super(variable);
	}
	
	public ImpactTableRowData() {
		super();
	}

	public ArrayList<VariableValue> getVariableValueArray() {
		return variable.getImpactArray();
	}
	
	public float getRightHandValue() {
		return variable.calculateActiveSum();
	}
}
