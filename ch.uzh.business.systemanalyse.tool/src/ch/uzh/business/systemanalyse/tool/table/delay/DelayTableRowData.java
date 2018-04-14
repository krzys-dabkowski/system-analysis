package ch.uzh.business.systemanalyse.tool.table.delay;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

public class DelayTableRowData extends AbstractTableRowData {

	public DelayTableRowData(Variable variable) {
		super(variable);
	}

	public DelayTableRowData() {
		super();
	}

	@Override
	public ArrayList<VariableValue> getVariableValueArray() {
		return variable.getDelayArray();
	}

	@Override
	public float getRightHandValue() {
		return variable.calculateProducedDelay();
	}

}
