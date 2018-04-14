/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.outgoinginfluence;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class OutgoingInfluenceTableRowData extends AbstractTableRowData {

	private int rowIndex;
	
	public OutgoingInfluenceTableRowData(int i, Variable var) {
		rowIndex = i;
		variable = var;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData#getVariableValueArray()
	 */
	@Override
	public ArrayList<VariableValue> getVariableValueArray() {
		if(rowIndex == 0) {
			return variable.getShortestPathsArray();
		} else if(rowIndex == 1) {
			return variable.getStrongestEffectsArray();
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData#getRightHandValue()
	 */
	@Override
	public float getRightHandValue() {
		return 0;
	}

	public int getRowIndex() {
		return rowIndex;
	}
}
