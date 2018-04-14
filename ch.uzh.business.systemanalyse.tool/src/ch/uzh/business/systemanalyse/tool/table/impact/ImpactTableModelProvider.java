/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.impact;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class ImpactTableModelProvider extends AbstractTableModelProvider {

	@Override
	protected AbstractTableRowData createRow(Variable variable) {
		return new ImpactTableRowData(variable);
	}

	@Override
	protected AbstractTableRowData createRow() {
		return new ImpactTableRowData();
	}

	
}
