/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.delay;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class DelayTableModelProvider extends AbstractTableModelProvider{

	@Override
	protected AbstractTableRowData createRow(Variable variable) {
		return new DelayTableRowData(variable);
	}

	@Override
	protected AbstractTableRowData createRow() {
		return new DelayTableRowData();
	}

}
