/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.incominginfluence;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class IncomingInfluenceTableRowData extends AbstractTableRowData {

	private int rowIndex;
	//private Variable variable;
	
	public IncomingInfluenceTableRowData(int i, Variable var) {
		rowIndex = i;
		variable = var;
	}
	
	/*public ArrayList<VariableValue> getRow() {
		if(rowIndex == 0) {
			return variable.getShortestPathsArray();
		} else if(rowIndex == 1) {
			return variable.getStrongestEffectsArray();
		}
		return null;
	}*/

	public float getRightHandValue() {
		return 0;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public ArrayList<VariableValue> getVariableValueArray() {
		if(rowIndex == 0) {
			return ModelProvider.INSTANCE.getShortestPathsTo(variable);
		} else if(rowIndex == 1) {
			return ModelProvider.INSTANCE.getStrongestEffectTo(variable);
		}
		return null;
	}

}
