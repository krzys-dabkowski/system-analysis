/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;

/**
 * @author Krzysztof Dabkowski
 *
 */
public abstract class AbstractTableModelProvider {

	protected List<AbstractTableRowData> rows;

	public AbstractTableModelProvider() {
		rows = new ArrayList<AbstractTableRowData>();
		List<Variable> variables = ModelProvider.INSTANCE.getVariables();
		for(Variable variable : variables) {
			AbstractTableRowData rowData = createRow(variable);
			rows.add(rowData);
		}
		rows.add(createRow());
	}
	
	public List<AbstractTableRowData> getRows() {
		return rows;
	}
	
	abstract protected AbstractTableRowData createRow(Variable variable);
	
	abstract protected AbstractTableRowData createRow();
}
