/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.table.crossdelay;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class CrossDelayTableModelProvider extends AbstractTableModelProvider {

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider#createRow(ch.uzh.business.systemanalyse.tool.model.Variable)
	 */
	@Override
	protected AbstractTableRowData createRow(Variable variable) {
		return new CrossDelayTableRowData(variable);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider#createRow()
	 */
	@Override
	protected AbstractTableRowData createRow() {
		return new CrossDelayTableRowData();
	}

}
