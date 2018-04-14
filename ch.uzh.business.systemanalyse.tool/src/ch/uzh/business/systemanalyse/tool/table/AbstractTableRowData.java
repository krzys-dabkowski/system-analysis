/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;

/**
 * @author Krzysztof Dabkowski
 *
 */
public abstract class AbstractTableRowData {

	protected Variable variable;
	
	public AbstractTableRowData(Variable variable) {
		this.variable = variable;
	}
	
	public AbstractTableRowData() {
		variable = null;
	}

	public String getVariableIndex() {
		return String.valueOf(variable.getIndex());
	}

	public String getVariableName() {
		return variable.getName();
	}
	
	public void setVariableName(String name) {
		variable.setName(name);
	}

	abstract public ArrayList<VariableValue> getVariableValueArray();
	
	abstract public float getRightHandValue();
	
	public Variable getVariable() {
		return variable;
	}
}
